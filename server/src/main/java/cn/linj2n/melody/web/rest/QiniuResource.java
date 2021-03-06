package cn.linj2n.melody.web.rest;

import cn.linj2n.melody.config.MelodyProperties;
import cn.linj2n.melody.service.QiniuFileService;
import cn.linj2n.melody.web.utils.ResponseBuilder;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author linj2n
 */
@RestController
@RequestMapping(value = "/api")
public class QiniuResource {

    private static final Logger logger = LoggerFactory.getLogger(QiniuResource.class);

    private QiniuFileService qiniuFileService;

    private MelodyProperties melodyProperties;

    @Autowired
    public QiniuResource(QiniuFileService qiniuFileService, MelodyProperties melodyProperties) {
        this.qiniuFileService = qiniuFileService;
        this.melodyProperties = melodyProperties;
    }

    @RequestMapping(
            value = "/v1/qiniu/upload-token",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getToken() {
        Map<String, String> data = new HashMap<>();
        data.put("uploadToken", qiniuFileService.getUploadToken());
        data.put("uploadUrl", melodyProperties.getQiniu().getUploadUrl());
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse(null, data),HttpStatus.OK);
    }

    @RequestMapping(
            value = "/v1/qiniu/upload-url",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUploadUrl() {
        Map<String, String> data = new HashMap<>();
        data.put("uploadUrl", melodyProperties.getQiniu().getUploadUrl());
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse(null, data),HttpStatus.OK);
    }

    @RequestMapping(
            value = "/v1/attach",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> addAttach(HttpServletRequest request) {
        logger.info("hit /v1/attach");
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse("upload successfuuly", null), HttpStatus.OK);
    }
    
}
