package com.vecondev.buildoptima.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.amazonaws.services.s3.AmazonS3;
import com.vecondev.buildoptima.config.properties.S3ConfigProperties;
import com.vecondev.buildoptima.exception.ResourceNotFoundException;
import com.vecondev.buildoptima.model.user.User;
import com.vecondev.buildoptima.service.s3.AmazonS3ServiceImpl;
import com.vecondev.buildoptima.util.FileUtil;
import com.vecondev.buildoptima.validation.ImageValidator;
import java.io.File;
import java.util.UUID;
import org.assertj.core.util.Files;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

  @InjectMocks private AmazonS3ServiceImpl imageService;
  @Mock private ImageValidator imageValidator;
  @Mock private AmazonS3 s3Client;
  @Mock private S3ConfigProperties configProperties;

  @Test
  void successfulImageUploading() {
    UUID userId = UUID.randomUUID();

    try (MockedStatic<FileUtil> fileUtil = Mockito.mockStatic(FileUtil.class)) {
      fileUtil
          .when(() -> FileUtil.convertMultipartFileToFile(any()))
          .thenReturn(Files.newTemporaryFile());
      fileUtil.when(() -> FileUtil.resizePhoto(any())).thenReturn(Files.newTemporaryFile());
      when(s3Client.doesBucketExistV2(any())).thenReturn(true);
      when(s3Client.doesObjectExist(any(), any())).thenReturn(false);
      imageService.uploadImagesToS3(
          User.class.getSimpleName().toLowerCase(), userId, 1, null, userId);
    }

    verify(s3Client, times(2)).putObject(any(), any(), any(File.class));
    verify(imageValidator).validateImage(any(), any());
  }

  @Test
  void successfulImageUpdating() {
    UUID userId = UUID.randomUUID();

    try (MockedStatic<FileUtil> fileUtil = Mockito.mockStatic(FileUtil.class)) {
      fileUtil
          .when(() -> FileUtil.convertMultipartFileToFile(any()))
          .thenReturn(Files.newTemporaryFile());
      fileUtil.when(() -> FileUtil.resizePhoto(any())).thenReturn(Files.newTemporaryFile());
      when(s3Client.doesBucketExistV2(any())).thenReturn(true);
      when(s3Client.doesObjectExist(any(), any())).thenReturn(true);
      imageService.uploadImagesToS3(
          User.class.getSimpleName().toLowerCase(), userId, 1, null, userId);
    }

    verify(s3Client, times(2)).putObject(any(), any(), any(File.class));
    verify(imageValidator).validateImage(any(), any());
  }

  @Test
  void failedImageUploading() {
    UUID userId = UUID.randomUUID();
    String className = User.class.getSimpleName().toLowerCase();

    when(s3Client.doesBucketExistV2(any())).thenReturn(false);

    assertThrows(
        ResourceNotFoundException.class,
        () -> imageService.uploadImagesToS3(className, userId, 1, null, userId));
  }

  @Test
  void failedImageDownloadingAsImageDoesntExist() {
    UUID userId = UUID.randomUUID();
    String className = User.class.getSimpleName().toLowerCase();

    when(s3Client.doesObjectExist(any(), any())).thenReturn(false);

    assertThrows(
        ResourceNotFoundException.class,
        () -> imageService.downloadImage(className, userId, 1, false));
    verify(configProperties).getImageBucketName();
  }

  @Test
  void successfulImageDeletion() {
    UUID userId = UUID.randomUUID();

    imageService.deleteImagesFromS3(User.class.getSimpleName().toLowerCase(), userId, 1);

    verify(s3Client, times(2)).deleteObject(any(), any());
  }
}
