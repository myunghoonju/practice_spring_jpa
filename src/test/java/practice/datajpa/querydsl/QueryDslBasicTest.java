package practice.datajpa.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import practice.datajpa.entity.Member;
import practice.datajpa.entity.QMember;
import practice.datajpa.entity.Team;

import javax.persistence.EntityManager;
import java.util.List;

import static com.querydsl.jpa.JPAExpressions.select;
import static org.assertj.core.api.Assertions.assertThat;
import static practice.datajpa.entity.QMember.member;

@SpringBootTest
@Transactional
public class QueryDslBasicTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }

    @Test
    void subQuery() {
        QMember memberSub = new QMember("memberSub");

        List<Member> memberList = queryFactory
                .selectFrom(member)
                .where(member.age.eq(
                        select(memberSub.age.max())
                                .from(memberSub)
                ))
                .fetch();

        assertThat(memberList).extracting("age").containsExactly(40);
    }

    @Test
    void selectSubQuery() {
        QMember memberSub = new QMember("memberSub");

        List<Tuple> tupleList = queryFactory
                .select(member.username,
                        select(memberSub.age.avg())
                                .from(memberSub))
                .from(member)
                .fetch();

        for (Tuple t : tupleList) {
            System.out.println(t);
        }
    }

    @Test
    void basicCase() {
        List<String> cases = queryFactory
                .select(member.age
                        .when(10).then("ten")
                        .when(20).then("twenty")
                        .otherwise("etc"))
                .from(member)
                .fetch();

        for (String c : cases) {
            System.out.println(c);
        }
    }

    @Test
    void complexCase() {
        List<String> ages = queryFactory
                .select(new CaseBuilder()
                        .when(member.age.between(0, 20)).then("0~20")
                        .when(member.age.between(21, 30)).then("21~30")
                        .otherwise("etc"))
                .from(member)
                .fetch();

        for (String a : ages) {
            System.out.println(a);
        }
    }

    @Test
    void constant() {
        List<Tuple> a = queryFactory
                .select(member.username, Expressions.constant("a"))
                .from(member)
                .fetch();

        for (Tuple aa : a) {
            System.out.println(aa);
        }
    }

    @Test
    void concat() {
        List<String> ages = queryFactory
                .select(member.username.concat("_").concat(member.age.stringValue()))
                .from(member)
                .where(member.username.eq("member1"))
                .fetch();

        for (String a : ages) {
            System.out.println(a);
        }
    }

    @Test
    void dynamicQueryBooleanBuilder() {
        String userNameParam = "member1";
        Integer ageParam = null;

        List<Member> result = searchMember1(userNameParam, ageParam);

        for (Member r : result) {
            System.out.println(r.getUsername());
        }
    }

    private List<Member> searchMember1(String userNameCond, Integer ageCond) {
        BooleanBuilder builder = new BooleanBuilder();
        if (!ObjectUtils.isEmpty(userNameCond)) {
            builder.and(member.username.eq(userNameCond));
        }

        if (!ObjectUtils.isEmpty(ageCond)) {
            builder.and(member.age.eq(ageCond));
        }

        return queryFactory
                .selectFrom(member)
                .where(builder)
                .fetch();
    }

    @Test
    void dynamicQueryWhereParam() {
        String userNameParam = "member1";
        Integer ageParam = 10;

        List<Member> result = searchMember2(userNameParam, ageParam);

        for (Member r : result) {
            System.out.println(r.getUsername());
        }
    }

    private List<Member> searchMember2(String userNameCond, Integer ageCond) {
        return queryFactory
                .selectFrom(member)
                //.where(usernameEq(userNameCond), ageEq(ageCond))
                .where(allEq(userNameCond, ageCond))
                .fetch();
    }

    private BooleanExpression ageEq(Integer ageCond) {
        return ObjectUtils.isEmpty(ageCond) ? null : member.age.eq(ageCond);
    }

    private BooleanExpression usernameEq(String userNameCond) {
        return ObjectUtils.isEmpty(userNameCond) ? null : member.username.eq(userNameCond);
    }

    private BooleanExpression allEq(String userNameCond, Integer ageCond) {
        return usernameEq(userNameCond).and(ageEq(ageCond));
    }
}
