package practice.datajpa.entity.composite;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Composite {

    @EmbeddedId
    /*    @AttributeOverrides({
            @AttributeOverride(name = "author", column = @Column(name = "지은이")),
            @AttributeOverride(name = "company", column = @Column(name = "소속사"))
    })*/
    private CompositeId id;
    private String gender;
}
