package com.rouby.user.device.presentation;

import com.rouby.user.device.application.facade.UserDeviceFacade;
import com.rouby.user.device.presentation.dto.request.DeleteUserDeviceRequest;
import com.rouby.user.device.presentation.dto.request.RegisterUserDeviceRequest;
import com.rouby.user.user.infrastructure.security.dto.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users/devices")
@RestController
public class UserDeviceController {

  private final UserDeviceFacade userDeviceFacade;

  @PreAuthorize("hasAnyRole('USER')")
  @PostMapping
  public ResponseEntity<Void> registerUserDevice(
      @AuthenticationPrincipal SecurityUser securityUser,
      @RequestBody @Validated RegisterUserDeviceRequest request) {

    userDeviceFacade.register(request.toCommand(securityUser.getId()));

    return ResponseEntity.noContent().build();
  }

  @PreAuthorize("hasAnyRole('USER')")
  @DeleteMapping
  public ResponseEntity<Void> deleteUserDevice(
      @AuthenticationPrincipal SecurityUser securityUser,
      @Validated DeleteUserDeviceRequest request) {

    userDeviceFacade.hardDelete(request.toCommand(securityUser.getId()));

    return ResponseEntity.noContent().build();
  }

  @PreAuthorize("hasAnyRole('USER')")
  @DeleteMapping("/all")
  public ResponseEntity<Void> deleteAllUserDevice(
      @AuthenticationPrincipal SecurityUser securityUser) {

    userDeviceFacade.hardDeleteAllByUser(securityUser.getId());

    return ResponseEntity.noContent().build();
  }
}
