package cn.linj2n.melody.domain.webdataanalysis;

import cn.linj2n.melody.domain.Post;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Entity
@Table(name = "resource_uv")
@Getter
@Setter
public class ResourceUniqueVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Temporal(TemporalType.DATE)
    Date date;

    @Column(name = "count")
    private Long count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id")
    private Post post;

    @Column(name = "updated_at",nullable = false)
    private ZonedDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        updatedAt = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
    }

    public ResourceUniqueVisit() {

    }


    public ResourceUniqueVisit(String name, Long count, Post post, Date date) {
        this.name = name;
        this.date = date;
        this.count = count;
        this.post = post;
    }
}
