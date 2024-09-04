package co.istad.elearning;

import co.istad.elearning.domain.Course;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;


@SpringBootTest
class ELearningApplicationTests {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void testFilterCourses() {

        Query query = new Query();

        Criteria criteria = Criteria
                .where("title")
                .regex("Cyber","i");

        Criteria criteria1 = Criteria
                .where("price")
                .gte(0);

        Criteria criteria2 = new  Criteria();
        criteria2.orOperator(criteria,criteria1);
        query.addCriteria(criteria2);

        List<Course> courses = mongoTemplate.find(query, Course.class);

        courses.forEach(System.out::println);
    }

}
