package cn.linj2n.melody.web.utils;

import cn.linj2n.melody.domain.Authority;
import cn.linj2n.melody.domain.Post;
import cn.linj2n.melody.domain.User;
import cn.linj2n.melody.web.dto.PostDTO;
import cn.linj2n.melody.web.dto.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

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
//        postDTO.setContent(viewUtils.renderToHtml(post.getContent()));
        postDTO.setContent(post.getContent());
        return postDTO;
    }

    public Post convertToEntity(PostDTO postDTO) {
        Post post = modelMapper.map(postDTO, Post.class);
//        post.setTitle(post.getTitle().isEmpty() ? "Untitled" : post.getTitle());
        return post;
    }

    public UserDTO convertToDTO(final User user) {
        if (user == null) {
            return null;
        }
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.setAuthorities(user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet()));
        System.out.println(userDTO);
        return userDTO;
    }

}
