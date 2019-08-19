package cn.linj2n.melody.domain.webdataanalysis;

import cn.linj2n.melody.domain.Post;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Entity
@Table(name = "resource_view")
@Getter
@Setter
public class ResourceView {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Temporal(TemporalType.DATE)
    Date date;

    @Column(name = "count")
    private Long count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id", nullable = true)
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

    public ResourceView() {
    }

    public ResourceView(String name, Long count, Post post, Date date) {
        this.name = name;
        this.date = date;
        this.count = count;
        this.post = post;
    }
}
