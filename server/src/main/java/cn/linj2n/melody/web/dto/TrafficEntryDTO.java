package cn.linj2n.melody.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class TrafficItemDTO {

    private Date bucket;

    private Long count;

    private Long uniques;

    public TrafficItemDTO() {
    }

    public TrafficItemDTO(Date bucket, Long count) {
        this.bucket = bucket;
        this.count = count;
    }

    public TrafficItemDTO(Date bucket, Long count, Long uniques) {
        this.bucket = bucket;
        this.count = count;
        this.uniques = uniques;
    }
}
