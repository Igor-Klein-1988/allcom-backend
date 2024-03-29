package de.allcom.services;

import de.allcom.dto.category.CategoryByLanguageDto;
import de.allcom.dto.category.CategoryDto;
import de.allcom.exceptions.RestException;
import de.allcom.models.Category;
import de.allcom.repositories.CategoryRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryDto> findCategoriesWithAllNames() {
        return categoryRepository.findAll().stream().map(c -> CategoryDto.builder()
                .id(c.getId())
                .nameRu(c.getNameRu())
                .nameDe(c.getNameDe())
                .nameEn(c.getNameEn())
                .parentId(c.getParentId())
                .build()).toList();
    }

    public List<CategoryByLanguageDto> findCategoriesByLanguage(String language) {
        return switch (language) {
            case "ru" -> categoryRepository.findAll().stream().map(c -> CategoryByLanguageDto.builder()
                            .id(c.getId())
                            .name(c.getNameRu())
                            .parentId(c.getParentId())
                            .build())
                    .toList();
            case "de" -> categoryRepository.findAll().stream().map(c -> CategoryByLanguageDto.builder()
                            .id(c.getId())
                            .name(c.getNameDe())
                            .parentId(c.getParentId())
                            .build())
                    .toList();
            case "en" -> categoryRepository.findAll().stream().map(c -> CategoryByLanguageDto.builder()
                            .id(c.getId())
                            .name(c.getNameEn())
                            .parentId(c.getParentId())
                            .build())
                    .toList();
            default -> throw new RestException(HttpStatus.BAD_REQUEST, "Unexpected value: " + language);
        };
    }

    public CategoryDto findCategoryById(Long id) {
        return categoryRepository.findById(id).map(c -> CategoryDto.builder()
                        .id(c.getId())
                        .nameRu(c.getNameRu())
                        .nameDe(c.getNameDe())
                        .nameEn(c.getNameEn())
                        .parentId(c.getParentId())
                        .build())
                .stream().findFirst()
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Unexpected id: " + id));
    }

    public CategoryByLanguageDto findCategoryByLanguage(Long id, String language) {
        return switch (language) {
            case "ru" -> categoryRepository.findById(id).map(c -> CategoryByLanguageDto.builder()
                            .id(c.getId())
                            .name(c.getNameRu())
                            .parentId(c.getParentId())
                            .build())
                    .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND,
                            "Unexpected id: " + id));
            case "de" -> categoryRepository.findById(id)
                    .map(c -> CategoryByLanguageDto.builder()
                            .id(c.getId())
                            .name(c.getNameDe())
                            .parentId(c.getParentId())
                            .build())
                    .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND,
                            "Unexpected id: " + id));
            case "en" -> categoryRepository.findById(id)
                    .map(c -> CategoryByLanguageDto.builder()
                            .id(c.getId())
                            .name(c.getNameEn())
                            .parentId(c.getParentId())
                            .build())
                    .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND,
                            "Unexpected id: " + id));
            default -> throw new RestException(HttpStatus.BAD_REQUEST, "Unexpected language");
        };
    }

    public List<CategoryDto> findCategoriesByParentId(Long parentId) {
        return categoryRepository.findAllByParentId(parentId).stream()
                .map(c -> CategoryDto.builder()
                        .id(c.getId())
                        .nameRu(c.getNameRu())
                        .nameDe(c.getNameDe())
                        .nameEn(c.getNameEn())
                        .parentId(c.getParentId())
                        .build()).toList();
    }

    public Set<Long> findAllSubcategoryIdsWithCurrentCategoryId(Long categoryId) {
        List<Category> allCategories = categoryRepository.findAll();

        Map<Long, Set<Long>> categoryToSubcategories = allCategories.stream()
                                                                    .collect(
                                                                            Collectors.groupingBy(Category::getParentId,
                                                                                    Collectors.mapping(Category::getId,
                                                                                            Collectors.toSet())));

        Set<Long> result = new HashSet<>();
        result.add(categoryId);

        findAllSubcategoryIds(categoryId, categoryToSubcategories, result);

        return result;
    }

    private void findAllSubcategoryIds(Long currentCategoryId, Map<Long, Set<Long>> categoryToSubcategories,
            Set<Long> result) {
        Set<Long> subcategories = categoryToSubcategories.get(currentCategoryId);
        if (subcategories == null) {
            return;
        }
        for (Long subcategoryId : subcategories) {
            if (result.add(subcategoryId)) {
                findAllSubcategoryIds(subcategoryId, categoryToSubcategories, result);
            }
        }
    }
}
