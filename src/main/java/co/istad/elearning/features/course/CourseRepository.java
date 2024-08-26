package co.istad.elearning.features.course;

import co.istad.elearning.domain.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends MongoRepository<Course,String> {

    Page<Course> findAllByIsDeletedIsFalseAndIsDraftIsFalse(PageRequest pageRequest);

    Optional<Course> findBySlugAndIsDeletedIsFalseAndIsDraftIsFalse(String slug);

    Page<Course> findByIsDeletedIsFalseAndIsDraftIsTrue(PageRequest pageRequest);

    @Query("{'price': 0}")
    Page<Course> findAllCourseIsFree(PageRequest pageRequest);

    List<Course> findAllByInstructorNameAndIsDeletedIsFalse(String instructorName);

    List<Course> findAllByCategoryNameAndIsDeletedIsFalse(String categoryName);

}
