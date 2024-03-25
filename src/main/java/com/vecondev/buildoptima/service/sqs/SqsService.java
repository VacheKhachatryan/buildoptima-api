package com.vecondev.buildoptima.service.sqs;

public interface SqsService {

  void sendMessage(String message);

  void receive(String message);
}