package com.j23.server.services;

import com.j23.server.exception.UserNotFoundException;
import com.j23.server.models.Member;
import com.j23.server.repos.MemberRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MemberService {
    private final MemberRepo memberRepo;

    @Autowired
    public MemberService(MemberRepo memberRepo) {
        this.memberRepo = memberRepo;
    }

    public Member addMember(Member member) {
        member.setMemberCode(String.valueOf(UUID.randomUUID()));
        return memberRepo.save(member);
    }

    public List<Member> findAllMembers(int pageNo, int pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<Member> pagedResults = memberRepo.findAll(paging);
        return pagedResults.toList();
//        return memberRepo.findAll();
    }

    public Member updateMember(Member member) {
        return memberRepo.save(member);
    }

    public Member findMemberById(Long id) {
        return memberRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User by id " + id + " was not found"));
    }

    public void deleteMember(Long id) {
        memberRepo.deleteById(id);
    }
}
