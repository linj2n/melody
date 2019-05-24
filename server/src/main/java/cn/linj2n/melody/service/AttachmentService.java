package cn.linj2n.melody.service;

import cn.linj2n.melody.domain.Attachment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AttachmentService {

    Page<Attachment> listAllAttachmentByPage(Pageable pageable);

    boolean checkIfExistsByName(String name);

    boolean checkIfExistsByName(String name, Long id);

    Optional<Attachment> getAttachment(Long id);

    Optional<Attachment> getAttachmentByName(String name);

    void deleteAttachment(Long attachmentId);

    void updateAttachment(Attachment newAttachment);

    Attachment addAttachment(Attachment newAttachment);

    Attachment renameAttachment(Attachment newAttachment);

    Attachment changeMimeType(Attachment newAttachment);

}
