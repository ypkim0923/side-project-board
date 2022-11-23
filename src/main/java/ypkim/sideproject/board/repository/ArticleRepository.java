package ypkim.sideproject.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ypkim.sideproject.board.domain.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}