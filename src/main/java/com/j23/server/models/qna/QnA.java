package com.j23.server.models.qna;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.j23.server.models.utils.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
//@EqualsAndHashCode(callSuper = true) // for extends base model
//@SQLDelete(sql = "UPDATE qna a SET a.deleted_on = CURRENT_TIMESTAMP, number = null  WHERE id=?")
//@Where(clause = "deleted_on is null")
public class QnA {
  @Id
  @Type(type = "uuid-char")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Min(value = 0)
  @Column()
  private Integer number;

  @Column(length = 100, nullable = false)
  @NotBlank(message = "Question is mandatory")
  private String question;

  @Column(length = 200, nullable = false)
  @NotBlank(message = "Answer is mandatory")
  private String answer;

  @CreationTimestamp
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @Column(name = "date_created", updatable = false)
  private LocalDateTime createdOn;

  @UpdateTimestamp
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @Column(name = "last_updated")
  private LocalDateTime updatedOn;
}
