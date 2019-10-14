package cn.linj2n.melody.web.utils;

import cn.linj2n.melody.config.MelodyProperties;
import cn.linj2n.melody.domain.*;
import cn.linj2n.melody.security.AuthoritiesConstants;
import cn.linj2n.melody.security.SecurityUtil;
import cn.linj2n.melody.utils.QiniuUtil;
import cn.linj2n.melody.web.dto.AttachmentDTO;
import cn.linj2n.melody.web.dto.PostDTO;
import cn.linj2n.melody.web.dto.QiniuFileDTO;
import cn.linj2n.melody.web.dto.UserDTO;
import cn.linj2n.melody.web.dto.comment.CommentAuthorDTO;
import cn.linj2n.melody.web.dto.comment.CommentDTO;
import cn.linj2n.melody.web.dto.comment.ReplyToCommentDTO;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.stream.Collectors;

@Component
public class DTOModelMapper {

    private static final int MAX_LENGTH_OF_CONTENT_PREVIEW = 300;

    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private ViewUtils viewUtils;

    @Autowired
    private MelodyProperties melodyProperties;

    public DTOModelMapper() {
        this.modelMapper.addMappings(getCommentMap());
        this.modelMapper.addMappings(getCommentAuthorMap());
        this.modelMapper.addMappings(getReplyToCommentMap());

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
        postDTO.setContentPreview(postDTO.getSummary());
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
        userDTO.setPassword("");
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


    public CommentDTO convertToDTO(Comment comment) {
        return modelMapper.map(comment, CommentDTO.class);
    }

    public CommentAuthorDTO convertToCommentAuthorDTO(User user) {
        return modelMapper.map(user, CommentAuthorDTO.class);
    }

    public ReplyToCommentDTO convertToReplyDTO(Comment comment) {
        return modelMapper.map(comment, ReplyToCommentDTO.class);
    }

    private PropertyMap<Comment, ReplyToCommentDTO> getReplyToCommentMap() {
        return new PropertyMap<Comment, ReplyToCommentDTO>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setContent(source.getContent());
                using(zonedDateTimeToLongConverter()).map(source.getCreatedAt()).setCreatedTime(null);
            }
        };
    }

    private PropertyMap<Comment, CommentDTO> getCommentMap() {
        return new PropertyMap<Comment, CommentDTO>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setContent(source.getContent());
                map().setReplyCount(source.getReplyCount());
                using(zonedDateTimeToLongConverter())
                        .map(source.getCreatedAt()).setCreatedTime(null);


            }
        };
    }

    private PropertyMap<User, CommentAuthorDTO> getCommentAuthorMap() {
        return new PropertyMap<User, CommentAuthorDTO>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setAvatar(source.getAvatarUrl());
                using(usernameConverter())
                        .map(source).setName(null);
                using((Converter<Object, Boolean>) context -> SecurityUtil.isCurrentUserInRole(AuthoritiesConstants.ADMIN))
                        .map().setPostAuthor(false);
            }
        };
    }

    private Converter<ZonedDateTime, Long> zonedDateTimeToLongConverter() {
        return context -> context.getSource().toEpochSecond();
    }

    private Converter<User, String> usernameConverter() {
        return context -> {
            User user = context.getSource();
            return user.getUsername() != null ? user.getUsername() : user.getLogin();
        };
    }


}
