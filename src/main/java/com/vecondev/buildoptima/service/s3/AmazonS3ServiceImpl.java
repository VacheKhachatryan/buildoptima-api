package com.vecondev.buildoptima.service.s3;

import static com.vecondev.buildoptima.exception.Error.BUCKET_NOT_FOUND;
import static com.vecondev.buildoptima.exception.Error.FAILED_IMAGE_CONVERTING;
import static com.vecondev.buildoptima.exception.Error.IMAGE_NOT_FOUND;
import static com.vecondev.buildoptima.util.FileUtil.*;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;
import com.vecondev.buildoptima.config.properties.S3ConfigProperties;
import com.vecondev.buildoptima.exception.ConvertingFailedException;
import com.vecondev.buildoptima.exception.ResourceNotFoundException;
import com.vecondev.buildoptima.validation.ImageValidator;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Data
@Service
@RequiredArgsConstructor
public class AmazonS3ServiceImpl implements AmazonS3Service {

  private static final String ORIGINAL_IMAGES_FOLDER_NAME = "original";
  private static final String THUMBNAIL_IMAGES_FOLDER_NAME = "thumbnail";
  private static final String STATIC_JSON_FILES_FOLDER_NAME = "static-api/json/";
  private final S3ConfigProperties s3ConfigProperties;
  private final AmazonS3 amazonS3;
  private final ImageValidator imageValidator;

  /**
   * Checks the existence of the bucket, uploads original image 'and' it's thumbnail version by
   * resizing with size of 100x100, after all these actions deletes created images from classpath.
   *
   * @param objectId the id of the image owner entity
   * @param multipartFile representing images
   */
  @Override
  public void uploadImagesToS3(
      String className,
      UUID objectId,
      Integer imageVersion,
      MultipartFile multipartFile,
      UUID userId) {
    checkExistenceOfBucket(s3ConfigProperties.getImageBucketName());
    imageValidator.validateImage(multipartFile, userId);
    File originalFile = convertMultipartFileToFile(multipartFile);
    uploadImage(className, originalFile, objectId, imageVersion + 1, true);
    File thumbnailFile = resizePhoto(originalFile);
    uploadImage(className, thumbnailFile, objectId, imageVersion + 1, false);

    if (amazonS3.doesObjectExist(
        s3ConfigProperties.getImageBucketName(),
        getImagePath(className, objectId, imageVersion, true))) {
      deleteImage(className, objectId, imageVersion, true);
    }
    if (amazonS3.doesObjectExist(
        s3ConfigProperties.getImageBucketName(),
        getImagePath(className, objectId, imageVersion, false))) {
      deleteImage(className, objectId, imageVersion, false);
    }
    deleteFile(originalFile);
    deleteFile(thumbnailFile);
  }

  /**
   * Downloads image from s3 by given entity id.
   *
   * @param className shows in which entity the image belongs to e.g. user, news
   * @param objectId the resource owner
   * @param isOriginal shows if the image is original or not (thumbnail)
   * @return byte[] image as byte array
   */
  @Override
  public byte[] downloadImage(
      String className, UUID objectId, Integer imageVersion, Boolean isOriginal) {
    String imageName = getImagePath(className, objectId, imageVersion, isOriginal);
    checkExistenceOfObject(imageName, objectId);
    S3Object object = amazonS3.getObject(s3ConfigProperties.getImageBucketName(), imageName);
    S3ObjectInputStream inputStream = object.getObjectContent();
    byte[] objectAsByteArray;

    try (inputStream) {
      objectAsByteArray = IOUtils.toByteArray(inputStream);
    } catch (IOException ex) {
      log.error("Error while downloading photo of {} with id: {}.", className, objectId);
      throw new ConvertingFailedException(FAILED_IMAGE_CONVERTING);
    }

    return objectAsByteArray;
  }

