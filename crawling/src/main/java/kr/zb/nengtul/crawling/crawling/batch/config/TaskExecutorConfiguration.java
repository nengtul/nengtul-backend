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
        int coreCount = Runtime.getRuntime().availableProcessors(); // 현재 시스템의 CPU 코어 수 가져오기
        executor.setCorePoolSize(coreCount + 2);
        executor.setMaxPoolSize(coreCount * 2);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("BatchTaskExecutor-"); // 스레드 이름 접두사 설정
        executor.initialize();
        return executor;
    }
}
