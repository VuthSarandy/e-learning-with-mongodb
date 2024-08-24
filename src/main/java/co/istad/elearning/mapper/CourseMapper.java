package co.istad.elearning.mapper;

import co.istad.elearning.domain.Course;
import co.istad.elearning.features.course.dto.CourseDetailResponse;
import co.istad.elearning.features.course.dto.CourseSnippetResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    CourseSnippetResponse toCourseSnippetResponse(Course courses);
    CourseDetailResponse toCourseDetailResponse(Course courses);

}
