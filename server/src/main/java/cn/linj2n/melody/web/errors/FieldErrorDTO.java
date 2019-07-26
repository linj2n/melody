package cn.linj2n.melody.web.errors;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class FieldErrorDTO implements Serializable {

    private final String field;

    private final String detail;

    public FieldErrorDTO(String field, String detail) {
        this.field = field;
        this.detail = detail;
    }
}
