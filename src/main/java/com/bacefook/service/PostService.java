package com.bacefook.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bacefook.dao.PostDAO;
import com.bacefook.dto.PostContentDTO;
import com.bacefook.dto.PostDTO;
import com.bacefook.exception.ElementNotFoundException;
import com.bacefook.model.Post;
import com.bacefook.model.User;
import com.bacefook.repository.PostsRepository;
import com.bacefook.repository.UsersRepository;

@Service
public class PostService {

	@Autowired
	private PostsRepository postsRepo;
	@Autowired
	private PostDAO postDao;
	@Autowired
	private UsersRepository usersRepo;
	ModelMapper mapper = new ModelMapper();

	public Integer save(Integer posterId, String content) {
		Post post = new Post(posterId, content, LocalDateTime.now());
		return postsRepo.save(post).getId();
	}

	public Integer update(Post post) {
		return postsRepo.save(post).getId();
	}

	public Integer deletePost(Integer id) {
		return postDao.deletePostById(id);
	}

	public Integer saveSharing(Integer sharesPostId, int posterId, PostContentDTO postContentDto)
			throws ElementNotFoundException {
		if (!existsById(sharesPostId)) {
			throw new ElementNotFoundException("Cannot share a post that does not exist!");
		}
		Post post = new Post(posterId, postContentDto.getContent(), LocalDateTime.now());
		post.setSharesPostId(sharesPostId);
		return postsRepo.save(post).getId();
	}

	public boolean existsById(Integer sharesPostId) {
		return postsRepo.existsById(sharesPostId);
	}

	/**
	 * get all posts by friends ordered
	 **/
	public List<PostDTO> findAllPostsFromFriends(Integer loggerId) {
		List<Integer> postIds = postDao.getAllPostsIdByFriends(loggerId);
		List<PostDTO> posts = new ArrayList<PostDTO>(postIds.size());
		for (Integer id : postIds) {
			Optional<Post> optional = postsRepo.findById(id);
			if (optional.isPresent()) {
				Post post = optional.get();
				PostDTO dto = new PostDTO();
				this.mapper.map(post, dto);
				Optional<User> u = usersRepo.findById(post.getPosterId());
				if (u.isPresent()) {
					User user = u.get();
					dto.setPosterFullName(user.getFullName());
					posts.add(dto);
				}
			}
		}
		return posts;
	}

	public List<PostDTO> findAllByUser(User poster) throws ElementNotFoundException {
		List<Post> posts = postsRepo.findAllByPosterIdOrderByPostingTimeDesc(poster.getId());
		return this.postsConverter(posts, poster.getId());
	}

	public List<PostDTO> findAllByUserId(Integer posterId) throws ElementNotFoundException {
		List<Post> posts = postsRepo.findAllByPosterIdOrderByPostingTimeDesc(posterId);
		return this.postsConverter(posts, posterId);
	}

	public List<PostDTO> postsConverter(List<Post> posts, Integer posterId) throws ElementNotFoundException {
		List<PostDTO> dtos = new LinkedList<>();
		for (Post post : posts) {
			PostDTO dto = new PostDTO();
			this.mapper.map(post, dto);
			Optional<User> optional = usersRepo.findById(posterId);
			if (!optional.isPresent()) {
				throw new ElementNotFoundException("A user with that ID does not exist!");
			}
			User user = optional.get();
			dto.setPosterFullName(user.getFullName());
			dtos.add(dto);
		}
		return dtos;
	}

	public boolean isPostedByUserId(Integer posterId, Post post) {
		List<Post> posts = postsRepo.findAllByPosterIdOrderByPostingTimeDesc(posterId);
		return posts.contains(post);
	}

	public Post findById(Integer postId) throws ElementNotFoundException {
			Optional<Post> post = postsRepo.findById(postId);
			if(post.isPresent()) {
			return post.get();
			}
			throw new ElementNotFoundException("No such post!");
	}

	public List<PostDTO> findAllWhichSharePostId(Integer postId) {
		List<Post> posts = postsRepo.findAllBySharesPostId(postId);
		List<PostDTO> dtos = new LinkedList<>();
		for (Post post : posts) {
			PostDTO dto = new PostDTO();
			this.mapper.map(post, dto);
			Optional<User> optional = usersRepo.findById(post.getPosterId());
			if (optional.isPresent()) {
				String posterFullName = optional.get().getFullName();
				dto.setPosterFullName(posterFullName);
				dtos.add(dto);
			}
		}
		return dtos;
	}
}
