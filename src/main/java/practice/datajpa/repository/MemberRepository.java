package practice.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import practice.datajpa.dto.MemberDto;
import practice.datajpa.entity.Member;

import java.util.Collection;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    @Query("select m from Member as m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member as m")
    List<String> findUserNameList();

    @Query("select new practice.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member as m join m.team as t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member as m where m.username in :username")
    List<Member> findNames(@Param("username") Collection<String> username);

    @Query(value = "select m from Member as m left join m.team as t",
           countQuery = "select count(m) from Member as m")
    Page<Member> findByAge(int age, Pageable pageable);

    @Modifying(clearAutomatically = true) // same as .executeUpdate();
    @Query("update Member as m set m.age = m.age +1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member as m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    //below uses fetch
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    //below uses fetch
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member as m")
    List<Member> findMemberWithEntityGraph();

    //below uses fetch
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member as m where m.username = :username")
    List<Member> findByUsername(@Param("username") String username);
}
