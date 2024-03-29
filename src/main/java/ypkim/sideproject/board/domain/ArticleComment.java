package ypkim.sideproject.board.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@Table(indexes = {
		@Index(columnList = "content"),
		@Index(columnList = "createdAt"),
		@Index(columnList = "createdBy")
})
@Entity
public class ArticleComment extends AuditingFields {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Setter
	@ManyToOne(optional = false)
	private Article article;

	@Setter
	@ManyToOne(optional = false)
	private UserAccount userAccount;

	@Setter
	@Column(nullable = false, length = 255)
	private String content;

	protected ArticleComment() {
	}

	public ArticleComment(Article article, UserAccount userAccount, String content) {
		this.article = article;
		this.userAccount = userAccount;
		this.content = content;
	}

	public static ArticleComment of(Article article, UserAccount userAccount, String content) {
		return new ArticleComment(article, userAccount, content);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ArticleComment that = (ArticleComment) o;
		return id != null && id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
