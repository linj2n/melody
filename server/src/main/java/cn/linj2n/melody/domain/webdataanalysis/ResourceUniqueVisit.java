package cn.linj2n.melody.domain.webdataanalysis;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "resource_id")
    private ResourceMeta resource;

    @Column(name = "updated_at",nullable = false)
    private ZonedDateTime updatedAt;

    @PreUpdate
    public void preUpdate() {
        updatedAt = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
    }
}
