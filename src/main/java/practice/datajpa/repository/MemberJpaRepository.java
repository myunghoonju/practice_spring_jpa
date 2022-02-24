package practice.datajpa.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import practice.datajpa.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberJpaRepository {

    @PersistenceContext
    private EntityManager em;

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

}
