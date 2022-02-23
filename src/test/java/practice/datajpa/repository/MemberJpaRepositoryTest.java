package practice.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import practice.datajpa.entity.Member;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository repository;

    @Test
    void save() {
        Member member = new Member("memberA");
        Member save = repository.save(member);
        Member member1 = repository.find(save.getId());

        assertThat(save.getUsername()).isEqualTo(member1.getUsername());
    }

    @Test
    void find() {
    }
}