package com.j23.server.repos.qna;

import com.j23.server.models.qna.QnA;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface QnaRepository extends JpaRepository<QnA, UUID> {

//  Page<QnA> findAll
}
