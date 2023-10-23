package ypkim.sideproject.board.controller;

import org.junit.jupiter.api.Test;
import ypkim.sideproject.board.config.SecurityConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SecurityConfig.class)
@WebMvcTest(MainController.class)
class MainControllerTest {

	private final MockMvc mvc;

	public MainControllerTest(@Autowired MockMvc mvc) {
		this.mvc = mvc;
	}

	@Test
	void givenNoting_whenRequestRootPage_thenRedirectsToArticlesPage() throws Exception {
		// given
		// when & then
		mvc.perform(get("/"))
				.andExpect(status().is3xxRedirection());
	}
}