package cn.linj2n.melody.web.rest;

import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class AttachResource {
    private Auth qiniuAuth;

    @Autowired
    public AttachResource(Auth qiniuAuth) {
        this.qiniuAuth = qiniuAuth;
    }

    public ResponseEntity<?> listAllAttachments
}
