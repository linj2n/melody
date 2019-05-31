package cn.linj2n.melody.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QiniuFileDTO {
    /**
     * 附件的HASH值
     */
    private String hash;
    /**
     * 附件存储类型，1 表示低频存储，0表示普通存储。
     */
    private int type;

    /**
     * 附件MIME类型。
     */
    private String mimeType;

    /**
     * 附件大小，单位为 KB
     */
    private long size;
    /**
     * 上传时间，Unix时间戳格式，且单位是 秒 。
     */
    private long putTime;

    /**
     * 访问附件的域名
     */
    private String domain;

    /**
     * 访问附件的路径
     */
    private String path;

    /**
     * 访问附件的url
     */
    private String url;
}
