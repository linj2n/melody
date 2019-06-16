package cn.linj2n.melody.repository;

import cn.linj2n.melody.domain.Category;
import cn.linj2n.melody.domain.Post;
import cn.linj2n.melody.domain.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long>, JpaSpecificationExecutor {

    Optional<Post> findOptionalByTitle(String title);

    Optional<Post> findOptionalById(Long id);

    List<Post> findAllByOrderByCreatedAtDesc();

    List<Post> findAllByOrderByCreatedAtDesc(Specification<Post> specification);

    Page<Post> findByTitleContaining(String title, Pageable pageable);

    @Query(" SELECT post FROM Post post JOIN post.tags tag " +
            " WHERE tag.id IN :tagIds " +
            " GROUP BY post.id HAVING COUNT(post.id) = :tagCount ")
    Page<Post> findAllByTags(@Param("tagIds") List<Long> tagIds, @Param("tagCount") Long tagCount, Pageable pageable);

    @Query(" SELECT post FROM Post post JOIN post.tags tag " +
            " WHERE tag.id IN :tagIdList AND LOWER(post.title) LIKE %:lowerTitle% " +
            " GROUP BY post.id HAVING COUNT(post.id) = :tagCount ")
    Page<Post> findAllByTagsAndTitle(@Param("tagIdList") List<Long> tagIdList,
                                     @Param("tagCount") Long tagCount,
                                     @Param("lowerTitle") String lowerTitle,
                                     Pageable pageable);

    @Query(" SELECT post FROM Post post JOIN post.categories category " +
            " WHERE category.id IN :categoryIdList AND LOWER(post.title) LIKE %:lowerTitle% " +
            " GROUP BY post.id HAVING COUNT(post.id) = :categoryCount ")
    Page<Post> findAllByCategoriesAndTitle(@Param("categoryIdList") List<Long> categoryIdList,
                                     @Param("categoryCount") Long categoryCount,
                                     @Param("lowerTitle") String lowerTitle,
                                     Pageable pageable);


    @Transactional
    @Modifying
    @Query(value = "UPDATE post SET views = :views WHERE id = :postId ", nativeQuery = true)
    void updatePostViews(@Param("postId") Long postId,
                         @Param("views") Long views);

    // TODO: Construct dynamic querying instead of native Query
    final String FIND_POSTS_BY_SEARCH_SQL_STRING =
            " SELECT * FROM Post WHERE id IN "  +
                    " (SELECT post_id FROM post_category " +
                        " WHERE category_id IN :categoryIdList " +
                        " GROUP BY post_id HAVING COUNT(post_id) = :categoryCount ) " +
                    " AND id IN "+
                        " (SELECT post_id FROM post_tag " +
                        " WHERE tag_id IN :tagIdList " +
                        " GROUP BY post_id HAVING COUNT(post_id) = :tagCount) " +
                    " AND LOWER(title) LIKE %:lowerTitle% ORDER BY ?#{#pageable}";
    @Query(value = FIND_POSTS_BY_SEARCH_SQL_STRING,
            nativeQuery = true
    )
    Page<Post> findBySearch(@Param("tagIdList") List<Long> tagIdList,
                            @Param("tagCount") Long tagCount,
                            @Param("categoryIdList") List<Long> categoryList,
                            @Param("categoryCount") Long categoryCount,
                            @Param("lowerTitle") String lowerTitle,
                            Pageable pageable);
}
