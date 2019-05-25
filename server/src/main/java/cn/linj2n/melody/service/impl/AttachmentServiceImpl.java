package cn.linj2n.melody.service.impl;

import cn.linj2n.melody.domain.Attachment;
import cn.linj2n.melody.domain.QiniuFile;
import cn.linj2n.melody.repository.AttachmentRepository;
import cn.linj2n.melody.service.AttachmentService;
import cn.linj2n.melody.service.QiniuFileService;
import cn.linj2n.melody.service.utils.QiniuUtil;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
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
    public Optional<Attachment> getAttachment(Long id) {
        return attachmentRepository.findOptionalById(id);
    }

    @Override
    @Transactional
    public void deleteAttachment(Long attachmentId) {
        qiniuFileService.deleteFile(attachmentId);
        getAttachment(attachmentId).map(attachment -> {
            attachment.setQiniuFile(null);
            attachmentRepository.delete(attachment);
            return attachment;
        });
    }

    @Override
    public Attachment addAttachment(Attachment newAttachment) {
        return attachmentRepository.save(newAttachment);
    }

    @Override
    public Attachment renameAttachment(Attachment newAttachment) {
        String newKey = renameFileInKey(newAttachment.getQiniuFile().getKey(), newAttachment.getName());
        qiniuFileService.renameFileKey(newAttachment.getId(), newKey);
        return attachmentRepository.save(newAttachment);
    }

    private String renameFileInKey(String originKey, String fileName) {
        int divideIndex = originKey.lastIndexOf('/');
        if (divideIndex == -1) {
            return fileName;
        }
        return originKey.substring(0, divideIndex + 1) + fileName;
    }

    @Override
    public Attachment changeMimeType(Attachment newAttachment) {
        return null;
    }

    @Override
    public Optional<Attachment> getAttachmentByName(String name) {
        return attachmentRepository.findOptionalByName(name);
    }

    @Override
    public void updateAttachment(Attachment newAttachment) {
        if (checkIfExistsByName(newAttachment.getName(), newAttachment.getId())) {
            logger.error("Failed to update attachment: the new file name has already existed");
            throw new RuntimeException("Failed to get the attachment.");
        }
        attachmentRepository
                .findOptionalById(newAttachment.getId())
                .map(oldAttachment -> {
                    // 1. Set the new filename in qiniu key.
                    updateKey(newAttachment);
                    // 2. Update the file info in qiniu cloud.
                    qiniuFileService.updateQiniuFile(oldAttachment.getQiniuFile(), newAttachment.getQiniuFile());
                    // 3. Update attachment in database
                    attachmentRepository.save(newAttachment);
                    return oldAttachment;
                })
                .orElseThrow(() -> {
                    logger.error("Failed to get attachment by id [{}].", newAttachment.getId());
                    return new RuntimeException("Failed to get the attachment.");
                });
    }

    private void updateKey(Attachment attachment) {
        QiniuFile file = attachment.getQiniuFile();
        String newKey = QiniuUtil
                .getPathFromKey(file.getKey())
                .concat(attachment.getName());
        file.setKey(newKey);
    }

    @Override
    public boolean checkIfExistsByName(String name) {
        return attachmentRepository.findOptionalByName(name).isPresent();
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
}
