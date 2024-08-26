package co.istad.elearning.features.course.dto;

import co.istad.elearning.domain.Video;

import java.util.List;

public record VideoUpdateRequest(

        Integer sectionOrderNo,
        List<Video> videos

) {
}
