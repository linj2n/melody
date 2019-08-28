package cn.linj2n.melody.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class TrafficEntryDTO {

    private LocalDate bucket;

    private Long count;

    private Long uniques;

    public TrafficEntryDTO() {
    }

    public TrafficEntryDTO(LocalDate bucket, Long count) {
        this.bucket = bucket;
        this.count = count;
    }

    public TrafficEntryDTO(LocalDate bucket, Long count, Long uniques) {
        this.bucket = bucket;
        this.count = count;
        this.uniques = uniques;
    }
}
