package com.vecondev.buildoptima.api.controller;

import com.vecondev.buildoptima.api.UserApi;
import com.vecondev.buildoptima.dto.ImageOverview;
import com.vecondev.buildoptima.dto.filter.FetchRequestDto;
import com.vecondev.buildoptima.dto.filter.FetchResponseDto;
import com.vecondev.buildoptima.dto.user.request.ChangePasswordRequestDto;
import com.vecondev.buildoptima.dto.user.request.EditUserDto;
import com.vecondev.buildoptima.dto.user.response.UserResponseDto;
import com.vecondev.buildoptima.security.user.AppUserDetails;
import com.vecondev.buildoptima.service.user.UserService;
import java.util.UUID;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController implements UserApi {

  private final UserService userService;

  @Override
  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('resource_write')")
  public ResponseEntity<UserResponseDto> getById(
      @PathVariable("id") UUID id, @AuthenticationPrincipal AppUserDetails user) {
    return ResponseEntity.ok(userService.getById(id));
  }

  @Override
  @GetMapping("/me")
  public ResponseEntity<UserResponseDto> getCurrentUser() {
    return ResponseEntity.ok(userService.getCurrentUser());
  }

  @Override
  @PostMapping("/fetch")
  @PreAuthorize("hasAuthority('resource_write')")
  public ResponseEntity<FetchResponseDto> fetch(@RequestBody FetchRequestDto fetchRequest) {
    return ResponseEntity.ok(userService.fetch(fetchRequest));
  }

  @Override
  @PutMapping("/password/change")
  public ResponseEntity<Void> changePassword(@RequestBody @Valid ChangePasswordRequestDto request) {
    userService.changePassword(request);
    return ResponseEntity.ok().build();
  }

  @Override
  @PatchMapping("/{id}")
  @PreAuthorize("#user.id == #id")
  public ResponseEntity<UserResponseDto> editUser(
      @PathVariable UUID id,
      @AuthenticationPrincipal AppUserDetails user,
      @RequestBody @Valid EditUserDto editUserDto) {
    return ResponseEntity.ok(userService.edit(id, editUserDto));
  }

  @Override
  @PostMapping(
      value = "/{id}/image",
      consumes = {"multipart/form-data"})
  @PreAuthorize("#user.id == #id")
  public ResponseEntity<ImageOverview> uploadImage(
      @PathVariable UUID id,
      @RequestParam("file") MultipartFile multipartFile,
      @AuthenticationPrincipal AppUserDetails user) {
    return ResponseEntity.ok(userService.uploadImage(id, multipartFile));
  }

  @Override
  @GetMapping(value = "/{id}/image")
  @PreAuthorize("#user.id == #ownerId or hasRole('ADMIN')")
  public ResponseEntity<byte[]> downloadOriginalImage(
      @PathVariable("id") UUID ownerId, @AuthenticationPrincipal AppUserDetails user) {
    return userService.downloadImage(ownerId, true);
  }

  @Override
  @GetMapping(value = "/{id}/thumbnail-image")
  @PreAuthorize("#user.id == #ownerId or hasRole('ADMIN')")
  public ResponseEntity<byte[]> downloadThumbnailImage(
      @PathVariable("id") UUID ownerId, @AuthenticationPrincipal AppUserDetails user) {
    return userService.downloadImage(ownerId, false);
  }

  @Override
  @DeleteMapping(value = "/{id}/image")
  @PreAuthorize("#user.id == #ownerId or hasRole('ADMIN')")
  public ResponseEntity<Void> deleteImage(
      @PathVariable("id") UUID ownerId, @AuthenticationPrincipal AppUserDetails user) {
    userService.deleteImage(ownerId);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
