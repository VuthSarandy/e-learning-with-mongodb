package co.istad.elearning.mapper;

import co.istad.elearning.domain.Course;
import co.istad.elearning.features.course.dto.CourseDetailResponse;
import co.istad.elearning.features.course.dto.CourseSnippetResponse;
import co.istad.elearning.features.course.dto.CourseUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    CourseSnippetResponse toCourseSnippetResponse(Course courses);
    CourseDetailResponse toCourseDetailResponse(Course courses);

    void updateCourseFromRequest(CourseUpdateRequest courseUpdateRequest, @MappingTarget Course course);

}
