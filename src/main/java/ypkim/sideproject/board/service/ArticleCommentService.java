package ypkim.sideproject.board.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import ypkim.sideproject.board.dto.ArticleCommentDto;
import ypkim.sideproject.board.repository.ArticleCommentRepository;
import ypkim.sideproject.board.repository.ArticleRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleCommentService {

	private final ArticleCommentRepository articleCommentRepository;

	private final ArticleRepository articleRepository;

	@Transactional(readOnly = true)
	public List<ArticleCommentDto> searchArticleComment() {
		return List.of();
	}

	public void saveArticleComment(ArticleCommentDto dto) {
	}
}
