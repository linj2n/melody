package cn.linj2n.melody.domain.webdataanalysis;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Entity
@Table(name = "resource_visit")
@Getter
@Setter
public class ResourceVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Temporal(TemporalType.DATE)
    @Column(name = "date")
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
