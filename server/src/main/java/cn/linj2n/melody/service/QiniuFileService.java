package cn.linj2n.melody.service;

import cn.linj2n.melody.domain.QiniuFile;
import com.qiniu.storage.model.StorageType;

public interface QiniuFileService {

    String getUploadToken();

    void deleteFile(String fileKey);

    QiniuFile renameFileKey(QiniuFile newFile);

    QiniuFile changeMimeType(QiniuFile newFile);

    QiniuFile changeStoreType(QiniuFile newFile);
}
