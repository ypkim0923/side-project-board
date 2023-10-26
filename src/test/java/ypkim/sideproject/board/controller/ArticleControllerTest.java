package ypkim.sideproject.board.controller;

import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import ypkim.sideproject.board.config.SecurityConfig;
import ypkim.sideproject.board.dto.ArticleWithCommentsDto;
import ypkim.sideproject.board.dto.UserAccountDto;
import ypkim.sideproject.board.service.ArticleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@DisplayName("View 컨트롤러 - 게시글")
@WebMvcTest
@Import(SecurityConfig.class)
class ArticleControllerTest {
	private final MockMvc mvc;

	@MockBean
	private ArticleService service;

	public ArticleControllerTest(@Autowired MockMvc mvc) {
		this.mvc = mvc;
	}

	//    @Disabled("개발 중")
	@DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 정상 호출")
	@Test
	public void givenNoting_whenRequestingArticlesView_thenReturnsArticlesView() throws Exception {
		// given
		given(service.searchArticles(eq(null), eq(null), any(Pageable.class))).willReturn(Page.empty());

		// when
		mvc.perform(get("/articles"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
				.andExpect(view().name("articles/index"))
				.andExpect(model().attributeExists("articles"));

		// then
		BDDMockito.then(service).should().searchArticles(eq(null), eq(null), any(Pageable.class));
	}

	@DisplayName("[view][GET] 게시글 상세 페이지 - 정상 호출")
	@Test
	public void givenNoting_whenRequestingArticlesDetailView_thenReturnsArticlesView() throws Exception {
		// given
		Long articleId = 1L;
		given(service.getArticle(articleId)).willReturn(createArticleWithCommentsDto());

		// when
		mvc.perform(get("/articles/1"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
				.andExpect(view().name("articles/detail"))
				.andExpect(model().attributeExists("article"))
				.andExpect(model().attributeExists("articleComments"));

		then(service).should().getArticle(articleId);
	}


	@Disabled("개발 중")
	@DisplayName("[view][GET] 게시글 검색 전용 페이지 - 정상 호출")
	@Test
	public void givenNoting_whenRequestingArticlesSearchView_thenReturnsArticlesView() throws Exception {
		// given

		// when
		mvc.perform(get("/articles/1"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
				.andExpect(view().name("articles/search"));
	}


	@Disabled("개발 중")
	@DisplayName("[view][GET] 게시글 hashTag 검색 페이지 - 정상 호출")
	@Test
	public void givenNoting_whenRequestingArticlesHashtagView_thenReturnsArticlesView() throws Exception {
		// given

		// when
		mvc.perform(get("/articles/1"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
				.andExpect(view().name("articles/search-hashtag"));
	}

	private ArticleWithCommentsDto createArticleWithCommentsDto() {
		return ArticleWithCommentsDto.of(
				1L,
				createUserAccountDto(),
				Set.of(),
				"title",
				"content",
				"#java",
				LocalDateTime.now(),
				"ypkim",
				LocalDateTime.now(),
				"ypkim"
		);
	}

	private UserAccountDto createUserAccountDto() {
		return UserAccountDto.of(
				1L,
				"ypkim",
				"password",
				"ypkim@gmail.com",
				"ypkim",
				"This is memo",
				LocalDateTime.now(),
				"ypkim",
				LocalDateTime.now(),
				"ypkim"
		);
	}

}