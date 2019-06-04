package cn.linj2n.melody.service;

import cn.linj2n.melody.domain.Attachment;
import cn.linj2n.melody.domain.QiniuFile;
import com.qiniu.storage.model.StorageType;

public interface QiniuFileService {

    String getUploadToken();

    void deleteFile(String fileKey);

    QiniuFile getFileById(Long attachmentId);

    QiniuFile renameFileKey(Long attachmentId, String newFileKey);

    QiniuFile changeMimeType(Long attachmentId, String newMimeType);

    QiniuFile changeStoreType(Long attachmentId, StorageType newStorageType);

    QiniuFile updateQiniuFile(QiniuFile oldFile, QiniuFile newFile);

    QiniuFile getRemoteFileInfoByKey(String key);

    QiniuFile addQiniuFile(QiniuFile qiniuFile);

}
