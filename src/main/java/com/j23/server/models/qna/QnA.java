package com.j23.server.models.qna;

import com.j23.server.models.utils.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true) // for extends base model
@SQLDelete(sql = "UPDATE qna a SET a.deleted_on = CURRENT_TIMESTAMP, number = null  WHERE id=?")
@Where(clause = "deleted_on is null")
public class QnA extends BaseModel {
  @Id
  @Type(type = "uuid-char")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Min(value = 0)
  @Column()
  private Integer number;

  @Column(length = 100, unique = true, nullable = false)
  @NotBlank(message = "Question is mandatory")
  private String question;

  @Column(length = 200, nullable = false)
  @NotBlank(message = "Answer is mandatory")
  private String answer;
}
