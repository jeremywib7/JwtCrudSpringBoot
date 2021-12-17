package com.j23.server.repos;

import com.j23.server.models.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepo extends JpaRepository<Member, Long> {

    void deleteMemberById(Long id);

    Member findMemberById(Long id);
}
