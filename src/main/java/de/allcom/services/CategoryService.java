package de.allcom.services;

import de.allcom.dto.category.CategoryDto;
import de.allcom.dto.category.CategoryLanguageDto;
import de.allcom.exceptions.RestException;
import de.allcom.repositories.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryDto> findAllCategoriesWithAllNames() {
        return categoryRepository.findAll().stream().map(c -> CategoryDto.builder()
                .id(c.getId())
                .nameRu(c.getNameRu())
                .nameDe(c.getNameDe())
                .nameEn(c.getNameEn())
                .parentId(c.getParentId())
                .build()).toList();
    }

    public List<CategoryLanguageDto> findAllCategoryWithLanguage(String language) {
        return switch (language) {
            case "ru" -> categoryRepository.findAll().stream().map(c -> CategoryLanguageDto.builder()
                    .id(c.getId())
                    .name(c.getNameRu())
                    .parentId(c.getParentId())
                    .build()).toList();
            case "de" -> categoryRepository.findAll().stream().map(c -> CategoryLanguageDto.builder()
                    .id(c.getId())
                    .name(c.getNameDe())
                    .parentId(c.getParentId())
                    .build()).toList();
            case "en" -> categoryRepository.findAll().stream().map(c -> CategoryLanguageDto.builder()
                            .id(c.getId())
                            .name(c.getNameEn())
                            .parentId(c.getParentId())
                            .build())
                    .toList();
            default -> throw new RestException(HttpStatus.BAD_REQUEST, "Unexpected value: " + language);
        };
    }

    public CategoryDto findOneCategoryWithAllField(Long id) {
        return categoryRepository.findById(id).map(c -> CategoryDto.builder()
                        .id(c.getId())
                        .nameRu(c.getNameRu())
                        .nameDe(c.getNameDe())
                        .nameEn(c.getNameEn())
                        .parentId(c.getParentId())
                        .build())
                .stream().findFirst()
                .orElseThrow(() -> new RestException(HttpStatus.BAD_REQUEST, "Unexpected id: " + id));
    }

    public CategoryLanguageDto findOneCategoryWithLanguage(Long id, String language) {
        return switch (language) {
            case "ru" -> categoryRepository.findById(id).map(c -> CategoryLanguageDto.builder()
                            .id(c.getId())
                            .name(c.getNameRu())
                            .parentId(c.getParentId())
                            .build())
                    .orElseThrow(() -> new RestException(HttpStatus.BAD_REQUEST,
                            "Unexpected id: " + id + " or language"));
            case "de" -> categoryRepository.findById(id)
                    .map(c -> CategoryLanguageDto.builder()
                            .id(c.getId())
                            .name(c.getNameDe())
                            .parentId(c.getParentId())
                            .build())
                    .orElseThrow(() -> new RestException(HttpStatus.BAD_REQUEST,
                            "Unexpected id: " + id + " or language"));
            case "en" -> categoryRepository.findById(id)
                    .map(c -> CategoryLanguageDto.builder()
                            .id(c.getId())
                            .name(c.getNameEn())
                            .parentId(c.getParentId())
                            .build())
                    .orElseThrow(() -> new RestException(HttpStatus.BAD_REQUEST,
                            "Unexpected id: " + id + " or language"));
            default -> throw new RestException(HttpStatus.BAD_REQUEST, "Unexpected id: " + id + " or language");
        };
    }
}
