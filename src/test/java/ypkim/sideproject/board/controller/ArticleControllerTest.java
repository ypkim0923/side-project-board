package ypkim.sideproject.board.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ypkim.sideproject.board.config.SecurityConfig;
import ypkim.sideproject.board.domain.type.SearchType;
import ypkim.sideproject.board.dto.ArticleWithCommentsDto;
import ypkim.sideproject.board.dto.UserAccountDto;
import ypkim.sideproject.board.service.ArticleService;
import ypkim.sideproject.board.service.PaginationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
	PaginationService paginationService;

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
		given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(0, 1, 2, 3, 4));

		// when
		mvc.perform(get("/articles"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
				.andExpect(view().name("articles/index"))
				.andExpect(model().attributeExists("articles"))
				.andExpect(model().attributeExists("paginationBarNumbers"));

		// then
		then(service).should().searchArticles(eq(null), eq(null), any(Pageable.class));
		then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());
	}

	@DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 검색어와 함께 호출")
	@Test
	public void givenSearchKeword_whenSearchingArticlesView_thenReturnArticlesView() throws Exception {

		// given
		SearchType searchType = SearchType.TITLE;
		String searchValue = "title";
		given(service.searchArticles(eq(searchType), eq(searchValue), any(Pageable.class))).willReturn(Page.empty());
		given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(0, 1, 2, 3, 4));


		// when
		mvc.perform(
						get("/articles")
								.queryParam("searchType", searchType.name())
								.queryParam("searchValue", searchValue)
				)
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
				.andExpect(view().name("articles/index"))
				.andExpect(model().attributeExists("articles"))
				.andExpect(model().attributeExists("searchTypes"));

		// then
		then(service).should().searchArticles(eq(searchType), eq(searchValue), any(Pageable.class));
		then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());
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

	@DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 페이징, 정렬 기능")
	@Test
	void givenPagingAndSortingParams_whenSearchingArticlesPage_thenReturnsArticlesPage() throws Exception {
		// Given
		String sortName = "title";
		String direction = "desc";
		int pageNumber = 0;
		int pageSize = 5;
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Order.desc(sortName)));
		List<Integer> barNumbers = List.of(1, 2, 3, 4, 5);
		given(service.searchArticles(null, null, pageable)).willReturn(Page.empty());
		given(paginationService.getPaginationBarNumbers(pageable.getPageNumber(), Page.empty().getTotalPages())).willReturn(barNumbers);

		// When & Then
		mvc.perform(
						get("/articles")
								.queryParam("page", String.valueOf(pageNumber))
								.queryParam("size", String.valueOf(pageSize))
								.queryParam("sort", sortName + "," + direction)
				)
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
				.andExpect(view().name("articles/index"))
				.andExpect(model().attributeExists("articles"))
				.andExpect(model().attribute("paginationBarNumbers", barNumbers));
		then(service).should().searchArticles(null, null, pageable);
		then(paginationService).should().getPaginationBarNumbers(pageable.getPageNumber(), Page.empty().getTotalPages());
	}


	@Disabled("개발 중")
	@DisplayName("[view][GET] 게시글 검색 전용 페이지 - 정상 호출")
	@Test
	public void givenNoting_whenRequestingArticlesSearchView_thenReturnsArticlesView() throws Exception {
		// given

		// when
		mvc.perform(get("/articles/search-hashtag"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
				.andExpect(view().name("articles/search"));
	}


	@DisplayName("[view][GET] 게시글 hashTag 검색 페이지 - 정상 호출")
	@Test
	public void givenNoting_whenRequestingArticlesSearchHashtagView_thenReturnsArticlesSearchView() throws Exception {
		// given
		List<String> hashtags = List.of("#java", "#spring", "#boot");
		given(service.searchArticlesViaHashtag(eq(null), any(Pageable.class))).willReturn(Page.empty());
		given(service.getHashtags()).willReturn(hashtags);
		given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(1, 2, 3, 4, 5));


		// when
		mvc.perform(get("/articles/search-hashtag"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
				.andExpect(view().name("articles/search-hashtag"))
				.andExpect(model().attribute("articles", Page.empty()))
				.andExpect(model().attributeExists("hashtags"))
				.andExpect(model().attributeExists("paginationBarNumbers"))
				.andExpect(model().attribute("searchType", SearchType.HASHTAG));

		// then
		then(service).should().searchArticlesViaHashtag(eq(null), any(Pageable.class));
		then(service).should().getHashtags();
		then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());
	}

	@DisplayName("[view][GET] 게시글 hashTag 검색 페이지 - 정상 호출 해시태그 입력")
	@Test
	public void givenHashtag_whenRequestingArticlesSearchHashtagView_thenReturnsArticlesSearchView() throws Exception {
		// given
		String hashtag = "#java";
		List<String> hashtags = List.of("#java", "#spring", "#boot");
		given(service.searchArticlesViaHashtag(eq(hashtag), any(Pageable.class))).willReturn(Page.empty());
		given(service.getHashtags()).willReturn(hashtags);
		given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(1, 2, 3, 4, 5));


		// when
		mvc.perform(get("/articles/search-hashtag").queryParam("searchValue", hashtag))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
				.andExpect(view().name("articles/search-hashtag"))
				.andExpect(model().attribute("articles", Page.empty()))
				.andExpect(model().attributeExists("hashtags"))
				.andExpect(model().attributeExists("paginationBarNumbers"));

		// then
		then(service).should().searchArticlesViaHashtag(eq(hashtag), any(Pageable.class));
		then(service).should().getHashtags();
		then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());

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