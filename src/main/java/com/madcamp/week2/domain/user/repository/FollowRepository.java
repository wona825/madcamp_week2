package com.madcamp.week2.domain.user.repository;

import com.madcamp.week2.domain.user.entity.Follow;
import com.madcamp.week2.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByFollowingAndFollowedId(User following, Long followedId);

    @Query("SELECT f FROM Follow f JOIN FETCH f.followed LEFT JOIN FETCH f.followed.walkingRecords WHERE f.following = :following")
    List<Follow> findByFollowingWithFollowed(@Param("following") User following);
}
