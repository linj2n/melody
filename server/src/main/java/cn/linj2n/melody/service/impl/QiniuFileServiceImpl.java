package cn.linj2n.melody.service.impl;

import cn.linj2n.melody.config.MelodyProperties;
import cn.linj2n.melody.domain.QiniuFile;
import cn.linj2n.melody.repository.QiniuFileRepository;
import cn.linj2n.melody.service.QiniuFileService;
import cn.linj2n.melody.utils.QiniuUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.storage.model.StorageType;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QiniuFileServiceImpl implements QiniuFileService {

    private static final Logger logger = LoggerFactory.getLogger(QiniuFileServiceImpl.class);

    private MelodyProperties melodyProperties;

    private Auth qiniuAuth;

    private BucketManager qiniuBucketManager;

    private QiniuFileRepository fileRepository;

    @Autowired
    public QiniuFileServiceImpl(MelodyProperties melodyProperties, Auth qiniuAuth, BucketManager qiniuBucketManager, QiniuFileRepository fileRepository) {
        this.melodyProperties = melodyProperties;
        this.qiniuAuth = qiniuAuth;
        this.qiniuBucketManager = qiniuBucketManager;
        this.fileRepository = fileRepository;
    }

    @Override
    public String getUploadToken() {
        StringMap putPolicy = new StringMap();
        putPolicy.put("callbackUrl", "http://requestbin.fullcontact.com/11mtq281");
        putPolicy.put("callbackBody", "key=$(key)&hash=$(etag)&bucket=$(bucket)&fsize=$(fsize)");
        putPolicy.put("callbackBodyType", "application/json");
        long expireSeconds = 3600;
        return qiniuAuth.uploadToken(
                melodyProperties.getQiniu().getBucket(),
                null);
//        return qiniuAuth.uploadToken(
//                melodyProperties.getQiniu().getBucket(),
//                null,
//                expireSeconds,
//                putPolicy);
    }

    @Override
    @Transactional
    public void deleteFile(Long attachmentId) {
        QiniuFile file = fileRepository.findOne(attachmentId);
        try {
            qiniuBucketManager.delete(getBucket(), file.getKey());
        } catch (QiniuException ex) {
            logger.error("Failed to delete file from Qiniu, fileKey: {}, error code: {} , error msg: {}.",
                    ex.code(), ex.response.toString());
        }
        fileRepository.delete(file);
    }

    @Override
    public QiniuFile renameFileKey(Long attachmentId, String newFileKey) {
        QiniuFile file = fileRepository.findOne(attachmentId);
        String fromBucket = getBucket(), toBucket = getBucket();
        try {
            qiniuBucketManager.move(fromBucket, file.getKey(), toBucket, newFileKey);
        } catch (QiniuException ex) {
            logger.error("Failed to rename key from Qiniu, [fromBucket: {}, fromKey: {}, toBucket: {}, toKey: {}]; error code: {}, error msg: {}",
                    fromBucket, file.getKey(), toBucket, newFileKey, ex.code(), ex.response.toString());
        }
        file.setKey(newFileKey);
        return fileRepository.save(file);
    }

    @Override
    public QiniuFile changeMimeType(Long attachmentId, String newMimeType) {
        QiniuFile file = fileRepository.findOne(attachmentId);
        try {
            qiniuBucketManager.changeMime(getBucket(), file.getKey(), newMimeType);
        } catch (QiniuException ex) {
            logger.error("Failed to change MimeType for Key: {}, error msg: {}",
                    file.getKey(), ex.response.toString());
        }
        file.setMimeType(newMimeType);
        return fileRepository.save(file);
    }

    @Override
    public QiniuFile changeStoreType(Long attachmentId, StorageType newStoreageType) {
        QiniuFile file = fileRepository.findOne(attachmentId);
        try {
            qiniuBucketManager.changeType(getBucket(), file.getKey(), newStoreageType);
        } catch (QiniuException ex) {
            logger.error("Failed to change store type for Key: {}, error msg: {}",
                    file.getKey(), ex.response.toString());
        }
        file.setType(newStoreageType.ordinal());
        return fileRepository.save(file);
    }

    @Override
    public QiniuFile getFileById(Long attachmentId) {
        return fileRepository.findOne(attachmentId);
    }

    @Override
    public QiniuFile updateQiniuFile(QiniuFile oldFile, QiniuFile newFile) {
        FileInfo fileInfo = null;
        try {
            if (!oldFile.getMimeType().equals(newFile.getMimeType())) {
                qiniuBucketManager.changeMime(getBucket(), oldFile.getKey(), newFile.getMimeType());
            }
            if (oldFile.getType() != newFile.getType()) {
                qiniuBucketManager.changeType(getBucket(), oldFile.getKey(), StorageType.values()[newFile.getType()]);
            }
            if (!oldFile.getKey().equals(newFile.getKey())) {
                String fromBucket = getBucket(), toBucket = getBucket();
                qiniuBucketManager.move(fromBucket, oldFile.getKey(), toBucket, newFile.getKey());
            }
            fileInfo = qiniuBucketManager.stat(getBucket(), newFile.getKey());
        } catch (QiniuException ex) {
            logger.error("Failed to update file info: {}, error msg: {}",
                    oldFile.getKey(), ex.response.toString());
            throw new RuntimeException(ex.response.toString());
        }
        if (fileInfo != null) {
            QiniuUtil.mapToQiniuFile(fileInfo, newFile);
        }
        return newFile;
    }

    private String getBucket() {
        return melodyProperties.getQiniu().getBucket();
    }
}
