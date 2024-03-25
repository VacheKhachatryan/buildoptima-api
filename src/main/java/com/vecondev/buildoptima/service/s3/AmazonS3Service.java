package com.vecondev.buildoptima.service.s3;

import com.amazonaws.services.s3.model.S3Object;
import java.io.File;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface AmazonS3Service {

  void uploadImagesToS3(
      String className,
      UUID objectId,
      Integer imageVersion,
      MultipartFile multipartFile,
      UUID userId);

  byte[] downloadImage(String className, UUID objectId, Integer imageVersion, Boolean isOriginal);

  String getContentTypeOfObject(
      String className, UUID objectId, Integer imageVersion, boolean isOriginal);

  void deleteImagesFromS3(String className, UUID objectId, Integer imageVersion);

  void checkExistenceOfObject(String imageName, UUID userId);

  String getImagePath(String className, UUID objectId, Integer imageVersion, boolean isOriginal);

  List<S3Object> getObjects(String bucketName);

  S3Object getObject(String bucketName, String objectKey);

  boolean doesObjectExist(String bucketName, String objectKey);

  void uploadJsonObject(String objectKey, File file);
}
