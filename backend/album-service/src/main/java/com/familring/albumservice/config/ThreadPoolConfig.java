package com.familring.albumservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ThreadPoolConfig {

    /*
    CORE_POOL_SIZE만큼 요청 처리 -> 가득찰 경우 큐에 대기 시킴 -> 큐에 가득찰 경우 MAX_POOL_SIZE만큼 쓰레드 생성
    -> 초과 되는 요청은 setRejectedExecutionHandler 정책 따라감
     */
    private static final int CORE_POOL_SIZE = 30; // 동시에 실행 할 쓰레드수
    private static final int MAX_POOL_SIZE = 40; // 쓰레드 풀이 사용하는 최대 쓰레드 수
    private static final int QUEUE_CAPACITY = 100; // CORE POOL SIZE가 가득 찼을 경우 요청 대기시키는 큐의 개수
    private static final boolean WAIT_TASK_COMPLETE = false;  // 어플리케이션이 종료될 때 큐에 대기중인 작업이 완료될 때까지 기다릴지 여부

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(CORE_POOL_SIZE);
        taskExecutor.setMaxPoolSize(MAX_POOL_SIZE);
        taskExecutor.setQueueCapacity(QUEUE_CAPACITY);
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        taskExecutor.setWaitForTasksToCompleteOnShutdown(WAIT_TASK_COMPLETE);
        taskExecutor.initialize();
        return taskExecutor;
    }
}
