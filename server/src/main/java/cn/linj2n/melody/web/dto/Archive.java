package cn.linj2n.melody.web.dto;

import cn.linj2n.melody.domain.Post;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Archive {

    private String name;

    private List<PostDTO> postDTOs = new ArrayList<>();

    public void addPost(PostDTO postDTO) {
        this.postDTOs.add(postDTO);
    }

    public Archive() {

    }
    public Archive(String name) {
        this.name = name;
    }

    public Archive(String name, List<PostDTO> postDTOs) {
        this.name = name;
        this.postDTOs = postDTOs;
    }
}
