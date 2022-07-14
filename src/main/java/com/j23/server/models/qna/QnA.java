package com.j23.server.models.qna;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QnA {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Min(value = 0)
  @Column(unique = true, nullable = false)
  private Integer number;

  @Column(length = 100, unique = true, nullable = false)
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
