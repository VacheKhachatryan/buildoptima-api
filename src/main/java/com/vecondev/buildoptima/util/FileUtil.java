package com.vecondev.buildoptima.util;

import static com.vecondev.buildoptima.exception.Error.FAILED_FILE_DELETION;
import static com.vecondev.buildoptima.exception.Error.FAILED_IMAGE_CONVERTING;
import static com.vecondev.buildoptima.exception.Error.FAILED_IMAGE_RESIZING;
import static com.vecondev.buildoptima.exception.Error.FAILED_MULTIPART_CONVERTING;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.vecondev.buildoptima.exception.ConvertingFailedException;
import com.vecondev.buildoptima.exception.FailedFileOperationException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import javax.imageio.ImageIO;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@UtilityClass
public class FileUtil {

  private static final Integer THUMBNAIL_WIDTH = 100;
  private static final Integer THUMBNAIL_HEIGHT = 100;

  public File convertMultipartFileToFile(MultipartFile multipartFile) {
    File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));

    try (FileOutputStream fos = new FileOutputStream(file)) {
      fos.write(multipartFile.getBytes());
    } catch (IOException e) {
      log.error("Error occurred while converting multipart file to file.");
      throw new ConvertingFailedException(FAILED_MULTIPART_CONVERTING);
    }

    return file;
  }

  /**
   * Resizes original photo to get thumbnail version with size of 100X100.
   *
   * @param originalFile the original version of photo
   * @return File the thumbnail version of original photo
   */
  public File resizePhoto(File originalFile) {
    try {
      BufferedImage originalImage = convertFileToImage(originalFile);
      BufferedImage thumbnailImage = Scalr.resize(originalImage, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);

      String originalPath = originalFile.getAbsolutePath();
      String extension = originalPath.substring(originalPath.lastIndexOf(".") + 1);
      String thumbnailPath =
          String.format(
              "%s-%sx%s.%s",
              originalPath.substring(0, originalPath.lastIndexOf(".") - 1),
              THUMBNAIL_WIDTH,
              THUMBNAIL_HEIGHT,
              extension);

      File thumbnailFile = new File(thumbnailPath);
      ImageIO.write(thumbnailImage, extension, thumbnailFile);

      return thumbnailFile;
    } catch (IOException ex) {
      log.error("Failed to resize the original photo to get thumbnail version.");
      throw new ConvertingFailedException(FAILED_IMAGE_RESIZING);
    }
  }

  public BufferedImage convertFileToImage(File file) {
    try {
      return ImageIO.read(file);
    } catch (IOException ex) {
      log.error("Failed to convert file to image.");
      throw new ConvertingFailedException(FAILED_IMAGE_CONVERTING);
    }
  }

  /** Deletes file from source directory. */
  public void deleteFile(File file) {
    try {
      Files.deleteIfExists(file.toPath());
      log.info(
          "The file with name: {} is successfully deleted from local storage.", file.getName());
    } catch (IOException ex) {
      log.error("Failed to delete file with name: {}", file.getName());
      throw new FailedFileOperationException(FAILED_FILE_DELETION);
    }
  }

  public static Path convertS3ObjectToPath(S3Object s3Object) throws IOException {
    S3ObjectInputStream inputStream = s3Object.getObjectContent();
    byte[] bytes = IOUtils.toByteArray(inputStream);
    return convertByteArrayToJsonPath(bytes);
  }

  private Path convertByteArrayToJsonPath(byte[] bytes) throws IOException {
    Path gzipFile = convertByteArrayToPathList(bytes);
    Path path = decompressGzipFile(gzipFile);
    Files.delete(gzipFile);
    log.info("Removed unzipped files from local memory");
    return path;
  }

  private Path convertByteArrayToPathList(byte[] bytes) throws IOException {
    Path tempFile = Files.createTempFile(String.valueOf(UUID.randomUUID()), ".json.gz");
    Files.write(tempFile, bytes);
    return tempFile;
  }

  private Path decompressGzipFile(Path gzipFile) throws IOException {
    Path path = Files.createTempFile(String.valueOf(UUID.randomUUID()), ".json");
    try (FileInputStream fis = new FileInputStream(gzipFile.toFile().getAbsolutePath());
        GZIPInputStream gis = new GZIPInputStream(fis);
        FileOutputStream fos = new FileOutputStream(path.toFile().getAbsolutePath())) {
      byte[] buffer = new byte[1024];
      int len;
      while ((len = gis.read(buffer)) != -1) {
        fos.write(buffer, 0, len);
      }
    }
    return path;
  }
}
