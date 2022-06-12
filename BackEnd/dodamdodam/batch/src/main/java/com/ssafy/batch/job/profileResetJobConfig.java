package com.ssafy.batch.job;

import com.ssafy.core.entity.Profile;
import com.ssafy.core.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class profileResetJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ProfileRepository profileRepository;

    private final int CHUNK_SIZE = 5;

    @Bean
    public Job profileResetJob(Step profileResetStep) {
        return jobBuilderFactory.get("profileResetJob")
                .incrementer(new RunIdIncrementer())
                .start(profileResetStep)
                .build();
    }

    @Bean
    @JobScope
    public Step profileResetStep(ItemReader<Profile> missionResetReader,
                                 ItemProcessor<Profile, Profile> missionResetProcessor,
                                 ItemWriter<Profile> missionResetWriter
    ) {
        return stepBuilderFactory.get("profileResetStep")
                .<Profile, Profile>chunk(CHUNK_SIZE)
                .reader(missionResetReader)
                .processor(missionResetProcessor)
                .writer(missionResetWriter)
                .build();
    }

    @Bean
    @StepScope
    public RepositoryItemReader<Profile> missionResetReader() {
        return new RepositoryItemReaderBuilder<Profile>()
                .name("missionResetReader")
                .repository(profileRepository)
                .methodName("findBy")
                .pageSize(CHUNK_SIZE)
                .arguments(List.of())
                .sorts(Collections.singletonMap("id", Sort.Direction.DESC))
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<Profile, Profile> missionResetProcessor() {
        return profile -> {
            profile.updateMissionContent(null);
            profile.updateMissionTarget(null);
            return profile;
        };
    }

    @Bean
    @StepScope
    public ItemWriter<Profile> missionResetWriter() {
        return profiles -> {
            profiles.forEach(profileRepository::save);
        };
    }
}
