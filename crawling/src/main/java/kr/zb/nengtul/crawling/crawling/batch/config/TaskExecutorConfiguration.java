package kr.zb.nengtul.crawling.crawling.batch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class TaskExecutorConfiguration {

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // 스레드 풀의 기본 크기
        executor.setMaxPoolSize(50); // 스레드 풀의 최대 크기
        executor.setQueueCapacity(100); // 대기 큐의 크기
        executor.setThreadNamePrefix("BatchTaskExecutor-"); // 스레드 이름 접두사 설정
        executor.initialize();
        return executor;
    }
}
