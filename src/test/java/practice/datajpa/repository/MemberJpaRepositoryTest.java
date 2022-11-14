package practice.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import practice.datajpa.entity.Member;
import practice.datajpa.entity.Team;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository repository;
    @Autowired
    TeamRepository teamRepository;

    @Test
    void save() {
        Member member = new Member("memberA");
        Member save = repository.save(member);
        Member member1 = repository.find(save.getId());
        List<Member> members = repository.findbyUsername_querydsl(save.getUsername());

        assertThat(save.getUsername()).isEqualTo(member1.getUsername());
        assertThat(save.getUsername()).isEqualTo(members.get(0).getUsername());
    }

    @Test
    void paging() {
        Team teamC = new Team("teamC");
        Team teamD = new Team("teamD");
        teamRepository.save(teamC);
        teamRepository.save(teamD);
        Member member1 = new Member("username1", 50, teamC);
        Member member2 = new Member("username2", 60, teamC);
        Member member3 = new Member("username3", 70, teamD);
        Member member4 = new Member("username4", 70, teamD);
        Member save1 = repository.save(member1);
        Member save2 = repository.save(member2);
        Member save3 = repository.save(member3);
        Member save4 = repository.save(member4);

        int age = 70;
        int offset = 0;
        int limit =2;

        List<Member> members = repository.findByPage(age, offset, limit);
        long count = repository.totalCount(age);

        assertThat(members.size()).isEqualTo(2);
        assertThat(count).isEqualTo(2);

    }

    @Test
    void bulkAge() {
        Member member1 = new Member("username1", 50, null);
        Member member2 = new Member("username2", 60, null);
        Member member3 = new Member("username3", 70, null);
        Member member4 = new Member("username4", 70, null);
        Member save1 = repository.save(member1);
        Member save2 = repository.save(member2);
        Member save3 = repository.save(member3);
        Member save4 = repository.save(member4);

        int resultCnt = repository.bulkAge(60);

        assertThat(resultCnt).isEqualTo(3);
    }
}