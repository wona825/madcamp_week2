package com.madcamp.week2.domain.user.repository;

import com.madcamp.week2.domain.user.entity.Follow;
import com.madcamp.week2.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowingAndFollowed(User following, User followed);

    // following을 기준으로 follow 엔티티를 찾으면서 동시에 followed를 join으로 같이 조회하는 쿼리
    @Query("SELECT f FROM Follow f JOIN FETCH f.followed WHERE f.following = :following")
    List<Follow> findByFollowingWithFollowed(@Param("following") User following);
}
