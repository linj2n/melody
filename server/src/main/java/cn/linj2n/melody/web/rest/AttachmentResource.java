package cn.linj2n.melody.web.rest;

import cn.linj2n.melody.domain.Attachment;
import cn.linj2n.melody.service.AttachmentService;
import cn.linj2n.melody.service.QiniuFileService;
import cn.linj2n.melody.web.dto.AttachmentDTO;
import cn.linj2n.melody.web.utils.DTOModelMapper;
import cn.linj2n.melody.web.utils.ResponseBuilder;
import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api")
public class AttachmentResource {

    private static final Logger logger = LoggerFactory.getLogger(AttachmentResource.class);

    private AttachmentService attachmentService;

    private DTOModelMapper dtoModelMapper;

    @Autowired
    public AttachmentResource(AttachmentService attachmentService, DTOModelMapper dtoModelMapper) {
        this.attachmentService = attachmentService;
        this.dtoModelMapper = dtoModelMapper;
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
