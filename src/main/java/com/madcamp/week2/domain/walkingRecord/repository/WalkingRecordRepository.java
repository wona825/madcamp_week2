package com.madcamp.week2.domain.walkingRecord.repository;

import com.madcamp.week2.domain.walkingRecord.entity.WalkingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WalkingRecordRepository extends JpaRepository<WalkingRecord, Long> {

    @Query("SELECT w FROM WalkingRecord w JOIN FETCH w.locations WHERE w.user.id = :userId" )
    List<WalkingRecord> findByUserId(@Param("userId") Long userId);
}
