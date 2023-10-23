package ypkim.sideproject.board.dto;

import java.time.LocalDateTime;

/**
 * DTO for {@link ypkim.sideproject.board.domain.ArticleComment}
 */
public record ArticleCommentDto(LocalDateTime createdAt,
								String createdBy,
								String content) {
	public static ArticleCommentDto of(LocalDateTime createdAt, String createdBy, String content) {
		return new ArticleCommentDto(createdAt, createdBy, content);
	}
}