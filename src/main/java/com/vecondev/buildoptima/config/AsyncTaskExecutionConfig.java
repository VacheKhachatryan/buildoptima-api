package com.vecondev.buildoptima.config;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncTaskExecutionConfig {

  private static final int POOL_SIZE = 100;

  @Bean(name = "asyncExecutor")
  public ThreadPoolTaskExecutor threadPoolTaskExecutorQueue() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setMaxPoolSize(POOL_SIZE);
    executor.setQueueCapacity(0);
    executor.setRejectedExecutionHandler(new BlockingTaskSubmissionPolicy(1000));
    return executor;
  }

  @RequiredArgsConstructor
  private static class BlockingTaskSubmissionPolicy implements RejectedExecutionHandler {
    private final long timeout;

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
      try {
        BlockingQueue<Runnable> queue = executor.getQueue();
        if (!queue.offer(r, this.timeout, TimeUnit.MILLISECONDS)) {
          throw new RejectedExecutionException("The Thread Pool is full");
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }
}