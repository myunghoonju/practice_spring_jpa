package practice.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import practice.datajpa.dto.MemberDto;
import practice.datajpa.dto.MemberSearchCondition;
import practice.datajpa.dto.MemberTeamDto;
import practice.datajpa.entity.Member;
import practice.datajpa.entity.composite.Composite;
import practice.datajpa.repository.CompositeRepository;
import practice.datajpa.repository.MemberJpaRepository;
import practice.datajpa.repository.MemberRepository;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberRepository repository;
    private final MemberJpaRepository jpaRepository;
    private final CompositeRepository compositeRepository;

    @GetMapping("/members")
    public Page<MemberDto> list(@PageableDefault(size = 5) Pageable pageable) {
        Page<Member> members = repository.findAll(pageable);
        Page<MemberDto> mappedMembers = members.map(MemberDto::new);
        return mappedMembers;
    }

    @GetMapping("/v1/members")
    public List<MemberTeamDto> searchMemberV1(MemberSearchCondition cond) {
        return jpaRepository.search_with_params(cond);
    }

    @GetMapping("/composite")
    public String getComposite() {
        return compositeRepository.findUniqueAuthor("C");
    }

    @PostMapping("/composite")
    public Composite saveComposite(@RequestBody Composite composite) {
        return compositeRepository.save(composite);
    }
}
