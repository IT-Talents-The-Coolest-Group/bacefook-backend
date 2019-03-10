package com.bacefook.serviceTests;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.bacefook.dao.PostDAO;
import com.bacefook.dto.PostContentDTO;
import com.bacefook.dto.PostDTO;
import com.bacefook.exception.ElementNotFoundException;
import com.bacefook.model.Post;
import com.bacefook.model.User;
import com.bacefook.repository.PostsRepository;
import com.bacefook.repository.UsersRepository;
import com.bacefook.service.PostService;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
		PostService.class,
		PostsRepository.class, 
		UsersRepository.class, 
		PostDAO.class})
public class PostsTest {

	@Autowired
	private PostService postService;
	@MockBean
	private PostsRepository postsRepo;
	@MockBean
	private PostDAO postDao;
	@MockBean
	private UsersRepository  usersRepo;
	
	@Test(expected = ElementNotFoundException.class)
	public void nonexistingPosterConversionTest() throws ElementNotFoundException {
		Integer poster = 1;

		List<Post> posts = new LinkedList<Post>();
		posts.add(new Post(poster, "Content!", LocalDateTime.now()));
		
		Mockito.when(usersRepo.findById(poster)).thenReturn(Optional.empty());
		
		postService.postsConverter(posts, poster);
	}
	
	@Test
	public void correctConversionTest() throws ElementNotFoundException {
		
		User poster = new User();
		poster.setFirstName("firstname");
		poster.setLastName("lastname");
		poster.setId(1);

		List<Post> posts = new LinkedList<Post>();
		Post post = new Post(poster.getId(), "Content!", LocalDateTime.now());
		posts.add(post);

		List<PostDTO> expected = new LinkedList<PostDTO>();
		PostDTO postDto = new PostDTO();
		postDto.setPosterFullName(poster.getFullName());
		postDto.setContent(post.getContent());
		postDto.setPostingTime(post.getPostingTime());
		expected.add(postDto);
		
		Mockito.when(usersRepo.findById(poster.getId())).thenReturn(Optional.of(poster));
		
		List<PostDTO> actual = postService.postsConverter(posts, poster.getId());
		
		assertThat(actual).containsExactlyElementsOf(expected);
	}
	
	@Test(expected = ElementNotFoundException.class)
	public void findNonexistingPost() throws ElementNotFoundException {
		Integer poster = 1;
		Mockito.when(postsRepo.findById(poster)).thenReturn(Optional.empty());
		
		postService.findById(poster);
	}
	
	@Test(expected = ElementNotFoundException.class)
	public void shareNonexistingPost() throws ElementNotFoundException {
		Integer poster = 1;
		
		Mockito.when(postsRepo.findById(poster)).thenReturn(Optional.empty());
		
		postService.saveSharing(123, poster, new PostContentDTO("Content!"));
	}
	
	
}
