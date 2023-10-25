package ypkim.sideproject.board.service;

import lombok.RequiredArgsConstructor;
import ypkim.sideproject.board.domain.type.SearchType;
import ypkim.sideproject.board.dto.ArticleDto;
import ypkim.sideproject.board.dto.ArticleWithCommentsDto;
import ypkim.sideproject.board.repository.ArticleRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class ArticleService {
	private final ArticleRepository repository;

	@Transactional(readOnly = true)
	public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {

		return Page.empty();
	}

	public ArticleWithCommentsDto getArticle(Long articleId) {

		return null;
	}

	public void saveArticle(ArticleDto dto) {
	}

	public void updateArticle(ArticleDto dto) {
	}

	public void deleteArticle(long l) {
	}
}
