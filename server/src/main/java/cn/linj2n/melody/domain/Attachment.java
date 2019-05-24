package cn.linj2n.melody.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;

@Entity
@Table(name = "attachment")
@Getter
@Setter
public class Attachment {

    /**
     * 附件的主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 附件的名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 附件的描述信息
     */
    @Column(name = "description")
    private String description;


    /**
     * 附件对应七牛云的元数据信息
     */
    @OneToOne(mappedBy = "attachment", cascade = CascadeType.ALL,orphanRemoval = true,
            fetch = FetchType.LAZY, optional = false)
    private QiniuFile qiniuFile;

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("description", description)
                .toString();
    }
}
