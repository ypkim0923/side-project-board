package ypkim.sideproject.board.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import ypkim.sideproject.board.config.JpaConfig;
import ypkim.sideproject.board.domain.Article;
import ypkim.sideproject.board.repository.ArticleRepository;
import ypkim.sideproject.board.repository.ArticleCommentRepository;

import java.util.List;

//@ActiveProfiles("testdb") // Test 전용 DB
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {

    private ArticleRepository articleRepository;
    private ArticleCommentRepository articleCommentRepository;

    public JpaRepositoryTest(@Autowired ArticleRepository articleRepository, @Autowired ArticleCommentRepository articleCommentRepository) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
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
        Article article = Article.of("new article","new content","#spring");

        Article savedArticle = articleRepository.save(article);

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

        Assertions.assertThat(articleRepository.count()).isEqualTo(previousArticleCount -1);
    }
}