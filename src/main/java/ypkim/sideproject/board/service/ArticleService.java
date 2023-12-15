package ypkim.sideproject.board.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ypkim.sideproject.board.domain.Article;
import ypkim.sideproject.board.domain.type.SearchType;
import ypkim.sideproject.board.dto.ArticleDto;
import ypkim.sideproject.board.dto.ArticleWithCommentsDto;
import ypkim.sideproject.board.repository.ArticleRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class ArticleService {
	private final ArticleRepository repository;

	@Transactional(readOnly = true)
	public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {
		if (searchKeyword == null || searchKeyword.isBlank()) {
			return repository.findAll(pageable).map(ArticleDto::from);
		}

		return switch (searchType) {
			case TITLE ->
					repository.findByTitleContaining(searchKeyword, pageable).map(ArticleDto::from);
			case CONTENT ->
					repository.findByContentContaining(searchKeyword, pageable).map(ArticleDto::from);
			case NICKNAME ->
					repository.findByUserAccount_NicknameContaining(searchKeyword, pageable).map(ArticleDto::from);
			case HASHTAG ->
					repository.findByHashtag("#" + searchKeyword, pageable).map(ArticleDto::from);
			case ID ->
					repository.findByUserAccount_UserIdContaining(searchKeyword, pageable).map(ArticleDto::from);

		};

	}

	public ArticleWithCommentsDto getArticle(Long articleId) {

		return repository.findById(articleId)
				.map(ArticleWithCommentsDto::from)
				.orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다. " + articleId));
	}

	public void saveArticle(ArticleDto dto) {
		repository.save(dto.toEntity());
	}

	public void updateArticle(ArticleDto dto) {
		try {
			Article article = repository.getReferenceById(dto.id());
			if (dto.title() != null) {
				article.setTitle(dto.title());
			}
			if (dto.content() != null) {
				article.setContent(dto.content());
			}
			article.setHashtag(dto.hashtag());
		}
		catch (EntityNotFoundException e) {
			log.warn("게시글 업데이트 실패. 게시글을 찾을 수 없습니다 DTO : {}", dto);
		}
	}

	public void deleteArticle(long l) {
		repository.deleteById(l);
	}

	@Transactional(readOnly = true)
	public Page<ArticleDto> searchArticlesViaHashtag(String hashtag, Pageable pageable) {
		if (hashtag == null || hashtag.isBlank()) {
			return Page.empty(pageable);
		}

		return repository.findByHashtag(hashtag, pageable).map(ArticleDto::from);
	}

	public List<String> getHashtags() {
		return repository.findAllDistinctHashTags();
	}
}
