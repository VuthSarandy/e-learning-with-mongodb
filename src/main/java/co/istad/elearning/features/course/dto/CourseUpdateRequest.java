package co.istad.elearning.features.course.dto;

import jakarta.validation.constraints.*;

public record CourseUpdateRequest(

        @NotBlank(message = "Title is required")
        String title,

        @NotBlank(message = "slug is required")
        String slug,

        String description,

        @NotBlank(message = "thumbnail is required")
        String thumbnail,

        @Min(value = 0, message = "Price must be greater than 0")
        @NotNull(message = "Price is required ")
        Double price,

        @Min(value = 0, message = "Double discount must greater than 0")
        @Max(value = 100, message = "Double discount must be less than and equal 100")
        Double discount,

        @NotBlank(message = "Content is required")
        String content,

        @NotBlank(message = "Category is required")
        String categoryName


) {
}
