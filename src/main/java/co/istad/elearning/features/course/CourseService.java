package co.istad.elearning.features.course;

import co.istad.elearning.features.course.dto.*;
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


    void updateThumbnail(String id, CourseUpdateThumbnail courseUpdateThumbnail);

    void updateIsPaid(String id,Boolean isPaid);

    ResponseEntity<?> findCoursePublic(String slug, String response);

    Page<?> findAllCoursePrivate(int page,int size,String response);

    Page<?> findAllCoursePublic(int page,int size,String response);

    Page<?> findAllCourseIsFree(int page,int size,String response);

    void updateCourse(String id, CourseUpdateRequest courseUpdateRequest);

    void createCourseSection(String id, SectionCreateRequest sectionCreateRequest);

    void createVideoInSection(String id, VideoCreateRequest videoCreateRequest);

    void updateVideoInSection(String id, VideoUpdateRequest videoUpdateRequest);

    List<CourseDetailResponse> findAllCourseByInstructorName(String instructorName);

    Page<?> advancedSearchCourse(int page, int size,String filterAnd, String filterOr, String Orders, String response);

    ResponseEntity<?> findCourseBySlug(String slug, String response);

}
