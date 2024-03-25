package com.vecondev.buildoptima.service.user;

import com.vecondev.buildoptima.dto.ImageOverview;
import com.vecondev.buildoptima.dto.filter.FetchRequestDto;
import com.vecondev.buildoptima.dto.filter.FetchResponseDto;
import com.vecondev.buildoptima.dto.user.request.ChangePasswordRequestDto;
import com.vecondev.buildoptima.dto.user.request.EditUserDto;
import com.vecondev.buildoptima.dto.user.response.UserResponseDto;
import com.vecondev.buildoptima.model.user.User;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

  FetchResponseDto fetch(FetchRequestDto viewRequest);

  void changePassword(ChangePasswordRequestDto request);

  UserResponseDto edit(UUID id, EditUserDto editUserDto);

  UserResponseDto getById(UUID userId);

  UserResponseDto getCurrentUser();

  ImageOverview uploadImage(UUID userId, MultipartFile multipartFile);

  ResponseEntity<byte[]> downloadImage(UUID ownerId, boolean isOriginal);

  void deleteImage(UUID userId);

  User findUserById(UUID userId);
}
