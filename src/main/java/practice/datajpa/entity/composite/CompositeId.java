package practice.datajpa.entity.composite;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class CompositeId implements Serializable {

    @Column(name = "지은이")
    private String author;

    @Column(name = "소속사")
    private String company;
}
