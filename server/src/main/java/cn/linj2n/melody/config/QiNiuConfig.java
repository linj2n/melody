package cn.linj2n.melody.config;

import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author linj2n
 */
@Configuration
public class QiNiuConfig {

    private MelodyProperties melodyProperties;

    @Autowired
    public QiNiuConfig(MelodyProperties melodyProperties) {
        this.melodyProperties = melodyProperties;
    }

    @Bean
    Auth auth() {
        return Auth.create(
                melodyProperties.getQiniu().getAccessKey(),
                melodyProperties.getQiniu().getSecretKey());
    }

    @Bean
    public UploadManager uploadManager() {

        return new UploadManager(new com.qiniu.storage.Configuration(Zone.autoZone()));
    }

    @Bean
    public BucketManager bucketManager() {
        return new BucketManager(auth(), new com.qiniu.storage.Configuration(Zone.autoZone()));
    }

}
