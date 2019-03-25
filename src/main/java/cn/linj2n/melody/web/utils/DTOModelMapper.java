package cn.linj2n.melody.web.utils;

import cn.linj2n.melody.domain.Post;
import cn.linj2n.melody.web.dto.PostDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DTOModelMapper {

    private ModelMapper modelMapper;

    private ViewUtils viewUtils;

    @Autowired
    public DTOModelMapper(ModelMapper modelMapper, ViewUtils viewUtils) {
        this.modelMapper = modelMapper;
        this.viewUtils = viewUtils;
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
        postDTO.setContent(viewUtils.renderToHtml(post.getContent()));
        return postDTO;
    }

    public Post convertToEntity(PostDTO postDTO) {
        return modelMapper.map(postDTO, Post.class);
    }
}
