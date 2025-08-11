package com.rouby.user.device.domain.entity;

import com.rouby.user.device.domain.entity.vo.DeviceInfo;
import com.rouby.user.device.domain.entity.vo.DeviceTokenInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    name = "user_device",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_device_user_id_device_token",
            columnNames = {"user_id", "device_token"})
    }
)
public class UserDevice {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(nullable = false)
  private Long userId;

  private DeviceTokenInfo tokenInfo;

  private DeviceInfo deviceInfo;

  @CreatedDate
  private LocalDateTime registeredAt;

  @LastModifiedDate
  private LocalDateTime lastActiveAt;

  @Builder
  private UserDevice (Long userId,  DeviceTokenInfo tokenInfo, DeviceInfo deviceInfo) {
  }

  public boolean ifDeviceInfoChanged(DeviceInfo deviceInfo) {
    return this.deviceInfo.equals(deviceInfo);
  }

  public void modifyDeviceInfo(DeviceInfo deviceInfo) {
    this.deviceInfo = deviceInfo;
  }
}
