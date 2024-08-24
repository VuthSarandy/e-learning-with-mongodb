package co.istad.elearning.features.course.dto;

import co.istad.elearning.domain.Section;

import java.time.LocalDateTime;
import java.util.List;

public record CourseDetailResponse(

        String id,

        String title,

        String slug,

        String description,

        String thumbnail,

        Double price,

        Double  discount,

        String code,

        Boolean isPaid,

        Boolean isDraft,

        String instructorName,

        LocalDateTime createAt,

        LocalDateTime updatedAt,

        String categoryName,

        List<Section> sections

) {
}
