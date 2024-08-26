package co.istad.elearning.features.course.dto;

import co.istad.elearning.domain.Video;

import java.util.List;

public record SectionCreateRequest(

        String title,

        Integer orderNo,

        List<Video> videos

) {
}
