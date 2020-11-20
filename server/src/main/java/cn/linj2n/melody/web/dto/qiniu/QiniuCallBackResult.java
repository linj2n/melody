package cn.linj2n.melody.web.dto.qiniu;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;

public class QiniuCallBackResult implements Serializable {
    private String key;
    private String hash;
    private String bucket;
    private long fsize;

    public QiniuCallBackResult() {
    }

    public String toString() {
        return (new ToStringBuilder(this)).append("key", this.key).append("hash", this.hash).append("bucket", this.bucket).append("fsize", this.fsize).toString();
    }

    public String getKey() {
        return this.key;
    }

    public String getHash() {
        return this.hash;
    }

    public String getBucket() {
        return this.bucket;
    }

    public long getFsize() {
        return this.fsize;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public void setFsize(long fsize) {
        this.fsize = fsize;
    }
}
