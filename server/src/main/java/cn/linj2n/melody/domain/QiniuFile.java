package cn.linj2n.melody.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "qiniu_file")
public class QiniuFile {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 七牛云存储对象的 key ，用于对象的操作
     */
    @Column(name = "qiniu_key")
    private String key;

    /**
     * 七牛云用于存储对象的HASH值
     */
    @Column(name = "qiniu_hash")
    private String hash;

    /**
     * 七牛云对象资源的存储类型，1 表示低频存储，0表示普通存储。
     */
    @Column(name = "qiniu_type")
    private int type;

    /**
     * 七牛云存储对象的MIME类型。
     */
    @Column(name = "qiniu_mime_type")
    private String mimeType;

    /**
     * 七牛云存储对象的大小
     */
    @Column(name = "qiniu_fsize")
    private long size;

    /**
     * 七牛云存储对象上传时间，Unix时间戳格式，且单位是 100纳秒。
     */
    @Column(name = "qiniu_put_time")
    private long putTime;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "attachment_id", nullable = false)
    @JsonIgnore
    private Attachment attachment;

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("key", key)
                .append("hash", hash)
                .append("type", type)
                .append("mimeType", mimeType)
                .append("putTime", putTime)
                .toString();
    }
}
