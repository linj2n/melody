package cn.linj2n.melody.repository;

import cn.linj2n.melody.domain.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    Optional<Attachment> findOptionalById(Long id);

    Optional<Attachment> findOptionalByName(String name);

    Optional<Attachment> findAttachmentByNameAndIdNot(String name, Long id);
}
