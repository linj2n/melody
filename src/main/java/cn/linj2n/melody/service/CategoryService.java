package cn.linj2n.melody.service;

import cn.linj2n.melody.domain.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    /**
     * 创建类别
     * @param categoryName 类别名称
     * @return 创建后的类别
     */
    Category createCategory(String categoryName);

    /**
     * 删除类别
     * @param categoryName 类别名称
     */
    void removeCategoryByName(String categoryName);

    /**
     * 获取类别
     * @param categoryName 类别名称
     * @return 仅返回类别
     */
    Optional<Category> getCategoryByName(String categoryName);

    /**
     * 获取类别，包括该类别所对应的文章
     * @param categoryName 类别名称
     * @return 返回类别及其文章
     */
    Optional<Category> getCategoryWithPosts(String categoryName);

    /**
     * 获取所有的类别
     * @return 返回所有的类别
     */
    List<Category> listAllCategories();

    /**
     * 获取所有的类别，包括类别所对应的文章
     * @return 返回所有类别及其对应的文章
     */
    List<Category> listAllCategoriesWithPosts();

}
