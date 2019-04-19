package cn.linj2n.melody.repository;


import cn.linj2n.melody.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority,Long> {
    Optional<Authority> findOneByName(String name);
}
