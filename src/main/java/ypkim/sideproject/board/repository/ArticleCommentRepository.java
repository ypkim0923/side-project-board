package ypkim.sideproject.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ypkim.sideproject.board.domain.ArticleComment;

// JpaRepository 를 extends하면 @Repository 붙어있어서 붙일 필요 없음
public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {
}
