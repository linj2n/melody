package cn.linj2n.melody.web.dto;

import cn.linj2n.melody.domain.Post;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Archive {

    private String name;

    private List<PostDTO> postDTOs = new ArrayList<>();

    public void addPost(PostDTO postDTO) {
        this.postDTOs.add(postDTO);
    }
}
