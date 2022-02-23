package practice.datajpa.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import practice.datajpa.dto.MemberDto;
import practice.datajpa.entity.Member;
import practice.datajpa.entity.Team;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository repository;
    @Autowired
    TeamRepository teamRepository;

    @Test
    void save() {
        Member member = new Member("memberA");
        Member save = repository.save(member);
        Member member1 = repository.findById(save.getId()).orElse(null);

        assertThat(save.getUsername()).isEqualTo(member1.getUsername());
    }

    @Test
    void queryMethodTest() {
        List<Member> result = repository.findByUsernameAndAgeGreaterThan("memberA", 10);

        for (Member m : result) {
            log.info(m.getUsername());
        }
    }

    @Test
    void dtoTest() {
        Team teamA = new Team("teamA");
        teamRepository.save(teamA);
        Member member = new Member("memberA");
        member.setTeam(teamA);
        Member save = repository.save(member);

        List<MemberDto> dtos = repository.findMemberDto();

        for (MemberDto dto : dtos) {
            log.info("dto id: {}", dto.getId());
        }
    }


    @Test
    void paramBindingTest() {
        Member membera = new Member("memberA");
        Member memberb = new Member("memberB");
        Member savea = repository.save(membera);
        Member saveb = repository.save(memberb);

        List<Member> result = repository.findNames(Arrays.asList("memberA", "memberB"));

        for (Member member : result) {
            log.info("name {}", member.getUsername());
        }
    }
}