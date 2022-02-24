package practice.datajpa.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import practice.datajpa.dto.MemberDto;
import practice.datajpa.entity.Member;
import practice.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Rollback(value = false)
@Transactional
@Slf4j
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository repository;
    @Autowired
    TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;

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
        repository.save(member);

        List<MemberDto> dtos = repository.findMemberDto();

        for (MemberDto dto : dtos) {
            log.info("dto id: {}", dto.getId());
        }
    }

    @Test
    void paramBindingTest() {
        Member membera = new Member("memberA");
        Member memberb = new Member("memberB");
        repository.save(membera);
        repository.save(memberb);

        List<Member> result = repository.findNames(Arrays.asList("memberA", "memberB"));

        for (Member member : result) {
            log.info("name {}", member.getUsername());
        }
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
        repository.save(member1);
        repository.save(member2);
        repository.save(member3);
        repository.save(member4);

        PageRequest pageRequest = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "username"));

        Page<Member> page = repository.findByAge(70, pageRequest);
        Page<MemberDto> toDto = page.map(m -> new MemberDto(m.getId(), m.getUsername(), null));

        List<Member> members = page.getContent();
        long totalCount = page.getTotalElements();
        int pageNumber = page.getNumber();
        boolean isFirstPage = page.isFirst();
        boolean isLastPage = page.hasNext();
        int totalPages = page.getTotalPages();

        for (Member m : members) {
            log.info(m.getUsername());
        }

        log.info("total cnt {}", totalCount);
        log.info("pageNumber {}", pageNumber);
        log.info("isFirstPage {}", isFirstPage);
        log.info("isLastPage {}", isLastPage);
        log.info("totalPages {}", totalPages);
    }

    @Test
    void bulkUpdate() {
        Member member1 = new Member("username1", 50, null);
        Member member2 = new Member("username2", 60, null);
        Member member3 = new Member("username3", 70, null);
        Member member4 = new Member("username4", 70, null);
        Member save1 = repository.save(member1);
        Member save2 = repository.save(member2);
        Member save3 = repository.save(member3);
        Member save4 = repository.save(member4);

        int resultCnt = repository.bulkAgePlus(60);

        // asynchronous persistence
        List<Member> all = repository.findAll();
        for (Member m : all) {
            log.info("name, age {} {}", m.getUsername(), m.getAge());
        }

        // sync with persistence or  @Modifying(clearAutomatically = true)
       /*
        em.flush();
        em.clear();
        List<Member> all2 = repository.findAll();
        for (Member m2 : all2) {
            log.info("name, age {} {}", m2.getUsername(), m2.getAge());
        }
        */

        assertThat(resultCnt).isEqualTo(3);
    }

    @Test
    @DisplayName("N + 1 problem generated without using @EntityGraph(attributePaths = {\"team\"})")
    void findMemberWithLazy() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamA);
        repository.save(member1);
        repository.save(member2);

        em.flush();
        em.clear();
        List<Member> all2 = repository.findAll();
        for (Member m2 : all2) {
            log.info("name, age {} {}", m2.getUsername(), m2.getAge());
            log.info("team class:: {}", m2.getTeam().getClass().toString());
            log.info("team name:: {}", m2.getTeam().getName());
        }
    }

    @Test
    @DisplayName("solve above")
    void fetchTest() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamA);
        repository.save(member1);
        repository.save(member2);

        em.flush();
        em.clear();
        List<Member> all2 =  repository.findMemberFetchJoin();
        for (Member m2 : all2) {
            log.info("name, age {} {}", m2.getUsername(), m2.getAge());
            log.info("team class:: {}", m2.getTeam().getClass().toString());
            log.info("team name:: {}", m2.getTeam().getName());
        }
    }

    @Test
    void findByUsernameWithEntityGraph() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamA);
        repository.save(member1);
        repository.save(member2);

        em.flush();
        em.clear();
        List<Member> members = repository.findByUsername(member1.getUsername());

        for (Member m2 : members) {
            log.info("name, age {} {}", m2.getUsername(), m2.getAge());
            log.info("team class:: {}", m2.getTeam().getClass().toString());
            log.info("team name:: {}", m2.getTeam().getName());
        }
    }

    @Test
    void customRepoTest() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamA);
        repository.save(member1);
        repository.save(member2);

        em.flush();
        em.clear();
        List<Member> all2 =  repository.findMemberCustom();
        for (Member m2 : all2) {
            log.info("name, age {} {}", m2.getUsername(), m2.getAge());
            log.info("team class:: {}", m2.getTeam().getClass().toString());
            log.info("team name:: {}", m2.getTeam().getName());
        }
    }
}