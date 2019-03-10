package com.bacefook.serviceTests;

import javax.management.relation.RelationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.bacefook.exception.ElementNotFoundException;
import com.bacefook.repository.RelationsRepository;
import com.bacefook.service.RelationService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

	@Autowired
	private RelationService relationService;
	@MockBean
	private RelationsRepository relationRepo;
	
	@Test(expected = RelationException.class)
	public void sendFriendRequestTest() throws RelationException, ElementNotFoundException {
		
		Integer sender = 1;
		Integer receiver = sender;
		
		relationService.sendFriendRequest(sender, receiver);
	}
	
	// TODO mock repositories to give predefined collections or users, 
	// then test the DTO conversions
	
}
