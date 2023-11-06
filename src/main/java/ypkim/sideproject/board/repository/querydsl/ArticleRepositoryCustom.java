package ypkim.sideproject.board.repository.querydsl;

import java.util.List;

public interface ArticleRepositoryCustom {
	List<String> findAllDistinctHashTags();
}
