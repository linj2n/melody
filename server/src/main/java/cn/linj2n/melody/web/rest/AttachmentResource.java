package cn.linj2n.melody.web.rest;

import cn.linj2n.melody.domain.Attachment;
import cn.linj2n.melody.service.AttachmentService;
import cn.linj2n.melody.service.QiniuFileService;
import cn.linj2n.melody.web.dto.AttachmentDTO;
import cn.linj2n.melody.web.dto.qiniu.QiniuCallBackResult;
import cn.linj2n.melody.web.utils.DTOModelMapper;
import cn.linj2n.melody.web.utils.ResponseBuilder;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api")
public class AttachmentResource {

    private static final Logger logger = LoggerFactory.getLogger(AttachmentResource.class);

    public static final String QINIU_AUTHORIZATION_HEADER = "Authorization";

    private AttachmentService attachmentService;

    private QiniuFileService qiniuFileService;

    private DTOModelMapper dtoModelMapper;

    @Autowired
    public AttachmentResource(AttachmentService attachmentService, QiniuFileService qiniuFileService, DTOModelMapper dtoModelMapper) {
        this.attachmentService = attachmentService;
        this.qiniuFileService = qiniuFileService;
        this.dtoModelMapper = dtoModelMapper;
    }

    @RequestMapping(
            value = "/v1/attachments/_create",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createAttachment(HttpServletRequest request) throws IOException {
        final String callbackAuthHeader = request.getHeader(QINIU_AUTHORIZATION_HEADER);
        String callbackBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        if (!qiniuFileService.isValidCallBack(callbackAuthHeader, callbackBody.getBytes(StandardCharsets.UTF_8))) {
            logger.info("Qiniu callback request authentication failed.");
            return new ResponseEntity<>(ResponseBuilder.buildFailedResponse("服务器出错，附件创建的失败", null), HttpStatus.UNAUTHORIZED);
        }
        logger.info("Qiniu callback request authentication succeeded.");
        QiniuCallBackResult result = new Gson().fromJson(callbackBody, QiniuCallBackResult.class);
        return new ResponseEntity<>(
                ResponseBuilder.buildSuccessResponse(
                        "附件上传成功",
                        dtoModelMapper.convertToDTO(attachmentService.createAttachment(result.getKey()))
                )
                , HttpStatus.OK);
    }


    @RequestMapping(
            value = "/v1/attachments",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> listAllAttachments(@RequestParam(value = "title", required = false) String title,  Pageable pageable) {
        if (title != null && !title.isEmpty()) {
            return new ResponseEntity<>(
                    ResponseBuilder.buildSuccessResponse(null,
                            attachmentService
                                    .queryAttachmentsByNameContaining(title, pageable)
                                    .map(dtoModelMapper::convertToDTO)
                    ),
                    HttpStatus.OK);
        }
        return new ResponseEntity<>(
                ResponseBuilder.buildSuccessResponse(
                        null,
                        attachmentService
                                .listAllAttachmentByPage(pageable)
                                .map(dtoModelMapper::convertToDTO)
                ),
                HttpStatus.OK);
    }

    @RequestMapping(
            value = "/v1/attachments/{attachmentId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getAttachment(@PathVariable(value = "attachmentId", required = true) Long attachmentId) {
        return attachmentService
                .getAttachment(attachmentId)
                .map(attachment -> new ResponseEntity<>(
                        ResponseBuilder.buildSuccessResponse(
                                null,
                                dtoModelMapper.convertToDTO(attachment)
                        ),
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(ResponseBuilder.buildFailedResponse("附件不存在", null),HttpStatus.OK));
    }

    @RequestMapping(
            value = "/v1/attachments/{attachmentId}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updateAttachment(@RequestBody AttachmentDTO attachmentDTO) {
        if (attachmentService.checkIfExistsByName(attachmentDTO.getName(), attachmentDTO.getId())) {
            return new ResponseEntity<>(ResponseBuilder.buildFailedResponse("附件名已存在", null), HttpStatus.OK);
        }
        Attachment attachment = dtoModelMapper.convertToEntity(attachmentDTO);
        Attachment result = attachmentService.updateAttachment(attachment);
        return new ResponseEntity<>(
                ResponseBuilder.buildSuccessResponse("更新成功", dtoModelMapper.convertToDTO(result)),
                HttpStatus.OK);
    }

    @RequestMapping(
            value = "/v1/attachments/{attachmentId}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> deleteAttachment(@PathVariable(value = "attachmentId", required = true) Long attachmentId) {
        attachmentService.deleteAttachment(attachmentId);
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse("删除成功", null), HttpStatus.OK);
    }
}
