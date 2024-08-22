package co.istad.elearning.features.category;

import co.istad.elearning.domain.Category;
import co.istad.elearning.features.category.dto.CategoryCreateRequest;
import co.istad.elearning.features.category.dto.CategoryResponse;
import co.istad.elearning.features.category.dto.CategoryUpdateRequest;

import java.util.List;

public interface CategoryService {
    void create(CategoryCreateRequest categoryCreateRequest);
    void deleteCategory(String id);

    List<CategoryResponse> getAllCategories();

    void disableCategory(String id);
    void enableCategory(String id);

    CategoryResponse getCategoryById(String id);

    void updateCategory(String id, CategoryUpdateRequest categoryUpdateRequest);


}
