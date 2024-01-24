package de.allcom.controllers;

import de.allcom.controllers.api.CategoryApi;
import de.allcom.dto.category.CategoryDto;
import de.allcom.dto.category.CategoryLanguageDto;
import de.allcom.services.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CategoryController implements CategoryApi {

    private final CategoryService categoryService;

    @Override
    public List<CategoryDto> findAllCategoriesWithAllNames() {
        return categoryService.findAllCategoriesWithAllNames();
    }

    @Override
    public List<CategoryLanguageDto> findAllCategoryWithLanguage(String language) {
        return categoryService.findAllCategoryWithLanguage(language);
    }

    @Override
    public CategoryDto findOneCategoryWithAllField(Long id) {
        return categoryService.findOneCategoryWithAllField(id);
    }

    @Override
    public CategoryLanguageDto findOneCategoryWithLanguage(Long id, String language) {
        return categoryService.findOneCategoryWithLanguage(id, language);
    }
}
