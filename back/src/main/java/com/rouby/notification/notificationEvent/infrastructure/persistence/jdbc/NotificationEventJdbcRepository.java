package com.rouby.notification.notificationEvent.infrastructure.persistence.jdbc;

import com.rouby.notification.notificationEvent.domain.entity.DeviceTokenInfo;
import com.rouby.notification.notificationEvent.domain.entity.NotificationMessage;
import com.rouby.notification.notificationEvent.domain.entity.TokenProviderType;
import com.rouby.notification.notificationEvent.domain.info.NotificationEventInfo;
import com.rouby.notification.notificationEvent.domain.info.SuccessResult;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificationEventJdbcRepository {

  private final JdbcTemplate jdbcTemplate;

  public List<NotificationEventInfo> claimSlot(Instant slotStart, Instant slotEnd, int limit, String workerId, int leaseSeconds) {

    final String sql = """
      with picked as (
        select id
        from notification_event
        where status = 'PENDING'
          and due_at >= ?::timestamptz
          and due_at <  ?::timestamptz
        order by due_at
        for update skip locked
        limit ?
      )
      update notification_event e
      set status      = 'IN_PROGRESS',
          lease_until = e.due_at + make_interval(secs => ?),
          worker_id   = ?,
          attempt     = coalesce(e.attempt, 0) + 1,
          updated_at  = now()
      from picked
      where e.id = picked.id
      returning e.id, e.user_id, e.due_at, e.title, e.body, e.url, e.device_token, e.token_provider
      """;

    return jdbcTemplate.query(sql,
        (rs,i) -> map(rs),
        Timestamp.from(slotStart),
        Timestamp.from(slotEnd),
        limit,
        leaseSeconds,
        workerId);
  }

  public List<NotificationEventInfo> claimBackfill(
      int minutes, int maxAttempt, int limit, String workerId, int leaseSeconds) {

    final String sql = """
      with picked as (
        select id
        from notification_event
        where status='PENDING'
          and due_at <= now()
          and due_at >= now() - make_interval(mins => ?)
          and (retry_at IS NULL OR retry_at <= now())
          and attempt < ?
        order by due_at
        for update skip locked
        limit ?
      )
      update notification_event e
      set status='IN_PROGRESS',
          lease_until = now() + make_interval(secs => ?),
          worker_id   = ?,
          attempt     = e.attempt + 1,
          updated_at  = now()
      from picked
      where e.id = picked.id
      returning e.id, e.user_id, e.due_at, e.title, e.body, e.url, e.device_token, e.token_provider
    """;

    return jdbcTemplate.query(sql,
        (rs,i) -> map(rs),
        minutes,
        maxAttempt,
        limit,
        leaseSeconds,
        workerId);
  }

  public int markSent(List<SuccessResult> successResults, String workerId) {

    if (successResults.isEmpty()) return 0;

    final String sql = """
      UPDATE notification_event AS e
      SET
        status     = 'SENT',
        lease_until= NULL,
        sent_at    = t.sent_at,
        updated_at = now()
      FROM unnest(?::bigint[], ?::timestamptz[]) AS t(id, sent_at)
      WHERE e.id = t.id
        AND e.status   = 'IN_PROGRESS'
        AND e.worker_id = ?;
    """;

    Long[] ids = successResults.stream()
        .map(r -> r.id())
        .toArray(Long[]::new);
    Timestamp[] sentAts = successResults.stream()
        .map(r -> Timestamp.from(Instant.ofEpochMilli(r.sentAt())))
        .toArray(Timestamp[]::new);

    return jdbcTemplate.execute((Connection con) -> {
      try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setArray(1, con.createArrayOf("bigint", ids));
        ps.setArray(2, con.createArrayOf("timestamptz", sentAts));
        ps.setString(3, workerId);
        return ps.executeUpdate();
      }
    });
  }

  public int markRetry(List<Long> ids, String workerId) {

    if (ids.isEmpty()) return 0;

    final String sql = """
      update notification_event e
      set status='PENDING',
          retry_at = now() + make_interval(secs => LEAST(300, 5 * cast(power(2, greatest(0, e.attempt-1)) as int))),
          lease_until=null,
          updated_at=now()
      from unnest(?::bigint[]) as t(id)
      where e.id = t.id and e.status='IN_PROGRESS' and e.worker_id = ?
    """;
    return executeSqlWithArray(sql, ids, workerId);
  }

  public int recoverExpiredLeases() {

    final String sql = """
      update notification_event
      set 
          status='PENDING', 
          lease_until=null, 
          worker_id=null, 
          updated_at=now()
      where 
          status='IN_PROGRESS' 
        and lease_until < now()
    """;
    return jdbcTemplate.update(sql);
  }

  private NotificationEventInfo map(ResultSet rs) throws SQLException {

    return NotificationEventInfo.builder()
        .id(rs.getLong("id"))
        .userId(rs.getLong("user_id"))
        .dueAt(rs.getTimestamp("due_at").toLocalDateTime())
        .message(NotificationMessage.builder()
            .title(rs.getString("title"))
            .body(rs.getString("body"))
            .url(rs.getString("url"))
            .build())
        .deviceTokenInfo(DeviceTokenInfo.builder()
            .tokenProvider(TokenProviderType.parse(rs.getString("token_provider")))
            .deviceToken(rs.getString("device_token"))
            .build())
        .build();
  }

  private int executeSqlWithArray(String sql, List<Long> ids, String workerId) {

    return jdbcTemplate.execute((ConnectionCallback<Integer>) con -> {
      Array array = con.createArrayOf("bigint", ids.toArray());

      try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setArray(1, array);
        ps.setString(2, workerId);
        return ps.executeUpdate();
      } finally {
        array.free();
      }
    });
  }
}
