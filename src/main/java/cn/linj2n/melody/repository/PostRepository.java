package cn.linj2n.melody.repository;

import cn.linj2n.melody.domain.Category;
import cn.linj2n.melody.domain.Post;
import cn.linj2n.melody.domain.Tag;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long>, JpaSpecificationExecutor {
    Optional<Post> findOptionalByTitle(String title);

    Optional<Post> findOptionalById(Long id);

    List<Post> findAllByOrderByCreatedAtDesc();

    List<Post> findAllByOrderByCreatedAtDesc(Specification<Post> specification);

    @Query("select post from Post post join post.tags tag " +
            "where tag.id in :tagIds " +
            "group by post.id having count(post.id) = :tagCount")
    List<Post> findAllByTags(@Param("tagIds") List<Long> tagIds, @Param("tagCount") Long tagCount);
}
