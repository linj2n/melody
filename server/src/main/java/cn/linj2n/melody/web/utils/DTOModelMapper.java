package cn.linj2n.melody.web.utils;

import cn.linj2n.melody.config.MelodyProperties;
import cn.linj2n.melody.domain.*;
import cn.linj2n.melody.utils.QiniuUtil;
import cn.linj2n.melody.web.dto.AttachmentDTO;
import cn.linj2n.melody.web.dto.PostDTO;
import cn.linj2n.melody.web.dto.QiniuFileDTO;
import cn.linj2n.melody.web.dto.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class DTOModelMapper {

    private ModelMapper modelMapper;

    private ViewUtils viewUtils;

    private MelodyProperties melodyProperties;

    @Autowired
    public DTOModelMapper(ModelMapper modelMapper, ViewUtils viewUtils, MelodyProperties melodyProperties) {
        this.modelMapper = modelMapper;
        this.viewUtils = viewUtils;
        this.melodyProperties = melodyProperties;
    }

    public PostDTO convertToDTO(final Post post) {
        if (post == null) {
            return null;
        }
        PostDTO postDTO = modelMapper.map(post,PostDTO.class);
        if (post.getCreatedAt() != null) {
            postDTO.setCreatedAt(post.getCreatedAt().toLocalDateTime());
        }
        if (post.getUpdatedAt() != null) {
            postDTO.setUpdatedAt(post.getUpdatedAt().toLocalDateTime());
        }
//        postDTO.setContent(viewUtils.renderToHtml(post.getContent()));
        postDTO.setContent(post.getContent());
        return postDTO;
    }

    public Post convertToEntity(PostDTO postDTO) {
        return modelMapper.map(postDTO, Post.class);
    }

    public UserDTO convertToDTO(final User user) {
        if (user == null) {
            return null;
        }
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.setAuthorities(user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet()));
        return userDTO;
    }

    // Converting to entity just simply using setter and getter.
    public Attachment convertToEntity(AttachmentDTO attachmentDTO) {

        QiniuFileDTO qiniuFileDTO = attachmentDTO.getQiniuFileDTO();
        QiniuFile qiniuFile = new QiniuFile();
        qiniuFile.setId(attachmentDTO.getId());
        qiniuFile.setKey(qiniuFileDTO
                .getPath()
                .concat(attachmentDTO.getName()));
        qiniuFile.setType(qiniuFileDTO.getType());
        qiniuFile.setMimeType(qiniuFileDTO.getMimeType());

        Attachment attachment = new Attachment();
        attachment.setId(attachmentDTO.getId());
        attachment.setName(attachmentDTO.getName());
        attachment.setDescription(attachmentDTO.getDescription());
        attachment.setQiniuFile(qiniuFile);
        return attachment;
    }

    // Converting to dto just simply using setter and getter.
    public AttachmentDTO convertToDTO(Attachment attachment) {
        QiniuFile qiniuFile = attachment.getQiniuFile();
        QiniuFileDTO qiniuFileDTO = new QiniuFileDTO();
        qiniuFileDTO.setType(qiniuFile.getType());
        qiniuFileDTO.setMimeType(qiniuFile.getMimeType());
        qiniuFileDTO.setHash(qiniuFile.getHash());

        // Convert Byte to Kilobyte
        qiniuFileDTO.setSize(qiniuFile.getSize() / 1024);
        // Convert nanoseconds to second
        qiniuFileDTO.setPutTime(qiniuFile.getPutTime() / 10000000);
        // Get path from key
        qiniuFileDTO.setPath(QiniuUtil.getPathFromKey(qiniuFile.getKey()));
        // Set URL
        String url = melodyProperties.getQiniu().getExternalDefaultLinkDomain() + qiniuFileDTO.getPath() + attachment.getName();
        qiniuFileDTO.setUrl(url);

        AttachmentDTO attachmentDTO = new AttachmentDTO();
        attachmentDTO.setName(attachment.getName());
        attachmentDTO.setDescription(attachment.getDescription());
        attachmentDTO.setId(attachment.getId());
        attachmentDTO.setQiniuFileDTO(qiniuFileDTO);

        return attachmentDTO;
    }

}
