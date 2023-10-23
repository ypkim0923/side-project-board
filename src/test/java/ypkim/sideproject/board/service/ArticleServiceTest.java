package ypkim.sideproject.board.service;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ypkim.sideproject.board.domain.Article;
import ypkim.sideproject.board.domain.type.SearchType;
import ypkim.sideproject.board.dto.ArticleDto;
import ypkim.sideproject.board.dto.ArticleUpdateDto;
import ypkim.sideproject.board.repository.ArticleRepository;

import org.springframework.data.domain.Page;

import static org.mockito.ArgumentMatchers.any;

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

		// When
		Page<ArticleDto> articles = service.searchArticles(SearchType.TITLE, "search keyword");

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

	@DisplayName("게시글 정보를 입력하면, 게시글을 생성한다.")
	@Test
	void givenArticleInfo_whenSavingArticle_thenArticle() {
		// Given
		ArticleDto dto = ArticleDto.of(LocalDateTime.now(), "YPKIM", "title", "content", "#java");

		// any()는 아무 Mockup class를 넣어주는것
		// BDDMockito.given은 명시적으로 무슨일이 일어나는지를 알려주는 Mocking함수
		// repository에서는 정상적으로 동작하는지 확인
		BDDMockito.given(repository.save(any(Article.class))).willReturn(null);

		// When
		service.saveArticle(dto);

		// Then
		BDDMockito.then(repository).should().save(any(Article.class)); // 실제로 service.saveArticle에서 save를 호출했는지 검사

		// Repository의 기능을 검사하는 것은 온전한 유닛테스트가(Sociable Test) 아니기 때문에 Service코드만 검사하고 싶은 것 (Solitary Test)
	}

	@DisplayName("게시글의 ID와 수정 정보를 입력 하면, 게시글을 수정한다.")
	@Test
	void givenArticleInfoAndModifiedInfo_whenUpdatingArticle_thenUpdatesArticle() {
		// Given
		BDDMockito.given(repository.save(any(Article.class))).willReturn(null);

		// When
		service.updateArticle(1L, ArticleUpdateDto.of("updatedtitle", "content", "#java"));

		// Then
		BDDMockito.then(repository).should().save(any(Article.class));
	}

	@DisplayName("게시글의 ID와 수정 정보를 입력 하면, 게시글을 수정한다.")
	@Test
	void givenArticleId_whenDeletingArticle_thenDeletesArticle() {
		// Given
		BDDMockito.willDoNothing().given(repository).delete(any(Article.class));

		// When
		service.deleteArticle(1L);

		// Then
		BDDMockito.then(repository).should().save(any(Article.class));
	}

}