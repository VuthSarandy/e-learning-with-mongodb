package co.istad.elearning.features.category;

import co.istad.elearning.domain.Category;
import co.istad.elearning.features.category.dto.CategoryCreateRequest;
import co.istad.elearning.features.category.dto.CategoryResponse;
import co.istad.elearning.features.category.dto.CategoryUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    void createCategory(@Valid @RequestBody CategoryCreateRequest categoryCreateRequest){
        categoryService.create(categoryCreateRequest);
    }

    @DeleteMapping("/{id}")
    void deleteCategory(@PathVariable String id){
        categoryService.deleteCategory(id);
    }

    @GetMapping
    List<CategoryResponse> getAllCategories(){
        return categoryService.getAllCategories();
    }

    @PutMapping("/{id}/disable")
    void disableCategory(@PathVariable String id){
        categoryService.disableCategory(id);
    }

    @PutMapping("/{id}/enable")
    void enableCategory(@PathVariable String id){
        categoryService.enableCategory(id);
    }

    @GetMapping("/{id}")
    CategoryResponse getCategoryById(@PathVariable String id){
        return categoryService.getCategoryById(id);
    }

    @PutMapping("/{id}")
    void updateCategory(@PathVariable String id, @Valid @RequestBody CategoryUpdateRequest categoryUpdateRequest){
        categoryService.updateCategory(id, categoryUpdateRequest);
    }


}
