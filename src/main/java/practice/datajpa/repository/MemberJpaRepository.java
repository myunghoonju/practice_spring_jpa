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



}
