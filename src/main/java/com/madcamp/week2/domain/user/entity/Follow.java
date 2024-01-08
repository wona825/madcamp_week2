package com.madcamp.week2.domain.user.entity;

import com.madcamp.week2.global.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
public class Follow extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "followed_id")
    @NotNull
    private User followed;

    @ManyToOne
    @JoinColumn(name = "following_id")
    @NotNull
    private User following;
}
