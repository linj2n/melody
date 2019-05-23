package cn.linj2n.melody.service.impl;

import cn.linj2n.melody.config.MelodyProperties;
import cn.linj2n.melody.domain.QiniuFile;
import cn.linj2n.melody.repository.QiniuFileRepository;
import cn.linj2n.melody.service.QiniuFileService;
import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.model.StorageType;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QiniuFileServiceImpl implements QiniuFileService {

    private static final Logger logger = LoggerFactory.getLogger(QiniuFileServiceImpl.class);

    private MelodyProperties melodyProperties;

    private Auth qiniuAuth;

    private BucketManager qiniuBucketManager;

    private QiniuFileRepository fileRepository;

    @Autowired
    public QiniuFileServiceImpl(MelodyProperties melodyProperties, Auth qiniuAuth, BucketManager qiniuBucketManager) {
        this.melodyProperties = melodyProperties;
        this.qiniuAuth = qiniuAuth;
        this.qiniuBucketManager = qiniuBucketManager;
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
    public void deleteFile(String fileKey) {
        try {
            qiniuBucketManager.delete(getBucket(), fileKey);
        } catch (QiniuException ex) {
            logger.error("Failed to delete file from Qiniu, fileKey: {}, error code: {} , error msg: {}.",
                    ex.code(), ex.response.toString());
        }
    }

    @Override
    public QiniuFile renameFileKey(QiniuFile newFile) {
        QiniuFile oldFile = fileRepository.findOne(newFile.getId());
        String fromBucket = getBucket(), toBucket = getBucket();
        try {
            qiniuBucketManager.move(fromBucket, oldFile.getKey(), toBucket, newFile.getKey());
        } catch (QiniuException ex) {
            logger.error("Failed to rename key from Qiniu, [fromBucket: {}, fromKey: {}, toBucket: {}, toKey: {}]; error code: {}, error msg: {}",
                    fromBucket, oldFile.getKey(), toBucket, newFile.getKey(), ex.code(), ex.response.toString());
        }
        return fileRepository.save(newFile);
    }

    @Override
    public QiniuFile changeMimeType(QiniuFile newFile) {

        try {
            qiniuBucketManager.changeMime(getBucket(), newFile.getKey(), newFile.getMimeType());
        } catch (QiniuException ex) {
            logger.error("Failed to change MimeType for Key: {}, error msg: {}",
                    newFile.getKey(), ex.response.toString());
        }
        return fileRepository.save(newFile);
    }

    @Override
    public QiniuFile changeStoreType(QiniuFile newFile) {
        try {
            qiniuBucketManager.changeType(getBucket(), newFile.getKey(), StorageType.values()[newFile.getType()]);
        } catch (QiniuException ex) {
            logger.error("Failed to change store type for Key: {}, error msg: {}",
                    newFile.getKey(), ex.response.toString());
        }
        return fileRepository.save(newFile);
    }

    private String getBucket() {
        return melodyProperties.getQiniu().getBucket();
    }
}
