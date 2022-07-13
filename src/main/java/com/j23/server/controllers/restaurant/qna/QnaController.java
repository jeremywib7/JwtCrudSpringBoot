package com.j23.server.controllers.restaurant.qna;

import com.j23.server.configuration.ResponseHandler;
import com.j23.server.models.qna.QnA;
import com.j23.server.services.restaurant.qna.QnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("v1/qna")
@RequiredArgsConstructor
public class QnaController {

  private final QnaService qnaService;

  @PostMapping("/add")
  public ResponseEntity<Object> addQna(@Valid @RequestBody QnA qnA) {
    return ResponseHandler.generateResponse("Successfully add qna!", HttpStatus.CREATED,
      qnaService.addQna(qnA));
  }

  @PutMapping("/update")
  public ResponseEntity<Object> updateQna(@RequestBody QnA qnA) {
    return ResponseHandler.generateResponse("Successfully update qna!", HttpStatus.OK,
            qnaService.addQna(qnA));
  }
}
