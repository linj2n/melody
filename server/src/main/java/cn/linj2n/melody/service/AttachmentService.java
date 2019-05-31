package cn.linj2n.melody.service;

import cn.linj2n.melody.domain.Attachment;
import cn.linj2n.melody.web.dto.AttachmentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AttachmentService {

    Page<Attachment> listAllAttachmentByPage(Pageable pageable);

    boolean checkIfExistsByName(String name, Long id);

    Optional<Attachment> getAttachment(Long id);

    void deleteAttachment(Long attachmentId);

    Attachment updateAttachment(Attachment newAttachment);

    Page<Attachment> queryAttachmentsByNameContaining(String name, Pageable pageable);
}
