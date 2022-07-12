package com.j23.server.repos.qna;

import com.j23.server.models.qna.QnA;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnaRepository extends JpaRepository<QnA,String> {

//  Page<QnA> findAll
}
