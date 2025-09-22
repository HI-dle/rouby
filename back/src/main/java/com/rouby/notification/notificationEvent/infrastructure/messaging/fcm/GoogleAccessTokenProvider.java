package com.rouby.notification.notificationEvent.infrastructure.messaging.fcm;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GoogleAccessTokenProvider {

  private static final long SKEW_MS = Duration.ofMinutes(2).toMillis(); // 만료 2분 전 갱신

  private final GoogleCredentials fcmGoogleCredentials;
  private final AtomicReference<AccessToken> tokenRef = new AtomicReference<>();
  private final AtomicBoolean refreshing = new AtomicBoolean(false);

  @PostConstruct
  void prewarm() throws IOException {

    fcmGoogleCredentials.refreshIfExpired();
    AccessToken t = fcmGoogleCredentials.getAccessToken();
    if (t != null) tokenRef.set(t);
  }

  public String fetchAccessToken() {

    AccessToken cur = tokenRef.get();
    if (cur != null && !isExpiringSoon(cur)) {
      return cur.getTokenValue();
    }

    if (refreshing.compareAndSet(false, true)) {
      try {
        fcmGoogleCredentials.refresh();
        AccessToken fresh = fcmGoogleCredentials.getAccessToken();

        if (fresh == null)
          throw new IllegalStateException("AccessToken이 리프레시 수행 후 null 값으로 확인되었습니다.");

        tokenRef.set(fresh);
        return fresh.getTokenValue();
      } catch (IOException e) {

        AccessToken fallback = tokenRef.get();
        if (fallback != null && !isExpiringSoon(fallback))
          return fallback.getTokenValue();
        throw new RuntimeException("FCM을 위한 Google AccessToken의 갱신을 실패하였습니다.", e);
      } finally {
        refreshing.set(false);
      }
    }

    AccessToken t = tokenRef.get();
    if (t != null) return t.getTokenValue();

    throw new IllegalStateException("FCM을 위한 Google AccessToken이 사용 가능하지 않습니다. (현재 리프레시 동작 중)");
  }

  private boolean isExpiringSoon(AccessToken t) {

    Date exp = t.getExpirationTime();
    if (exp == null) return true;
    long remain = exp.getTime() - System.currentTimeMillis();
    return remain <= SKEW_MS;
  }
}
