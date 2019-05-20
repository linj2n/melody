package cn.linj2n.melody.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "attachment")
public class Attachment {

    @Id
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "store_name")
    private String storeName;

    @Column(name = "hash")
    private String hash;

    @Column(name = "type")
    private String type;

    @Column(name = "description")
    private String description;
}
