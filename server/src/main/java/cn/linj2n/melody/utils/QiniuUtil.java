package cn.linj2n.melody.utils;

import cn.linj2n.melody.domain.QiniuFile;
import com.qiniu.storage.model.FileInfo;

public class QiniuUtil {
    public static void mapToQiniuFile(FileInfo fileInfo, QiniuFile qiniuFile) {
        qiniuFile.setType(fileInfo.type);
        qiniuFile.setMimeType(fileInfo.mimeType);
        qiniuFile.setHash(fileInfo.hash);
        qiniuFile.setPutTime(fileInfo.putTime);
        qiniuFile.setSize(fileInfo.fsize);
    }

    public static String getFileNameInKey(String qiniuKey) {
        int index = qiniuKey.lastIndexOf('/');
        if (index == -1){
            return qiniuKey;
        } else if (index == qiniuKey.length() - 1){
            return "";
        }
        return  qiniuKey.substring(index + 1);
    }

    public static String getPathFromKey(String qiniuKey) {
        int index = qiniuKey.lastIndexOf('/');
        if (index == -1) {
            return "";
        }
        return qiniuKey.substring(0, index + 1);
    }
}
