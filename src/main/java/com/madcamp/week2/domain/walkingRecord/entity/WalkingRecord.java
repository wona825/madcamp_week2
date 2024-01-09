package com.madcamp.week2.domain.walkingRecord.entity;

import com.madcamp.week2.domain.user.entity.User;
import com.madcamp.week2.global.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
public class WalkingRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private User user;

    private LocalDateTime walkingStartDateTime;

    private Long totalWalkingTime;

    private Double walkingDistance;

    @OneToMany(mappedBy = "walkingRecord", fetch = FetchType.LAZY)
    private List<Location> locations;
}

