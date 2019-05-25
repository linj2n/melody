package cn.linj2n.melody.config;

import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

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
        Zone zone = Zone.autoZone();
        return new UploadManager(new com.qiniu.storage.Configuration(zone));
    }

    @Bean
    public BucketManager bucketManager() {
        Zone zone = Zone.autoZone();
        return new BucketManager(auth(), new com.qiniu.storage.Configuration(zone));
    }

}
