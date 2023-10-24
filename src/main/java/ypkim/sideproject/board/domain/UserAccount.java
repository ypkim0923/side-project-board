package ypkim.sideproject.board.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Entity
@Table(indexes = {
		@Index(columnList = "userId"),
		@Index(columnList = "email", unique = true),
		@Index(columnList = "createdAt"),
		@Index(columnList = "createdBy")
})
public class UserAccount extends AuditingFields {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Setter
	@Column(nullable = false, length = 50)
	private String userId;

	@Setter
	@Column(nullable = false)
	private String userPassword;

	@Setter
	@Column(length = 100)
	private String email;

	@Setter
	@Column(length = 100)
	private String nickname;

	@Setter
	private String memo;

	protected UserAccount() {
	}

	private UserAccount(String userId, String userPassword, String email, String nickname, String memo) {
		this.userId = userId;
		this.userPassword = userPassword;
		this.email = email;
		this.nickname = nickname;
		this.memo = memo;
	}

	public static UserAccount of(String userId, String userPassword, String email, String nickname, String memo) {
		return new UserAccount(userId, userPassword, email, nickname, memo);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		UserAccount that = (UserAccount) o;

		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
}
