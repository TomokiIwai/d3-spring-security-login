package com.example.tomokiiwai;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;

/**
 * ユーザー情報を取得するサービス
 *
 * @author tomoki.iwai
 */
@Service
public class D3UserDetailsService implements UserDetailsService {
	private final JdbcTemplate jdbcTemplate;

	/**
	 * Constructor
	 */
	public D3UserDetailsService(final JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (StringUtils.isEmpty(username)) {
			throw new UsernameNotFoundException("No username was supplied");
		}

		// データベースから該当ユーザー情報を取得
		final String password = jdbcTemplate.queryForObject("select password from user where name = ?", new Object[]{username}, String.class);
		if (StringUtils.isEmpty(password)) {
			throw new UsernameNotFoundException("No user was found");
		}

		// ユーザー情報を生成
		return new User(username, password, Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
	}
}
