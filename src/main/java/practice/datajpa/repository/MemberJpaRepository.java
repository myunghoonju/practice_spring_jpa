package practice.datajpa.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import practice.datajpa.dto.MemberSearchCondition;
import practice.datajpa.dto.MemberTeamDto;
import practice.datajpa.dto.QMemberTeamDto;
import practice.datajpa.entity.Member;
import practice.datajpa.entity.QMember;
import practice.datajpa.entity.QTeam;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static org.springframework.util.StringUtils.hasText;
import static practice.datajpa.entity.QMember.member;
import static practice.datajpa.entity.QTeam.team;

@Repository
public class MemberJpaRepository {

    @PersistenceContext
    private EntityManager em;
    JPAQueryFactory queryFactory;

    public MemberJpaRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Transactional
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    @Transactional
    public Member find(Long id) {
        return em.find(Member.class, id);
    }

    @Transactional
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(em.find(Member.class, id));
    }

    @Transactional
    public long count(Long id) {
        return em.createQuery("select count(m) from Member as m", Long.class).getSingleResult();
    }

    @Transactional
    public List<Member> findAll(Long id) {
        return em.createQuery("select m from Member as m", Member.class).getResultList();
    }

    @Transactional
    public void delete(Long id) {
        em.remove(id);
    }

    @Transactional
    public List<Member> findByPage(int age, int offset, int limit) {
        return em.createQuery("select m from Member as m where m.age = :age order by m.username desc")
                .setParameter("age", age)
                .setFirstResult(offset) // where to get?
                .setMaxResults(limit) // how many get?
                .getResultList();
    }

    @Transactional
    public long totalCount(int age) {
        return em.createQuery("select count(m) from Member as m where m.age = :age", Long.class)
                .setParameter("age", age)
                .getSingleResult();
    }

    @Transactional
    public int bulkAge(int age) {
        return em.createQuery("update Member as m set m.age = m.age +1 where m.age >= :age")
                .setParameter("age", age)
                .executeUpdate();
    }

    public List<MemberTeamDto> searchByBuilder(MemberSearchCondition condition) {
        BooleanBuilder builder = new BooleanBuilder();
        if (hasText(condition.getUsername())) {
            builder.and(member.username.eq(condition.getUsername()));
        }
        if (hasText(condition.getTeamname())) {
            builder.and(team.name.eq(condition.getTeamname()));
        }
        if (condition.getAgeGoe() != null) {
            builder.and(member.age.goe(condition.getAgeGoe()));
        }
        if (condition.getAgeLoe() != null) {
            builder.and(member.age.loe(condition.getAgeLoe()));
        }

        return queryFactory
                .select(new QMemberTeamDto(
                        member.id.as("memberId"),
                        member.username,
                        member.age,
                        team.id.as("teamId"),
                        team.name.as("teamname")))
                .from(member)
                .leftJoin(member.team, team)
                .where(builder)
                .fetch();
    }

}
