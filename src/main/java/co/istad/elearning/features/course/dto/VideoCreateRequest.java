package co.istad.elearning.features.course.dto;

public record VideoCreateRequest(

        Integer orderNo,

        Integer sectionOrderNo,

        String lectureNo,

        String title,

        String fileName

) {
}
