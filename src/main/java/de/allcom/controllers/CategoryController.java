package de.allcom.controllers;

import de.allcom.controllers.api.CategoryApi;
import de.allcom.dto.category.CategoryByLanguageDto;
import de.allcom.dto.category.CategoryDto;
import de.allcom.services.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CategoryController implements CategoryApi {

    private final CategoryService categoryService;

    @Override
    public List<CategoryDto> findCategoriesWithAllNames() {
        return categoryService.findCategoriesWithAllNames();
    }

    @Override
    public List<CategoryByLanguageDto> findCategoriesByLanguage(String language) {
        return categoryService.findCategoriesByLanguage(language);
    }

    @Override
    public CategoryDto findCategoryById(Long id) {
        return categoryService.findCategoryById(id);
    }

    @Override
    public CategoryByLanguageDto findCategoryByLanguage(Long id, String language) {
        return categoryService.findCategoryByLanguage(id, language);
    }

    @Override
    public List<CategoryDto> findCategoriesByParentId(Long parentId) {
        return categoryService.findCategoriesByParentId(parentId);
    }
}
