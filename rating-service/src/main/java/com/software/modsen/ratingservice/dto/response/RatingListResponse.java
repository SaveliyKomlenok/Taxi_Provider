package com.software.modsen.ratingservice.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RatingListResponse {
    private List<RatingResponse> ratingResponseList;
}
