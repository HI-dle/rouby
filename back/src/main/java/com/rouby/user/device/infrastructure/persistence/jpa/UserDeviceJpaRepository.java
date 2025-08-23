package com.rouby.user.device.infrastructure.persistence.jpa;

import com.rouby.user.device.domain.entity.UserDevice;
import com.rouby.user.device.domain.entity.vo.DeviceInfo;
import com.rouby.user.device.domain.entity.vo.DeviceTokenInfo;
import com.rouby.user.device.domain.repository.UserDeviceRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserDeviceJpaRepository extends JpaRepository<UserDevice, Long>,
    UserDeviceRepository {

  @Override
  Optional<UserDevice> findByUserIdAndTokenInfo_DeviceToken(Long userId, String deviceToken);

  @Override
  int deleteByLastActiveAtBefore(LocalDateTime threshold);

  @Override
  @Modifying
  @Query(value = """
                INSERT INTO user_device (
                    user_id, 
                    device_token, 
                    token_provider, 
                    app_version, 
                    app_type,
                    device_type, 
                    browser, 
                    os,
                    user_agent, 
                    registered_at, 
                    last_active_at
                ) VALUES (
                    :userId, 
                    :#{#tokenInfo.deviceToken}, 
                    :#{#tokenInfo.tokenProvider.name()},
                    :#{#info.appVersion},
                    :#{#info.appType.name()}, 
                    :#{#info.deviceType.name()}, 
                    :#{#info.browser}, 
                    :#{#info.os},
                    :#{#info.userAgent},
                    NOW(),
                    NOW()
                )
                ON CONFLICT (user_id, device_token) DO UPDATE
                SET
                    token_provider = EXCLUDED.token_provider,
                    app_version    = EXCLUDED.app_version,
                    app_type       = EXCLUDED.app_type,
                    device_type    = EXCLUDED.device_type,
                    os             = EXCLUDED.os,
                    browser        = EXCLUDED.browser,
                    user_agent     = EXCLUDED.user_agent,
                    last_active_at = NOW()
                WHERE ROW(
                        user_device.token_provider,
                        user_device.app_version,
                        user_device.app_type,
                        user_device.device_type,
                        user_device.os,
                        user_device.browser,
                        user_device.user_agent
                    ) IS DISTINCT FROM ROW(
                        EXCLUDED.token_provider,
                        EXCLUDED.app_version,
                        EXCLUDED.app_type,
                        EXCLUDED.device_type,
                        EXCLUDED.os,
                        EXCLUDED.browser,
                        EXCLUDED.user_agent
                    );
  """, nativeQuery = true)
  int upsertUserDevice(@Param("userId") Long userId,
      @Param("tokenInfo") DeviceTokenInfo tokenInfo,
      @Param("info") DeviceInfo info);
}
