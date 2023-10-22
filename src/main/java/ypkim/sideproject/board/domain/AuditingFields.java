package ypkim.sideproject.board.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.ToString;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@ToString
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class AuditingFields {

	// 아래 필드는 자동으로 설정되어야하는 값들이라 Setter를 설정하지 않음
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@CreatedBy
	@Column(nullable = false, updatable = false, length = 100)
	private String createdBy;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@LastModifiedDate
	@Column(nullable = false)
	private LocalDateTime modifiedAt;

	@LastModifiedBy
	@Column(nullable = false, length = 100)
	private String modifiedBy;

}
