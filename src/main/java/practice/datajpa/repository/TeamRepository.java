package practice.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.datajpa.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {

}
