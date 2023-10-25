package ypkim.sideproject.board.service;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ypkim.sideproject.board.domain.Article;
import ypkim.sideproject.board.domain.UserAccount;
import ypkim.sideproject.board.domain.type.SearchType;
import ypkim.sideproject.board.dto.ArticleDto;
import ypkim.sideproject.board.dto.ArticleWithCommentsDto;
import ypkim.sideproject.board.dto.UserAccountDto;
import ypkim.sideproject.board.repository.ArticleRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

@DisplayName("비즈니스로직 게시판")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

	@InjectMocks
	private ArticleService service;

	@Mock
	private ArticleRepository articleRepository;

	@DisplayName("검색어 없이 게시글을 검색하면, 게시글 페이지를 반환한다.")
	@Test
	void givenNoSearchParameters_whenSearchingArticles_thenReturnsArticlePage() {
		// Given
		Pageable pageable = Pageable.ofSize(20);
		given(articleRepository.findAll(pageable)).willReturn(Page.empty());

		// When
		Page<ArticleDto> articles = service.searchArticles(null, null, pageable);

		// Then
		assertThat(articles).isEmpty();
		then(articleRepository).should().findAll(pageable);
	}


	@DisplayName("검색어와 함께 게시글을 검색하면, 게시글 페이지를 반환한다.")
	@Test
	void givenSearchParameters_whenSearchingArticles_thenReturnsArticlePage() {
		// Given
		SearchType searchType = SearchType.TITLE;
		String searchKeyword = "title";
		Pageable pageable = Pageable.ofSize(20);
		given(articleRepository.findByTitle(searchKeyword, pageable)).willReturn(Page.empty());

		// When
		Page<ArticleDto> articles = service.searchArticles(searchType, searchKeyword, pageable);

		// Then
		assertThat(articles).isEmpty();
		then(articleRepository).should().findByTitle(searchKeyword, pageable);
	}

	@DisplayName("게시글을 조회하면, 게시글을 반환한다.")
	@Test
	void givenId_whenSearchingArticle_thenReturnArticle() {

		// Given
		Long articleId = 1L;
		Article article = createArticle();
		given(articleRepository.findById(articleId)).willReturn(Optional.of(article));

		// When
		ArticleWithCommentsDto dto = service.getArticle(articleId);

		// Then
		assertThat(dto)
				.hasFieldOrPropertyWithValue("title", article.getTitle())
				.hasFieldOrPropertyWithValue("content", article.getContent())
				.hasFieldOrPropertyWithValue("hashtag", article.getHashtag());
		then(articleRepository).should().findById(articleId);
	}

	@DisplayName("없는 게시글을 조회하면, 예외를 던진다.")
	@Test
	void givenNonexistentArticleId_whenSearchingArticle_thenThrowsException() {
		// Given
		Long articleId = 0L;
		given(articleRepository.findById(articleId)).willReturn(Optional.empty());

		// When
		Throwable t = catchThrowable(() -> service.getArticle(articleId));

		// Then
		assertThat(t)
				.isInstanceOf(EntityNotFoundException.class)
				.hasMessage("게시글이 없습니다 - articleId: " + articleId);
		then(articleRepository).should().findById(articleId);
	}

	@DisplayName("게시글 정보를 입력하면, 게시글을 생성한다.")
	@Test
	void givenArticleInfo_whenSavingArticle_thenArticle() {
		// Given
		// any()는 아무 Mockup class를 넣어주는것
		// BDDMockito.given은 명시적으로 무슨일이 일어나는지를 알려주는 Mocking함수
		// repository에서는 정상적으로 동작하는지 확인
		ArticleDto dto = createArticleDto();
		given(articleRepository.save(any(Article.class))).willReturn(createArticle());


		// When
		service.saveArticle(dto);

		// Then
		then(articleRepository).should().save(any(Article.class)); // 실제로 service.saveArticle에서 save를 호출했는지 검사

		// Repository의 기능을 검사하는 것은 온전한 유닛테스트가(Sociable Test) 아니기 때문에 Service코드만 검사하고 싶은 것 (Solitary Test)
	}

	@DisplayName("게시글의 수정 정보를 입력하면, 게시글을 수정한다.")
	@Test
	void givenArticleInfoAndModifiedInfo_whenUpdatingArticle_thenUpdatesArticle() {
		// Given
		Article article = createArticle();
		ArticleDto dto = createArticleDto("새 타이틀", "새 내용", "#springboot");
		given(articleRepository.getReferenceById(dto.id())).willReturn(article);

		// When
		service.updateArticle(dto);

		// Then
		assertThat(article)
				.hasFieldOrPropertyWithValue("title", dto.title())
				.hasFieldOrPropertyWithValue("content", dto.content())
				.hasFieldOrPropertyWithValue("hashtag", dto.hashtag());
		then(articleRepository).should().getReferenceById(dto.id());
	}

	@DisplayName("없는 게시글의 수정 정보를 입력하면, 경고 로그를 찍고 아무 것도 하지 않는다.")
	@Test
	void givenNonexistentArticleInfo_whenUpdatingArticle_thenLogsWarningAndDoesNothing() {
		// Given
		ArticleDto dto = createArticleDto("새 타이틀", "새 내용", "#springboot");
		given(articleRepository.getReferenceById(dto.id())).willThrow(EntityNotFoundException.class);

		// When
		service.updateArticle(dto);

		// Then
		then(articleRepository).should().getReferenceById(dto.id());
	}

	@DisplayName("게시글의 ID를 입력하면, 게시글을 삭제한다")
	@Test
	void givenArticleId_whenDeletingArticle_thenDeletesArticle() {
		// Given
		Long articleId = 1L;
		willDoNothing().given(articleRepository).deleteById(articleId);

		// When
		service.deleteArticle(1L);

		// Then
		then(articleRepository).should().deleteById(articleId);
	}

	private UserAccount createUserAccount() {
		return UserAccount.of(
				"ypkim",
				"password",
				"ypkim@gmail.com",
				"ypkim",
				null
		);
	}

	private Article createArticle() {
		return Article.of(
				createUserAccount(),
				"title",
				"content",
				"#java"
		);
	}

	private ArticleDto createArticleDto() {
		return createArticleDto("title", "content", "#java");
	}

	private ArticleDto createArticleDto(String title, String content, String hashtag) {
		return ArticleDto.of(1L,
				createUserAccountDto(),
				title,
				content,
				hashtag,
				LocalDateTime.now(),
				"ypkim",
				LocalDateTime.now(),
				"ypkim");
	}

	private UserAccountDto createUserAccountDto() {
		return UserAccountDto.of(
				1L,
				"ypkiim",
				"password",
				"ypkim@gmail.com",
				"Uno",
				"This is memo",
				LocalDateTime.now(),
				"ypkim",
				LocalDateTime.now(),
				"ypkim"
		);
	}

}