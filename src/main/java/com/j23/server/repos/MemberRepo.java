package com.j23.server.repos;

import com.j23.server.models.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepo extends JpaRepository<Member, Long> {

    void deleteMemberById(Long id);

    Optional<Member> findMemberById(Long id);
}
