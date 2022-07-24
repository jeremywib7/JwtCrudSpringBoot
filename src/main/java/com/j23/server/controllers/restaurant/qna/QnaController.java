package com.j23.server.controllers.restaurant.qna;

import com.j23.server.configuration.ResponseHandler;
import com.j23.server.models.qna.QnA;
import com.j23.server.services.restaurant.qna.QnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

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
  public ResponseEntity<Object> updateQna(@Valid @RequestBody QnA qnA) {
    return ResponseHandler.generateResponse("Successfully update qna!", HttpStatus.OK,
      qnaService.updateQna(qnA));
  }

  @DeleteMapping("/delete")
  public ResponseEntity<Object> deleteQna(@RequestParam UUID id) {
    qnaService.deleteQna(id);
    return ResponseHandler.generateResponse("Successfully delete qna!", HttpStatus.OK,
      null);
  }

  @GetMapping("/find-all")
  public ResponseEntity<Object> findAllQna(
    @RequestParam(required = false) String searchKeyword,
    @RequestParam(defaultValue = "question") String sortedFieldName,
    @RequestParam(defaultValue = "1") int order,
    @RequestParam(defaultValue = "0") Integer page,
    @RequestParam(defaultValue = "10") Integer size
  ) {
    return ResponseHandler.generateResponse("Successfully find all qna!", HttpStatus.OK,
      qnaService.findAllQna(searchKeyword, page, size, sortedFieldName, order));
  }

}
