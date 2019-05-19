package cn.linj2n.melody.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "attach")
public class Attach {

    @Id
    private Long id;
    private String name;
    private String type;
    private String description;
}
