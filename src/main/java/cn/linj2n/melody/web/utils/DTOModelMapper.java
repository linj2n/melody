package cn.linj2n.melody.web.utils;

import cn.linj2n.melody.domain.Post;
import cn.linj2n.melody.web.dto.PostDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DTOModelMapper {

    private ModelMapper modelMapper;

    @Autowired
    public DTOModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public PostDTO convertToDTO(Post post) {
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
        return postDTO;
    }

    public Post convertToEntity(PostDTO postDTO) {
        return modelMapper.map(postDTO, Post.class);
    }
}
