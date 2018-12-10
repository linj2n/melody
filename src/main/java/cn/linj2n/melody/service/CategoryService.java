package cn.linj2n.melody.service;

import cn.linj2n.melody.domain.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    Category createCategory(String categoryName);

    void removeCategoryByName(String categoryName);

    Optional<Category> getCategoryByName(String categoryName);

    Optional<Category> getCategoryById(Long categoryId);

    Optional<Category> getCategoryWithPosts(String categoryName);

    List<Category> listAllCategories();

    List<Category> listAllCategoriesWithPosts();

}
