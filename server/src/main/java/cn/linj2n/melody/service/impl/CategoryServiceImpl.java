package cn.linj2n.melody.service.impl;

import cn.linj2n.melody.domain.Category;
import cn.linj2n.melody.repository.CategoryRepository;
import cn.linj2n.melody.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category createCategory(Category newCategory) {
        return categoryRepository.save(newCategory);
    }

    @Override
    public void removeCategoryByName(String categoryName) {

    }

    @Override
    public Optional<Category> getCategoryByName(String categoryName) {
        return Optional.empty();
    }

    @Override
    public Optional<Category> getCategoryById(Long categoryId) {
        return categoryRepository.findOptionalById(categoryId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Category> getCategoryWithPosts(String categoryName) {
        logger.info("hit getCategoryWithPosts");
        return categoryRepository.findOptionalByName(categoryName)
                .map(u -> {
                    u.getPosts().size();
                    return u;
                });
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Category> getCategoryWithPosts(Long categoryId) {
        logger.info("hit getCategoryWithPosts");
        return categoryRepository.findOptionalById(categoryId)
                .map(u -> {
                    u.getPosts().size();
                    return u;
                });
    }

    @Override
    public List<Category> listAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> listAllCategoriesWithPosts() {
        return categoryRepository.findAllByOrderByNameAsc()
                .stream()
                .peek(e -> {
                    e.getPosts().size();
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void removeCategoryById(Long categoryId) {
        logger.info("hit removeCategoryById");
        getCategoryWithPosts(categoryId).map(category -> {
            category.getPosts().forEach(e -> e.getCategories().remove(category));
            categoryRepository.delete(category);
            return category;
        });
    }

    @Override
    public Category updateCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public boolean existsById(Long categoryId) {
        return categoryRepository.exists(categoryId);
    }
}
