package cn.linj2n.melody.service.impl;

import cn.linj2n.melody.domain.Attachment;
import cn.linj2n.melody.domain.QiniuFile;
import cn.linj2n.melody.repository.AttachmentRepository;
import cn.linj2n.melody.service.AttachmentService;
import cn.linj2n.melody.service.QiniuFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    private static final Logger logger = LoggerFactory.getLogger(AttachmentServiceImpl.class);

    private AttachmentRepository attachmentRepository;

    private QiniuFileService qiniuFileService;

    @Autowired
    public AttachmentServiceImpl(AttachmentRepository attachmentRepository, QiniuFileService qiniuFileService) {
        this.attachmentRepository = attachmentRepository;
        this.qiniuFileService = qiniuFileService;
    }

    @Override
    @Transactional
    public Page<Attachment> listAllAttachmentByPage(Pageable pageable) {
        return attachmentRepository.findAll(pageable).map(attachment -> {
            attachment.getQiniuFile().getType();
            return attachment;
        });

    }

    @Override
    @Transactional
    public Optional<Attachment> getAttachment(Long id) {
        return attachmentRepository.findOptionalById(id);
    }

    @Override
    @Transactional
    public void deleteAttachment(Long attachmentId) {
        getAttachment(attachmentId).map(attachment -> {
            qiniuFileService.deleteFile(attachment.getQiniuFile().getKey());
            attachment.setQiniuFile(null);
            attachmentRepository.delete(attachment);
            return attachment;
        });
    }

    @Override
    public Attachment updateAttachment(Attachment newAttachment) {
        if (checkIfExistsByName(newAttachment.getName(), newAttachment.getId())) {
            logger.error("Failed to update attachment: the new file name has already existed");
            throw new RuntimeException("Failed to get the attachment.");
        }
        return attachmentRepository
                    .findOptionalById(newAttachment.getId())
                    .map(oldAttachment -> {
                        // Update file info in qiniu cloud
                        newAttachment.setQiniuFile(qiniuFileService
                                .updateQiniuFile(oldAttachment.getQiniuFile(),
                                        newAttachment.getQiniuFile()
                                ));
                        newAttachment.getQiniuFile().setAttachment(newAttachment);
                        // Update attachment in database and return new value
                        return attachmentRepository.save(newAttachment);
                    })
                    .orElseThrow(() -> {
                        logger.error("Failed to get attachment by id [{}].", newAttachment.getId());
                        return new RuntimeException("Failed to get the attachment.");
                    });
    }

    @Override
    public boolean checkIfExistsByName(String name, Long id) {
        return attachmentRepository.findAttachmentByNameAndIdNot(name, id).isPresent();
    }

    @Override
    public Page<Attachment> queryAttachmentsByNameContaining(String name, Pageable pageable) {
        return attachmentRepository.findByNameContaining(name, pageable).map(attachment -> {
            attachment.getQiniuFile();
            return attachment;
        });
    }

    @Override
    public Attachment createAttachment(String qiniuFileKey) {
        // TODO: Handling invalid qiniu file key
        QiniuFile qiniuFile = qiniuFileService.getRemoteFileInfoByKey(qiniuFileKey);
        Attachment newAttachment = new Attachment();
        newAttachment.setName(qiniuFileKey);
        newAttachment.setQiniuFile(qiniuFile);
        qiniuFile.setAttachment(newAttachment);
        return attachmentRepository.save(newAttachment);
    }

}
