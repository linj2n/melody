package cn.linj2n.melody.config;



import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author linj2n
 */
@Component
@ConfigurationProperties(prefix = "melody", ignoreUnknownFields = false)
public class MelodyProperties {

    private final Qiniu qiniu = new Qiniu();

    private final Redis redisProperties = new Redis();

    public Redis getRedis() {
        return redisProperties;
    }

    public Qiniu getQiniu(){
        return qiniu;
    }
    public static class Qiniu {
        private String accessKey;

        private String secretKey;

        private String bucket;

        private String uploadUrl;

        private String externalDefaultLinkDomain;

        private String callBackHandlingUrl;

        public String getAccessKey() {
            return accessKey;
        }

        public void setAccessKey(String accessKey) {
            this.accessKey = accessKey;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }

        public String getBucket() {
            return bucket;
        }

        public void setBucket(String bucket) {
            this.bucket = bucket;
        }

        public String getUploadUrl() {
            return uploadUrl;
        }

        public void setUploadUrl(String uploadUrl) {
            this.uploadUrl = uploadUrl;
        }

        public String getExternalDefaultLinkDomain() {
            return externalDefaultLinkDomain;
        }

        public void setExternalDefaultLinkDomain(String externalDefaultLinkDomain) {
            this.externalDefaultLinkDomain = externalDefaultLinkDomain;
        }

        public String getCallBackHandlingUrl() {
            return callBackHandlingUrl;
        }

        public void setCallBackHandlingUrl(String callBackHandlingUrl) {
            this.callBackHandlingUrl = callBackHandlingUrl;
        }
    }

    public static class Redis {

        private String host;
        private int port;
        private String password;
        private boolean usePool;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public boolean getUsePool() {
            return usePool;
        }

        public void setUsePool(boolean usePool) {
            this.usePool = usePool;
        }
    }
}
