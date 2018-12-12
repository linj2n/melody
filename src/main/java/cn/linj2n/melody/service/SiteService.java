package cn.linj2n.melody.service;

import cn.linj2n.melody.domain.Category;
import cn.linj2n.melody.domain.Tag;
import cn.linj2n.melody.web.dto.Archive;

import java.util.List;
import java.util.Map;

public interface SiteService {

    List<Tag> listAllTagsWithPosts();

    List<Category> listAllCategoriesWithPosts();

    List<Archive> listArchives();

    Map<String, List<Archive>> getArchivesGroupByYear();

    Map<String, List<Archive>> getArchivesByTagId(Long tagId);

    Map<String, List<Archive>> getArchivesByCategoryId(Long categoryId);

    List<Archive> getArchivesGroupByCategory();

}
