package cn.linj2n.melody.domain.webdataanalysis;

import cn.linj2n.melody.domain.Post;
import cn.linj2n.melody.utils.DateUtil;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Entity
@Table(name = "resource_pv")
@Getter
@Setter
public class ResourceView {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "date")
    private ZonedDateTime date;

    @Column(name = "count")
    private Long count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id", nullable = true)
    private Post post;

    @Column(name = "updated_at",nullable = false)
    private ZonedDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        updatedAt = DateUtil.nowDateTime();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = DateUtil.nowDateTime();
    }


    public ResourceView() {
    }

    public ResourceView(String name, ZonedDateTime date, Long count) {
        this.name = name;
        this.date = date;
        this.count = count;
    }

    public ResourceView(String name, ZonedDateTime date, Long count, Post post) {
        this.name = name;
        this.date = date;
        this.count = count;
        this.post = post;
    }
}
