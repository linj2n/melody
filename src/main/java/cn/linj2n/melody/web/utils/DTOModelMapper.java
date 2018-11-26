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
        PostDTO postDTO = modelMapper.map(post,PostDTO.class);
        postDTO.setCreatedAt(post.getCreatedAt().toLocalDateTime());
        postDTO.setUpdatedAt(post.getUpdatedAt().toLocalDateTime());
        return postDTO;
    }

    public Post convertToEntity(PostDTO postDTO) {
        return modelMapper.map(postDTO, Post.class);
    }
}
