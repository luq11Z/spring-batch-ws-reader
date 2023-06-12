package com.lucaslearning.wsreader.steps;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.lucaslearning.wsreader.domain.User;

@Configuration
public class ServiceReaderStepConfig {
	
	@Value("${chunkSize}")
	private Integer chunkSize;

	@Bean
	public Step serviceReaderStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
			ItemReader<User> reader, ItemWriter<User> writer) {
		return new StepBuilder("serviceReaderStep", jobRepository)
				.<User, User>chunk(chunkSize, transactionManager)
				.reader(reader)
				.writer(writer)
				.build();
	}
	
}
