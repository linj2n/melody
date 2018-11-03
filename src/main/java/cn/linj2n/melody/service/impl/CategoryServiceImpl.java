package cn.linj2n.melody.service.impl;

import cn.linj2n.melody.domain.Category;
import cn.linj2n.melody.repository.CategoryRepository;
import cn.linj2n.melody.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category createCategory(String categoryName) {
        return null;
    }

    @Override
    public void removeCategoryByName(String categoryName) {

    }

    @Override
    public Optional<Category> getCategoryByName(String categoryName) {
        return Optional.empty();
    }

    @Override
    public Optional<Category> getCategoryWithPosts(String categoryName) {
        return Optional.empty();
    }

    @Override
    public List<Category> listAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> listAllCategoriesWithPosts() {
        return null;
    }
}
