package cn.linj2n.melody.core;

import javax.persistence.MappedSuperclass;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@MappedSuperclass
public abstract class AbstractModel implements Comparable<AbstractModel>, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Override
    public int compareTo(AbstractModel o) {
        return this.getId().compareTo(o.getId());
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AbstractModel model = (AbstractModel) o;

        if (!Objects.equals(model.getId(), this.id)) return false;

        return true;
    }

    public int hashCode() {
        return Objects.hashCode(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}

