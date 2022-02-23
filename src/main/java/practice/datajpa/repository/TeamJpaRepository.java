package practice.datajpa.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import practice.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class TeamJpaRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Team save(Team team) {
        em.persist(team);
        return team;
    }

    @Transactional
    public Team find(Long id) {
        return em.find(Team.class, id);
    }

    @Transactional
    public Optional<Team> findById(Long id) {
        return Optional.ofNullable(em.find(Team.class, id));
    }

    @Transactional
    public long count(Long id) {
        return em.createQuery("select count(m) from Team as m", Long.class).getSingleResult();
    }

    @Transactional
    public List<Team> findAll(Long id) {
        return em.createQuery("select m from Team as m", Team.class).getResultList();
    }

    @Transactional
    public void delete(Long id) {
        em.remove(id);
    }
}
