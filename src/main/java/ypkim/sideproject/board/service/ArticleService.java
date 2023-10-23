package ypkim.sideproject.board.service;

import lombok.RequiredArgsConstructor;
import ypkim.sideproject.board.domain.type.SearchType;
import ypkim.sideproject.board.dto.ArticleDto;
import ypkim.sideproject.board.dto.ArticleUpdateDto;
import ypkim.sideproject.board.repository.ArticleRepository;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class ArticleService {
	private final ArticleRepository repository;

	@Transactional(readOnly = true)
	public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword) {

		return Page.empty();
	}

	public ArticleDto searchArticles(long l) {

		return null;
	}

	public void saveArticle(ArticleDto dto) {
	}

	public void updateArticle(long l, ArticleUpdateDto of) {
	}

	public void deleteArticle(long l) {
	}
}
