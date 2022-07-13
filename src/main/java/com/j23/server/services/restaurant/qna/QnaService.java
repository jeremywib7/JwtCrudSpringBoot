package com.j23.server.services.restaurant.qna;

import com.j23.server.models.qna.QnA;
import com.j23.server.repos.qna.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class QnaService {

  private final QnaRepository qnaRepository;

  public QnA addQna(QnA qnA) {
    return qnaRepository.save(qnA);
  }

  public QnA updateQna(QnA qnA) {
    return qnaRepository.save(qnA);
  }

  public Page<QnA> getAllQna() {
//    qnaRepository.findAll(PageRequest.of(page, size));
    return null;
  }
}
