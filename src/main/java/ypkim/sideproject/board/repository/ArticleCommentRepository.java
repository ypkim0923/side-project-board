package ypkim.sideproject.board.repository;

import java.util.List;

import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import ypkim.sideproject.board.domain.ArticleComment;
import ypkim.sideproject.board.domain.QArticleComment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


// JpaRepository 를 extends하면 @Repository 붙어있어서 붙일 필요 없음
@RepositoryRestResource
public interface ArticleCommentRepository extends
		JpaRepository<ArticleComment, Long>,
		QuerydslPredicateExecutor<ArticleComment>,
		QuerydslBinderCustomizer<QArticleComment> {

	List<ArticleComment> findByArticle_Id(Long articleId);

	@Override
	default void customize(QuerydslBindings bindings, QArticleComment root) {
		bindings.excludeUnlistedProperties(true);
		bindings.including(root.createdAt, root.createdBy, root.content);
		bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
		bindings.bind(root.createdAt).first(DateTimeExpression::eq);
		bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
	}
}
