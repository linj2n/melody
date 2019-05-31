package cn.linj2n.melody.web.dto;

import cn.linj2n.melody.domain.QiniuFile;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttachmentDTO {
    private Long id;
    private String name;
    private String description;
    @JsonProperty("qiniuFile")
    private QiniuFileDTO qiniuFileDTO;
}
