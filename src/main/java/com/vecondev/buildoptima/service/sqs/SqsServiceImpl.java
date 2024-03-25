package com.vecondev.buildoptima.service.sqs;

import com.amazonaws.services.s3.event.S3EventNotification;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.vecondev.buildoptima.config.properties.S3ConfigProperties;
import com.vecondev.buildoptima.config.properties.SqsConfigProperties;
import com.vecondev.buildoptima.exception.Error;
import com.vecondev.buildoptima.exception.SqsException;
import com.vecondev.buildoptima.service.property.PropertyMigrationService;
import com.vecondev.buildoptima.service.s3.AmazonS3Service;
import com.vecondev.buildoptima.util.JsonUtil;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SqsServiceImpl implements SqsService {

  private final S3ConfigProperties s3ConfigProperties;
  private final AmazonS3Service amazonS3Service;
  private final PropertyMigrationService migrationService;
  private final AsyncTaskExecutor asyncExecutor;
  private final SqsConfigProperties sqsConfigProperties;
  private final AmazonSQSAsync amazonSqsAsync;

  @Override
  public void sendMessage(String message) {
    SendMessageRequest request =
        new SendMessageRequest()
            .withQueueUrl(sqsConfigProperties.getMailQueueUrl())
            .withMessageBody(message)
            .withDelaySeconds(20);
    amazonSqsAsync.sendMessage(request);
  }

  @SqsListener(
      value = "${sqs.propertyQueueName}",
      deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
  public void receive(String message) {
    asyncExecutor.submit(
        () -> {
          try {
            S3EventNotification notification = JsonUtil.getNotification(message);
            if (notification != null && notification.getRecords() != null) {
              log.info("Message received from sqs: {}", message);
              notification.getRecords().stream()
                  .map(rec -> rec.getS3().getObject().getKey())
                  .map(
                      key -> amazonS3Service.getObject(s3ConfigProperties.getDataBucketName(), key))
                  .filter(Objects::nonNull)
                  .forEach(migrationService::migrateFromS3);
            }
          } catch (IOException e) {
            throw new SqsException(Error.FAILED_DATA_DOWNLOAD);
          }
        });
  }
}