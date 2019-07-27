package cn.linj2n.melody.domain.webdataanalysis;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "resource_url")
public class ResourceUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

}
