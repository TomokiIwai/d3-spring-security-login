package com.example.tomokiiwai;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * サーブレットコンテナの挙動カスタマイザー
 *
 * @author tomoki.iwai
 */
@Component
public class D3ServletContainerCustomizer implements EmbeddedServletContainerCustomizer {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/404"));
	}
}
