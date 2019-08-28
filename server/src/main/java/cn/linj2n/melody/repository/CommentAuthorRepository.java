package cn.linj2n.melody.repository;

import cn.linj2n.melody.domain.CommentAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentAuthorRepository extends JpaRepository<CommentAuthor, Long> {

    Optional<CommentAuthor> findOptionalById(Long id);

    Optional<CommentAuthor> findOptionalByEmail(String email);
}

