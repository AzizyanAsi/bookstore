package net.bookstore.repository;

import net.bookstore.entity.SystemUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SystemUserRepository extends JpaRepository<SystemUser, Long> {
    @EntityGraph(attributePaths = { "role" })
    Optional<SystemUser> findByEmailIgnoreCase(String email);

    @EntityGraph(attributePaths = { "role" })
    Optional<SystemUser> findByPhoneNumber(String phoneNumber);

}
