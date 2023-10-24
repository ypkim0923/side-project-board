package ypkim.sideproject.board.repository;

import ypkim.sideproject.board.domain.UserAccount;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
}