  @Override
  public String getContentTypeOfObject(
      String className, UUID objectId, Integer imageVersion, boolean isOriginal) {
    String imageName = getImagePath(className, objectId, imageVersion, isOriginal);

    return amazonS3
        .getObject(s3ConfigProperties.getImageBucketName(), imageName)
        .getObjectMetadata()
        .getContentType();
  }

  /**
   * Deletes both images the original and thumbnail.
   *
   * @param objectId the image owner id which should be deleted
   */
  @Override
  public void deleteImagesFromS3(String className, UUID objectId, Integer imageVersion) {
    deleteImage(className, objectId, imageVersion, true);
    deleteImage(className, objectId, imageVersion, false);
  }

  /**
   * Checks if there is an object with given image name in s3 bucket or not.
   *
   * @throws ResourceNotFoundException when no image found by given image name
   */
  @Override
  public void checkExistenceOfObject(String imagePath, UUID userId) {
    if (!amazonS3.doesObjectExist(s3ConfigProperties.getImageBucketName(), imagePath)) {
      log.warn("There is no image of user with id: {} to remove.", userId);
      throw new ResourceNotFoundException(IMAGE_NOT_FOUND);
    }
  }

  /**
   * Forms the path image should be saved in S3 bucket.
   *
   * @param isOriginal whether it's original (true) or thumbnail version
   * @return the image path in S3
   */
  @Override
  public String getImagePath(
      String className, UUID objectId, Integer imageVersion, boolean isOriginal) {
    return String.format(
        "%s/%s/%s/%s",
        className,
        objectId,
        isOriginal ? ORIGINAL_IMAGES_FOLDER_NAME : THUMBNAIL_IMAGES_FOLDER_NAME,
        imageVersion);
  }

  @Override
  public List<S3Object> getObjects(String bucketName) {
    checkExistenceOfBucket(bucketName);
    return amazonS3.listObjectsV2(bucketName).getObjectSummaries().stream()
        .map(S3ObjectSummary::getKey)
        .map(key -> getObject(bucketName, key))
        .toList();
  }

  @Override
  public S3Object getObject(String bucketName, String objectKey) {
    return amazonS3.getObject(bucketName, objectKey);
  }

  @Override
  public boolean doesObjectExist(String bucketName, String objectKey) {
    return amazonS3.doesObjectExist(bucketName, objectKey);
  }

  @Override
  public void uploadJsonObject(String objectKey, File file) {
    amazonS3.putObject(
        s3ConfigProperties.getWebBucketName(), STATIC_JSON_FILES_FOLDER_NAME + objectKey, file);
  }

  private void checkExistenceOfBucket(String bucketName) {
    if (!amazonS3.doesBucketExistV2(bucketName)) {
      log.error("The '{}' bucket doesn't exist!", bucketName);
      throw new ResourceNotFoundException(BUCKET_NOT_FOUND);
    }
  }

  private void uploadImage(
      String className, File file, UUID objectId, Integer imageVersion, boolean isOriginal) {
    String imagePath = getImagePath(className, objectId, imageVersion, isOriginal);

    if (amazonS3.doesObjectExist(s3ConfigProperties.getImageBucketName(), imagePath)) {
      log.info("The old {} image of item: {} is deleted.", imagePath, objectId);
    }

    amazonS3.putObject(s3ConfigProperties.getImageBucketName(), imagePath, file);
    log.info("New picture has been uploaded for news item {}", imagePath);
  }

  private void deleteImage(
      String className, UUID objectId, Integer imageVersion, boolean isOriginal) {
    String imageName = getImagePath(className, objectId, imageVersion, isOriginal);

    amazonS3.deleteObject(s3ConfigProperties.getImageBucketName(), imageName);
    log.info(
        "The (id: {}) {} image is successfully deleted.",
        objectId,
        isOriginal ? ORIGINAL_IMAGES_FOLDER_NAME : THUMBNAIL_IMAGES_FOLDER_NAME);
  }
}
