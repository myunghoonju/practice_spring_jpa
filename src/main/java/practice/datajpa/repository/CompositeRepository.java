package practice.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.datajpa.entity.composite.Composite;
import practice.datajpa.entity.composite.CompositeId;

public interface CompositeRepository extends JpaRepository<Composite, CompositeId>,
                                             CompositeRepositoryCustom {
}
