package cn.linj2n.melody.repository;

import cn.linj2n.melody.domain.traffic.ResourceView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface ResourceViewRepository extends JpaRepository<ResourceView, Long> {

    List<ResourceView> findAllByNameAndDateBetweenOrderByDateDesc(String name, Date start, Date end);

    List<ResourceView> findTop15ByNameOrderByDateDesc(String name);

    @Query(value = " SELECT SUM(resourceView.count) FROM ResourceView resourceView")
    Long getSiteTotalViews();
}
