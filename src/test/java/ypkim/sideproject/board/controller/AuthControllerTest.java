package ypkim.sideproject.board.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ypkim.sideproject.board.config.SecurityConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("View 컨트롤러 인증 부")
@Import(SecurityConfig.class)
@WebMvcTest
public class AuthControllerTest {

	private final MockMvc mvc;

	public AuthControllerTest(@Autowired MockMvc mvc) {
		this.mvc = mvc;
	}

	@DisplayName("[view][GET] 로그인 페이지 - 정상 호출")
	@Test
	public void givenNoting_whenTryingToLogIn_thenReturnsArticlesView() throws Exception {
		// given

		// when & then
		mvc.perform(get("/login"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
	}
}
