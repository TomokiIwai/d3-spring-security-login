package com.example.tomokiiwai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletResponse;

/**
 * Security Configuration
 *
 * @author tomoki.iwai
 */
@EnableWebSecurity
public class D3SecurityConfig extends WebSecurityConfigurerAdapter {
	/**
	 * 認証エントリポイントの動作を定義
	 */
	private static final AuthenticationEntryPoint LOGIN_REQUIRED = (request, response, authException) -> {
		// HTTP Statusは401
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		// Content-Type: application/json
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		// Body
		response.getWriter().write("{code: \"login_required\"}");
		response.getWriter().flush();
	};

	/**
	 * ログイン成功時の動作を定義
	 */
	private static final AuthenticationSuccessHandler LOGIN_SUCCESS = (request, response, authentication) -> {
		// HTTP Statusは200
		response.setStatus(HttpServletResponse.SC_OK);
		// Content-Type: application/json
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		// Body
		response.getWriter().write("{code: \"login_success\"}");
		response.getWriter().flush();
	};

	/**
	 * ログイン失敗時の動作を定義
	 */
	private static final AuthenticationFailureHandler LOGIN_FAILED = (request, response, authentication) -> {
		// HTTP Statusは401
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		// Content-Type: application/json
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		// Body
		response.getWriter().write("{code: \"login_failed\"}");
		response.getWriter().flush();
	};

	private final D3UserDetailsService service;

	/**
	 * Constructor
	 */
	public D3SecurityConfig(final D3UserDetailsService service) {
		this.service = service;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/**/js/**", "/**/css/**", "/**/images/**", "/**/favicon.ico");
	}

	/**
	 * {@inheritDoc}
	 */
	@Autowired
	void configureAuthenticationManager(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(service);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// 本件とは関係無いのでCSRFトークンチェックを無効化しておく
		http.csrf().disable();

		http.authorizeRequests()
				// ログイン無しでアクセス許可するパス
				.antMatchers("/").permitAll()
				// その他はログインが必要
				.anyRequest().authenticated();

		//
		// フォーム認証を有効化
		//
		// UsernamePasswordAuthenticationFilterを利用するのと同義です。
		//
		// デフォルトのログインページ、ログイン処理のパスは、両方とも/loginであり
		//  GET /loginは、Spring Securityが用意したログインフォームを表示
		//  POST /loginは、ログイン処理を実行
		http.formLogin()
				//
				// ログイン成功のハンドリング方法を制御
				//
				// REST APIを実装する際は、下記のようにログイン成功を示すJSONを返したくなるはず
				.successHandler(LOGIN_SUCCESS)
				//
				// ログイン失敗のハンドリング方法を制御
				//
				// REST APIを実装する際は、下記のようにログイン失敗を示すJSONを返したくなるはず
				.failureHandler(LOGIN_FAILED);

		http.exceptionHandling()
				//
				// 要ログインページアクセスのハンドリング方法を制御
				// デフォルトでは、ログインページへリダイレクトされる
				//
				// REST APIを実装する際などは、下記のようにログインが必要な旨を示すJSONを返したくなるはず
				.authenticationEntryPoint(LOGIN_REQUIRED);
	}
}
