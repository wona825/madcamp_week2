package com.madcamp.week2.domain.user.repository;

import com.madcamp.week2.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.profileImg WHERE u.id = :userId")
    Optional<User> findWithProfileImgById(@Param("userId") Long userId);

    @Query("SELECT u FROM User u lEFT JOIN FETCH u.profileImg WHERE (:search IS NULL OR u.email LIKE %:search% OR u.nickname LIKE %:search%)")
    List<User> findBySearch(@Param("search") String search);
}
