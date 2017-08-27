package com.example.tomokiiwai;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.MediaType.*;

/**
 * コントローラー
 *
 * @author tomoki.iwai
 */
@Controller
public class D3Controller {
	/**
	 * トップページ
	 */
	@RequestMapping("/")
	public String index() {
		return "/index.html";
	}

	/**
	 * マイページ
	 */
	@RequestMapping("/my-page")
	@ResponseBody
	public String myPage(@AuthenticationPrincipal final UserDetails userDetail) {
		return userDetail.getUsername() + "さん、こんにちは";
	}

	/**
	 * 404エラーハンドリング（application/jsonの場合）
	 */
	@RequestMapping(value = "/404", consumes = APPLICATION_JSON_VALUE)
	@ResponseBody
	public String notFoundErrorJson(final HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		return "{code: \"not_found\"}";
	}

	/**
	 * 404エラーハンドリング
	 */
	@RequestMapping(value = "/404", consumes = ALL_VALUE)
	@ResponseBody
	public void notFoundErrorOther(final HttpServletResponse response) {
		try {
			// HTTP Statusは404
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			// Content-Type: text/plain
			response.setContentType(TEXT_PLAIN_VALUE);
			// Charset
			response.setCharacterEncoding("utf-8");
			// Body
			response.getWriter().write("お探しのページは見つかりませんでした。");
			response.getWriter().flush();
		} catch (Exception ignore) {
		}
	}
}
