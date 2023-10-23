package ypkim.sideproject.board.service;

import java.util.List;

import io.micrometer.core.instrument.search.Search;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ypkim.sideproject.board.domain.type.SearchType;
import ypkim.sideproject.board.dto.ArticleDto;
import ypkim.sideproject.board.repository.ArticleRepository;

import org.springframework.data.domain.Page;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("비즈니스로직 게시판")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

	@InjectMocks
	private ArticleService service;

	@Mock
	private ArticleRepository repository;

	@DisplayName("게시글을 검색하면, 게시글 리스트를 반환한다.")
	@Test
	void givenSearchinParameters_whenSearchingArticle_thenReturnArticleLists() {

		// Given
//		SearchParams params = SearchingParams.of(SearchType.TITLE, "searc");

		// When
		Page<ArticleDto> articles = service.searchArticles(SearchType.TITLE , "search keyword");

		// Then
		Assertions.assertThat(articles).isNotNull();
	}

	@DisplayName("게시글을 조회하면, 게시글을 반환한다.")
	@Test
	void givenId_whenSearchingArticle_thenReturnArticle() {

		// Given

		// When
		ArticleDto article = service.searchArticles(1L);

		// Then
		Assertions.assertThat(article).isNotNull();
	}

}