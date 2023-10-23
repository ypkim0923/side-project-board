package ypkim.sideproject.board.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ypkim.sideproject.board.domain.Article;
import ypkim.sideproject.board.domain.ArticleComment;
import ypkim.sideproject.board.dto.ArticleCommentDto;
import ypkim.sideproject.board.repository.ArticleCommentRepository;
import ypkim.sideproject.board.repository.ArticleRepository;

@DisplayName("비즈니스로직 - 댓글")
@ExtendWith(MockitoExtension.class)
class ArticleCommentServiceTest {
	@InjectMocks
	private ArticleCommentService service;

	@Mock
	private ArticleCommentRepository articleCommentRepository;

	@Mock
	private ArticleRepository articleRepository;


	@DisplayName("게시글ID로 조회하면, 댓글 리스트를 반환한다.")
	@Test
	void givenArticleId_whenSearchingArticleComments_thenReturnArticleComments() {

		// Given
		Long articleId = 1L;
		Optional<Article> article = Optional.of(Article.of("title", "contents", "#java"));
		BDDMockito.given(articleRepository.findById(articleId)).willReturn(article);

		// When
		List<ArticleCommentDto> articleComments = service.searchArticleComment();

		// Then
		Assertions.assertThat(articleComments).isNotNull();
		BDDMockito.then(articleRepository).should().findById(articleId);
	}

	@DisplayName("댓글 정보를 입력하면, 댓글을 생성한다.")
	@Test
	void givenArticleCommentInfo_whenSavingArticleComment_thenSaveArticleComments() {

		// Given
		ArticleCommentDto dto = ArticleCommentDto.of(LocalDateTime.now(), "ypkim", "comments");
		BDDMockito.given(articleCommentRepository.save(ArgumentMatchers.any(ArticleComment.class))).willReturn(null);

		// When
		service.saveArticleComment(dto);

		// Then
		BDDMockito.then(articleCommentRepository).should().save(ArgumentMatchers.any(ArticleComment.class));
	}

}