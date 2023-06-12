package com.lucaslearning.wsreader.writers;

import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lucaslearning.wsreader.domain.User;

@Configuration
public class UserWriterConfig {

	@Bean
	public ItemWriter<User> userWriter() {
		return items -> items.forEach(System.out::println);
	}
	
}
