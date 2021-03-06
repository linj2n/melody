package cn.linj2n.melody.service;

import cn.linj2n.melody.domain.Category;
import cn.linj2n.melody.domain.Post;
import cn.linj2n.melody.domain.Tag;
import cn.linj2n.melody.web.dto.Archive;
import cn.linj2n.melody.web.dto.PostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public interface SiteService {

    List<Tag> listAllTagsWithPosts();

    List<Category> listAllCategoriesWithPosts();

    List<Archive> listArchives();

    Map<String, List<Archive>> getArchivesGroupByYear();

    Map<String, List<Archive>> getArchivesByTagId(long tagId);

    Map<String, List<Archive>> getArchivesByCategoryId(long categoryId);

    List<Archive> getArchivesGroupByCategory();

    TreeMap<Integer, List<PostDTO>> groupAllPostsByYear();

    Map<Integer, List<PostDTO>> groupPostsByYear(List<Post> posts);

    Map<Integer, Map<Month, List<PostDTO>>> groupPostsByYearMonth(List<Post> posts);

    Map<Integer, Map<Month, List<PostDTO>>> groupAllPostsByYearMonth();

    Map<String, List<PostDTO>> groupAllPostsByCategory();

    Optional<Tag> getTagWithPostsById(long tagId);

    Optional<Archive> getArchiveByTagId(long tagId);

    Optional<Category> getCategoryWithPostsById(long categoryId);

    Page<PostDTO> listPostsByPage(Pageable pageable);
}
