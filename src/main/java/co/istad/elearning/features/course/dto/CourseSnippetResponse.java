package co.istad.elearning.features.course.dto;

import java.time.LocalDateTime;

public record CourseSnippetResponse(

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

        String categoryName

) {
}
