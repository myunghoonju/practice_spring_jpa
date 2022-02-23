package practice.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.datajpa.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
