package com.cfa.jobs.SimpleJob;

import com.cfa.jobs.jobexample.SimpleTaskletSource;
import com.cfa.objects.letter.Letter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Function;

@Configuration
public class LetterJobConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    @Autowired
    private Source sources;

    @Bean
    public Job simpleLetterJob() {
        return jobBuilderFactory
                .get("simpleLetterJob")
                .start(step1())
                .next(step2())
                .build();
    }

    @Bean
    public Step step1(){
        return this.stepBuilderFactory
                .get("step1")
                .tasklet(new SimpleTaskletSource(sources))
                .build();
    }

    @Bean
    public Step step2(){
        return this.stepBuilderFactory
                .get("step2")
                .<List<String>, List<Letter>>chunk(10)
                .reader(new SimpleItemReader())
                .processor(new SimpleItemProcessor())
                .writer(new SimpleItemWriter())
                .build();
    }
}
