package com.example.user.repository;

import com.example.user.domain.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    User findByEmail(String email);
    User findById(long id);
    @Modifying
    @Transactional
    void deleteById(Long id);

    @Query("select u.id from User u WHERE u.email = :email ")
    Long getUserIdByEmail(@Param("email") String email);

}
