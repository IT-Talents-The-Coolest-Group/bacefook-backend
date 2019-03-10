package com.bacefook.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.management.relation.RelationException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bacefook.dao.RelationsDAO;
import com.bacefook.dao.UserDAO;
import com.bacefook.dto.UserSummaryDTO;
import com.bacefook.exception.ElementNotFoundException;
import com.bacefook.model.Relation;
import com.bacefook.model.User;
import com.bacefook.repository.RelationsRepository;
import com.bacefook.repository.UsersRepository;

@Service
public class RelationService {
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private UsersRepository usersRepo;
	@Autowired
	private RelationsRepository relationsRepo;
	@Autowired
	private RelationsDAO relationsDAO;
	@Autowired
	private UserService userService;

	private ModelMapper mapper = new ModelMapper();

	public String removeFromFriends(Integer loggedId, Integer friendId) {
		int rows = relationsDAO.removeFromFriends(loggedId, friendId);
		if (rows > 0) {
			return "User id no longer friends with " + friendId;
		} else {
			return "User is not friends with " + friendId;
		}
	}

	public Integer sendFriendRequest(Integer senderId, Integer receiverId)
			throws RelationException, ElementNotFoundException {

		Relation friendRequest = new Relation(senderId, receiverId, 0);
		if (senderId.equals(receiverId)) {
			throw new RelationException("You cannot send a request to yourself!");
		}
		if (!usersRepo.existsById(senderId) || !usersRepo.existsById(receiverId)) {
			throw new ElementNotFoundException("A user with that ID does not exist!");
		}
		if (relationsRepo.findBySenderIdAndReceiverId(senderId, receiverId) != null) {
			throw new RelationException("You have already sent a request to that person!");
		}
		if (userDAO.findAllFriendsOf(senderId).contains(receiverId)) {
			throw new RelationException("You are already friends!");
		}
		return relationsRepo.save(friendRequest).getId();
	}

	public Integer confirmFriendRequest(Integer receiverId, Integer senderId) throws RelationException {
		Relation relation = relationsRepo.findBySenderIdAndReceiverId(senderId, receiverId);
		if (relation == null) {
			throw new RelationException("You do not have a request from that user!");
		}
		if (relation.getIsConfirmed().equals(1)) {
			throw new RelationException("You are already friends!");
		}
		relation.setIsConfirmed(1);
		return relationsRepo.save(relation).getId();
	}

	/**
	 * find all friend requests to user
	 **/
	public List<UserSummaryDTO> findAllFromRequestsTo(Integer userId) {
		List<Integer> userIds = userDAO.findAllRequestsTo(userId);
		List<UserSummaryDTO> users = new LinkedList<UserSummaryDTO>();
		for (Integer id : userIds) {
			Optional<User> user = usersRepo.findById(id);
			if (user.isPresent()) {
				UserSummaryDTO summary = new UserSummaryDTO();
				mapper.map(user.get(), summary);
				summary.setFriendsCount(getFriendsCountOF(user.get().getId()));
				users.add(summary);
			}
		}
		return users;
	}

	/**
	 * find all friends by user
	 * 
	 * @throws ElementNotFoundException
	 **/
	public List<UserSummaryDTO> findAllFriendOf(Integer userId) {
		List<Integer> friendsIds = userDAO.findAllFriendsOf(userId);
		List<UserSummaryDTO> users = new LinkedList<UserSummaryDTO>();
		for (Integer id : friendsIds) {
			Optional<User> user = usersRepo.findById(id);
			if (user.isPresent()) {
				UserSummaryDTO summary = new UserSummaryDTO();
				mapper.map(user.get(), summary);
				summary.setFriendsCount(getFriendsCountOF(user.get().getId()));
				String url = null;
				try {
					url = userService.findProfilePhotoUrl(userId);
				} catch (ElementNotFoundException e) {
					System.out.println(userId + " has no profile picture");
				}
				summary.setProfilePhotoUrl(url);
				users.add(summary);
			}
		}
		return users;
	}

	public int getFriendsCountOF(Integer userId) {
		return userDAO.findAllFriendsOf(userId).size();
	}

	public String cancelFriendRequest(Integer loggedId, Integer receiverId) throws RelationException {
		int rows = relationsDAO.cancelFriendRequest(loggedId, receiverId);
		if (rows > 0) {
			return "You canceled a friend request to " + receiverId;
		} else {
			throw new RelationException("Sorry, but you have not sent a friend request to this user.");
		}
	}

	public String deleteFriendRequest(Integer loggedId, Integer senderId) throws RelationException {
		int rows = relationsDAO.cancelFriendRequest(loggedId, senderId);
		if (rows > 0) {
			return "You deleted a friend request from " + senderId;
		} else {
			throw new RelationException("Sorry, but you have not received a friend request from this user.");
		}
	}
}
