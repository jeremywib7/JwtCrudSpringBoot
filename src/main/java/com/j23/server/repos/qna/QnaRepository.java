package com.j23.server.repos.qna;

import com.j23.server.models.qna.QnA;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Repository
public interface QnaRepository extends JpaRepository<QnA, UUID> {

  boolean existsByQuestion(@NotBlank(message = "Question is mandatory") String question);

  boolean existsByQuestionAndIdIsNot(@NotBlank(message = "Question is mandatory") String question, UUID id);

  QnA findTopByOrderByCreatedOnDesc();

  Page<QnA> findAllByQuestionContaining(@NotBlank(message = "Question is mandatory") String question, Pageable pageable);
}
