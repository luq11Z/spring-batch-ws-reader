package com.lucaslearning.wsreader.readers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.lucaslearning.wsreader.domain.User;
import com.lucaslearning.wsreader.domain.UserResponse;

@Component
public class UserReader implements ItemReader<User> {
	
	private RestTemplate restTemplate = new RestTemplate();
	
	private int total = 0;
	private int page = 1;
	private int userIndex = 0;
	List<User> users = new ArrayList<>();
	
	@Value("${chunkSize}")
	private Integer chunkSize;
	
	@Value("${pageSize}")
	private Integer pageSize;
	
	@Value("${limit}")
	private Integer limit;

	@Override
	public User read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		User user = userIndex < users.size() ? users.get(userIndex) : null;
		
		userIndex++;
		
		return user;
	}

	/**
	 * Fetch users data from API.
	 * @return list of users.
	 */
	private List<User> fetchUserData() {
		ResponseEntity<UserResponse> response = restTemplate.getForEntity(String.format("https://gorest.co.in/public/v1/users?page=%d", page), UserResponse.class);
		
		List<User> userData = response.getBody().getData();
		
		return userData;
	}
	
	/**
	 * Fetch/load users data before reading each chunk.
	 * @param context
	 */
	@BeforeChunk
	private void beforeChunk(ChunkContext context) {
		for (int i = 0; i < chunkSize; i+= pageSize) {
			if (total >= limit) {
				return;
			}
			
			users.addAll(fetchUserData());
			total += pageSize;
			page++;
		}
	}
	
	@AfterChunk
	private void afterChunk(ChunkContext context) {
		System.out.println("Chunk processed");
		userIndex = 0;
		users = new ArrayList<>();
	}
	
}
