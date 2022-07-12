package com.j23.server.services.restaurant.qna;

import com.j23.server.models.qna.QnA;
import com.j23.server.repos.qna.QnaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class QnaService {

  QnaRepository qnaRepository;

  public Page<QnA> getAllQna() {
    qnaRepository.findAll(PageRequest.of(page, size));
    return null;
  }
}
