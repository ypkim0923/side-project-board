package ypkim.sideproject.board.repository.querydsl;

import java.util.List;

import ypkim.sideproject.board.domain.Article;
import ypkim.sideproject.board.domain.QArticle;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class ArticleRepositoryCustomImpl extends QuerydslRepositorySupport implements ArticleRepositoryCustom {

	public ArticleRepositoryCustomImpl() {
		super(Article.class);
	}

	@Override
	public List<String> findAllDistinctHashTags() {
		QArticle article = QArticle.article;

		return from(article)
				.distinct()
				.select(article.hashtag)
				.where(article.hashtag.isNull())
				.fetch();
	}
}
