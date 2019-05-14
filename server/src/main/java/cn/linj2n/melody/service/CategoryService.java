package cn.linj2n.melody.service;

import cn.linj2n.melody.domain.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    Category createCategory(Category newCategory);

    void removeCategoryByName(String categoryName);

    void removeCategoryById(Long categoryId);

    Optional<Category> getCategoryByName(String categoryName);

    Optional<Category> getCategoryById(Long categoryId);

    Category updateCategory(Category category);

    Optional<Category> getCategoryWithPosts(String categoryName);

    Optional<Category> getCategoryWithPosts(Long categoryId);

    List<Category> listAllCategories();

    List<Category> listAllCategoriesWithPosts();

    boolean existsById(Long categoryId);

}
