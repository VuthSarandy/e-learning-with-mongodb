package co.istad.elearning.features.course;

import co.istad.elearning.domain.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CourseRepository extends MongoRepository<Course,String> {

    Page<Course> findAllByIsDeletedIsFalseAndIsDraftIsFalse(PageRequest pageRequest);

}
