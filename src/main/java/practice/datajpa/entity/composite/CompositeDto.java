package practice.datajpa.entity.composite;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CompositeDto {

    private String author;

    public CompositeDto(String author) {
        this.author = author;
    }
}
