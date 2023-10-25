package ypkim.sideproject.board.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ypkim.sideproject.board.domain.Article;
import ypkim.sideproject.board.domain.ArticleComment;
import ypkim.sideproject.board.domain.UserAccount;
import ypkim.sideproject.board.dto.ArticleCommentDto;
import ypkim.sideproject.board.dto.UserAccountDto;
import ypkim.sideproject.board.repository.ArticleCommentRepository;
import ypkim.sideproject.board.repository.ArticleRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

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
		ArticleComment expected = createArticleComment("content");
		given(articleCommentRepository.findByArticle_Id(articleId)).willReturn(List.of(expected));

		// When
		List<ArticleCommentDto> actual = service.searchArticleComments(articleId);

		// Then
		assertThat(actual)
				.hasSize(1)
				.first().hasFieldOrPropertyWithValue("content", expected.getContent());
		then(articleCommentRepository).should().findByArticle_Id(articleId);
	}

	@DisplayName("댓글 정보를 입력하면, 댓글을 생성한다.")
	@Test
	void givenArticleCommentInfo_whenSavingArticleComment_thenSaveArticleComments() {

		// Given
		ArticleCommentDto dto = createArticleCommentDto("댓글");
		given(articleRepository.getReferenceById(dto.articleId())).willReturn(createArticle());

		// When
		service.saveArticleComment(dto);

		// Then
		then(articleRepository).should().getReferenceById(dto.articleId());
		then(articleCommentRepository).should().save(ArgumentMatchers.any(ArticleComment.class));
	}

	@DisplayName("댓글 저장을 시도했는데 맞는 게시글이 없으면, 경고 로그를 찍고 아무것도 안 한다.")
	@Test
	void givenNonexistentArticle_whenSavingArticleComment_thenLogsSituationAndDoesNothing() {
		// Given
		ArticleCommentDto dto = createArticleCommentDto("댓글");
		given(articleRepository.getReferenceById(dto.articleId())).willThrow(EntityNotFoundException.class);

		// When
		service.saveArticleComment(dto);

		// Then
		then(articleRepository).should().getReferenceById(dto.articleId());
		then(articleCommentRepository).shouldHaveNoInteractions();
	}

	@DisplayName("댓글 정보를 입력하면, 댓글을 수정한다.")
	@Test
	void givenArticleCommentInfo_whenUpdatingArticleComment_thenUpdatesArticleComment() {
		// Given
		String oldContent = "content";
		String updatedContent = "댓글";
		ArticleComment articleComment = createArticleComment(oldContent);
		ArticleCommentDto dto = createArticleCommentDto(updatedContent);
		given(articleCommentRepository.getReferenceById(dto.id())).willReturn(articleComment);

		// When
		service.updateArticleComment(dto);

		// Then
		assertThat(articleComment.getContent())
				.isNotEqualTo(oldContent)
				.isEqualTo(updatedContent);
		then(articleCommentRepository).should().getReferenceById(dto.id());
	}

	@DisplayName("없는 댓글 정보를 수정하려고 하면, 경고 로그를 찍고 아무 것도 안 한다.")
	@Test
	void givenNonexistentArticleComment_whenUpdatingArticleComment_thenLogsWarningAndDoesNothing() {
		// Given
		ArticleCommentDto dto = createArticleCommentDto("댓글");
		given(articleCommentRepository.getReferenceById(dto.id())).willThrow(EntityNotFoundException.class);

		// When
		service.updateArticleComment(dto);

		// Then
		then(articleCommentRepository).should().getReferenceById(dto.id());
	}

	@DisplayName("댓글 ID를 입력하면, 댓글을 삭제한다.")
	@Test
	void givenArticleCommentId_whenDeletingArticleComment_thenDeletesArticleComment() {
		// Given
		Long articleCommentId = 1L;
		willDoNothing().given(articleCommentRepository).deleteById(articleCommentId);

		// When
		service.deleteArticleComment(articleCommentId);

		// Then
		then(articleCommentRepository).should().deleteById(articleCommentId);
	}


	// Test Data를 java에서 Fixture라고 부름
	private ArticleCommentDto createArticleCommentDto(String content) {
		return ArticleCommentDto.of(
				1L,
				1L,
				createUserAccountDto(),
				content,
				LocalDateTime.now(),
				"uno",
				LocalDateTime.now(),
				"uno"
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

	private ArticleComment createArticleComment(String content) {
		return ArticleComment.of(
				Article.of(createUserAccount(), "title", "content", "hashtag"),
				createUserAccount(),
				content
		);
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

}