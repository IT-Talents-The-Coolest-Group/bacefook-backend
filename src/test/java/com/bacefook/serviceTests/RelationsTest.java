package com.bacefook.serviceTests;

import javax.management.relation.RelationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.bacefook.dao.RelationsDAO;
import com.bacefook.dao.UserDAO;
import com.bacefook.exception.ElementNotFoundException;
import com.bacefook.model.Relation;
import com.bacefook.repository.RelationsRepository;
import com.bacefook.repository.UsersRepository;
import com.bacefook.service.RelationService;
import com.bacefook.service.UserService;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
		RelationService.class, 
		RelationsRepository.class, 
		UserDAO.class, 
		UsersRepository.class, 
		RelationsDAO.class,
		UserService.class})
public class RelationsTest {

	@Autowired
	private RelationService relationService;
	@MockBean
	private RelationsRepository relationRepo;
	@MockBean 
	private UserDAO userDao;
	@MockBean
	private UsersRepository usersRepo;
	@MockBean
	private RelationsDAO relationsDao;
	@MockBean
	private UserService userService;

	@Test(expected = RelationException.class)
	public void sendRequestToYourselfTest() throws RelationException, ElementNotFoundException {
		
		Integer sender = 1;
		Integer receiver = sender;
		
		relationService.sendFriendRequest(sender, receiver);
	}
	
	@Test(expected = ElementNotFoundException.class)
	public void sendRequestToNoOneTest() throws RelationException, ElementNotFoundException {
		
		Integer sender = 1;
		Integer receiver = 10;
		Mockito.when(relationRepo.existsById(receiver)).thenReturn(false);
		
		relationService.sendFriendRequest(sender, receiver);
	}
	
	@Test(expected = RelationException.class)
	public void confirmNonexistingRequestTest() throws RelationException, ElementNotFoundException {
		
		Integer sender = 1;
		Integer receiver = 10;
		Mockito.when(relationRepo.findBySenderIdAndReceiverId(sender, receiver))
		.thenReturn(null);
		
		relationService.confirmFriendRequest(sender, receiver);
	}

	@Test(expected = RelationException.class)
	public void confirmConfirmedRequestTest() throws RelationException, ElementNotFoundException {
		
		Integer sender = 1;
		Integer receiver = 10;
		Mockito.when(relationRepo.findBySenderIdAndReceiverId(sender, receiver))
		.thenReturn(new Relation(sender, receiver, 1));
		
		relationService.confirmFriendRequest(sender, receiver);
	}
	
	@Test(expected = RelationException.class)
	public void confirmExistingFriendshipTest() throws RelationException, ElementNotFoundException {
		
		Integer sender = 1;
		Integer receiver = 10;
		Mockito.when(relationRepo.findBySenderIdAndReceiverId(sender, receiver))
		.thenReturn(new Relation(sender, receiver, 1));
		
		relationService.confirmFriendRequest(sender, receiver);
	}
	
	
	
}
