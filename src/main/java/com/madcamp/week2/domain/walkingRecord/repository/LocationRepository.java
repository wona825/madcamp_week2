package com.madcamp.week2.domain.walkingRecord.repository;

import com.madcamp.week2.domain.walkingRecord.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {

}
