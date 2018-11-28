package cn.linj2n.melody.repository;

import cn.linj2n.melody.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long>{
    Optional<Category> findOptionalByName(String name);

    List<Category> findAllByOrderByNameAsc();
}
