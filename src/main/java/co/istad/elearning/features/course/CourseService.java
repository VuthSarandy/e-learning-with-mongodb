package co.istad.elearning.features.course;

import co.istad.elearning.features.course.dto.CourseCreateRequest;
import co.istad.elearning.features.course.dto.CourseSnippetResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CourseService {

    void createCourse(CourseCreateRequest courseCreateRequest);

    Page<?> findAllCourse(int page,int size,String response);

    ResponseEntity<?> findByCourseId(String id,String response);

    void deleteCourseById(String id);

    void updateVisibility(String id,Boolean visibility);

    void disableCourse(String id);
    void enableCourse(String id);

}
