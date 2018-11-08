package cn.linj2n.melody.repository;

import cn.linj2n.melody.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag,Long>{
    Optional<Tag> findOptionalByName(String name);

    Optional<Tag> findOptionalByNameIgnoreCase(String name);

    List<Tag> findAllByName(String name);

    Optional<Tag> findOptionalById(Long id);
}
