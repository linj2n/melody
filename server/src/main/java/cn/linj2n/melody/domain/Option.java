package cn.linj2n.melody.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "config")
@Getter
@Setter
public class Option {

    @Id
    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private String value;

    public Option() {
    }

    public Option(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
