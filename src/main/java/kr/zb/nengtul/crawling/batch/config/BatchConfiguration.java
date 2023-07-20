package kr.zb.nengtul.crawling.batch.config;

import kr.zb.nengtul.crawling.batch.CrawlingItemProcessor;
import kr.zb.nengtul.crawling.batch.CrawlingItemReader;
import kr.zb.nengtul.crawling.batch.CrawlingItemWriter;
import kr.zb.nengtul.board.domain.Recipe;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class BatchConfiguration {

    private final CrawlingItemReader crawlingItemReader;
    private final CrawlingItemProcessor crawlingItemProcessor;
    private final CrawlingItemWriter crawlingItemWriter;
    private final TaskExecutor taskExecutor;


    @Bean
    public Job crawlingJob(JobRepository jobRepository, Step crawlingStep) {
        return new JobBuilder("crawlingJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(crawlingStep)
                .end()
                .build();
    }

    @Bean
    public Step crawlingStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager
    ) {
        int CHUNK_SIZE = 10;
        return new StepBuilder("crawlingStep", jobRepository)
                .<String, Recipe>chunk(CHUNK_SIZE, transactionManager)
                .reader(crawlingItemReader)
                .processor(crawlingItemProcessor)
                .writer(crawlingItemWriter)
                .taskExecutor(taskExecutor)
                .build();
    }


}

