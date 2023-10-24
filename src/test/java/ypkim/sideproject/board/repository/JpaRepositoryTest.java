package ypkim.sideproject.board.repository;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ypkim.sideproject.board.config.JpaConfig;
import ypkim.sideproject.board.domain.Article;
import ypkim.sideproject.board.domain.UserAccount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

//@ActiveProfiles("testdb") // Test 전용 DB
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {

	private final ArticleRepository articleRepository;

	private final ArticleCommentRepository articleCommentRepository;

	private final UserAccountRepository userAccountRepository;

	public JpaRepositoryTest(@Autowired ArticleRepository articleRepository, @Autowired ArticleCommentRepository articleCommentRepository, @Autowired UserAccountRepository userAccountRepository) {
		this.articleRepository = articleRepository;
		this.articleCommentRepository = articleCommentRepository;
		this.userAccountRepository = userAccountRepository;
	}

	@DisplayName("Select Test")
	@Test
	void givenTestData_whenSelecting_thenWorkFine() {
		List<Article> articles = articleRepository.findAll();
		Assertions.assertThat(articles).isNotNull().hasSize(100);
	}

	@DisplayName("Insert Test")
	@Test
	void givenTestData_whenInserting_thenWorkFine() {

		long previousCount = articleRepository.count();
		UserAccount userAccount = userAccountRepository.save(UserAccount.of("ypkim", "pw", null, null, null));
		Article article = Article.of(userAccount, "new article", "new content", "#spring");

		articleRepository.save(article);

		Assertions.assertThat(articleRepository.count()).isEqualTo(previousCount + 1);
	}

	@DisplayName("Update Test")
	@Test
	void givenTestData_whenUpdating_thenWorkFine() {

		Article article = articleRepository.findById(1L).orElseThrow();
		String updateHashTag = "#springboot";
		article.setHashtag(updateHashTag);

		Article savedArticle = articleRepository.saveAndFlush(article);

		Assertions.assertThat(savedArticle).hasFieldOrPropertyWithValue("hashtag", updateHashTag);
	}

	@DisplayName("Delete Test")
	@Test
	void givenTestData_whenDeleting_thenWorkFine() {

		Article article = articleRepository.findById(1L).orElseThrow();
		long previousArticleCount = articleRepository.count();
		long previousArticleCommentCount = articleCommentRepository.count();
		long deletedCommentsSize = article.getArticleComments().size();

		articleRepository.delete(article);

		Assertions.assertThat(articleRepository.count()).isEqualTo(previousArticleCount - 1);
	}
}