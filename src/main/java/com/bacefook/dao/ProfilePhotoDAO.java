package com.bacefook.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
@Component
public class ProfilePhotoDAO {
	private final String GET_PROFILE_PHOTO_URL_BY_USER = "SELECT p.url AS url FROM additional_users_info info "
			+ "LEFT JOIN photos p ON(info.profile_photo_id=p.id) WHERE info.id = ?;";
	@Autowired
	private JdbcTemplate jdbcTemplate;
	public List<String> findProfilePhotoUrl(Integer userId) {
		return jdbcTemplate.query(GET_PROFILE_PHOTO_URL_BY_USER, ps -> {
			ps.setInt(1, userId);
		}, (resultSet, rowNum) -> resultSet.getString("url"));
	}
}
