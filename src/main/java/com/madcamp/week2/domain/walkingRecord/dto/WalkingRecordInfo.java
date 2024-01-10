package com.madcamp.week2.domain.walkingRecord.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalkingRecordInfo {

    @JsonProperty("walking_record_id")
    private Long walkingRecordId;

    @JsonProperty("location_list")
    private List<Location> locationList;

    @JsonProperty("walking_start_date_time")
    private LocalDateTime walkingStartDateTime;

    @JsonProperty("total_walking_time")
    private Long totalWalkingTime;

    @JsonProperty("walking_distance")
    private Double walkingDistance;


    @Getter
    @Builder
    public static class Location {
        Double latitude;
        Double longitude;
        Long recordOrder;
    }
}

