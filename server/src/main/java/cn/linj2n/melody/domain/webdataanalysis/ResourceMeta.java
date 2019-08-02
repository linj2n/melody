package cn.linj2n.melody.domain.webdataanalysis;

import cn.linj2n.melody.domain.webdataanalysis.enumeration.ResourceType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "resource")
@Getter
@Setter
public class ResourceMeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "date")
    Date date;

    @Column(name = "name")
    private String name;

    @Column(name = "url")
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ResourceType resourceType;
}
