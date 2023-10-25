package ypkim.sideproject.board.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Disabled("Spring Data REST 통합테스트는 불필요함으로 Disabled")
@DisplayName("Data Rest - API Test")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
//@WebMvcTest WebMvcTest는 필요한 애만 데려옴
public class DataRestTest {

	private final MockMvc mvc;

	public DataRestTest(@Autowired MockMvc mvc) {
		this.mvc = mvc;
	}

	@DisplayName("[api]/api/articles")
	@Test
	void givenNothing_whenRequestingArticles_thenReturnsArticlesJsonResponse() throws Exception {

		mvc.perform(MockMvcRequestBuilders.get("/api/articles"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.valueOf("application/hal+json")))
				.andDo(print());
	}

	@DisplayName("[api]/api/articles/1")
	@Test
	void givenNothing_whenRequestingArticles1_thenReturnsArticlesJsonResponse() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/api/articles/1"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.valueOf("application/hal+json")))
				.andDo(print());
	}
}
