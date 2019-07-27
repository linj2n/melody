package cn.linj2n.melody.domain.webdataanalysis;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity
@Table(name = "resource_visit")
@Getter
@Setter
public class ResourceVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    private ResourceUrl resourceUrl;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;


    @Column(name = "updated_at",nullable = false)
    private ZonedDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
    }


    @PreUpdate
    public void preUpdate() {
        updatedAt = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
    }

}
