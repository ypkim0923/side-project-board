package ypkim.sideproject.board.domain;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@Table(indexes = {
		@Index(columnList = "title"),
		@Index(columnList = "hashtag"),
		@Index(columnList = "createdAt"),
		@Index(columnList = "createdBy")
})
@Entity
public class Article extends AuditingFields {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Setter
	@ManyToOne(optional = false)
	private UserAccount userAccount;


	@Setter
	@Column(nullable = false)
	private String title;

	@Setter
	@Column(nullable = false, length = 10000)
	private String content;

	@Setter
	private String hashtag;

	@OrderBy("createdAt DESC ")
	@OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
	@ToString.Exclude
	private Set<ArticleComment> articleComments = new LinkedHashSet<>();

	// 기본 생성자가 있어야함
	protected Article() {
	}

	public Article(UserAccount userAccount, String title, String content, String hashtag) {
		this.userAccount = userAccount;
		this.title = title;
		this.content = content;
		this.hashtag = hashtag;
	}

	public static Article of(UserAccount userAccount, String title, String content, String hashtag) {
		return new Article(userAccount, title, content, hashtag);
	}

	public void setArticleComments(Set<ArticleComment> articleComments) {
		this.articleComments = articleComments;
	}

	// 동등성 검사
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Article article = (Article) o;
		return id != null && id.equals(article.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
