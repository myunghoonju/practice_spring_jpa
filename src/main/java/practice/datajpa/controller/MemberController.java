package practice.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import practice.datajpa.dto.MemberDto;
import practice.datajpa.entity.Member;
import practice.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberRepository repository;

    @GetMapping("/members")
    public Page<MemberDto> list(@PageableDefault(size = 5) Pageable pageable) {
        Page<Member> members = repository.findAll(pageable);
        Page<MemberDto> mappedMembers = members.map(MemberDto::new);
        return mappedMembers;
    }
/*

    @PostConstruct
    public void init() {
        for (int i = 0; i < 100; i++) {
            repository.save(new Member("user_" + i));
        }
    }
*/
}
