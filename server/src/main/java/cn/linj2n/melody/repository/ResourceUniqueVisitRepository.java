package cn.linj2n.melody.repository;

import cn.linj2n.melody.domain.traffic.ResourceUniqueVisitor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ResourceUniqueVisitRepository extends JpaRepository<ResourceUniqueVisitor, Long> {

    List<ResourceUniqueVisitor>  findAllByNameAndDateBetweenOrderByDateDesc(String name, Date start, Date end);

    List<ResourceUniqueVisitor> findTop15ByNameOrderByDateDesc(String name);
}
