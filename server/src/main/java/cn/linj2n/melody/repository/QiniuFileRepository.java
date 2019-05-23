package cn.linj2n.melody.repository;

import cn.linj2n.melody.domain.QiniuFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QiniuFileRepository extends JpaRepository<QiniuFile, Long> {
}
