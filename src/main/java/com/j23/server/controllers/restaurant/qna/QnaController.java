package com.j23.server.controllers.restaurant.qna;

import com.j23.server.configuration.ResponseHandler;
import com.j23.server.models.qna.QnA;
import com.j23.server.services.restaurant.qna.QnaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/qna")
public class QnaController {

  @Autowired
  private QnaService qnaService;

  @PostMapping("/add")
  public ResponseEntity<Object> addQna(@RequestBody QnA qnA) {
    return ResponseHandler.generateResponse("Successfully add qna!", HttpStatus.CREATED,
      qnaService.addQna(qnA));
  }
}
