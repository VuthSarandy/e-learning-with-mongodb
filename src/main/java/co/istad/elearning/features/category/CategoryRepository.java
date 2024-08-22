package co.istad.elearning.features.category;

import co.istad.elearning.domain.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends MongoRepository<Category, String> {

    Boolean existsByName(String name);

    @Query("{'isDeleted' : false }")
    List<Category> findAllNotDeleted();

    Optional<Category> findByIdAndIsDeletedFalse(String id);
}
