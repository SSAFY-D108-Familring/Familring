package com.familring.questionservice.domain;

import com.familring.questionservice.dto.request.QuestionAnswerCreateRequest;
import com.familring.questionservice.dto.request.QuestionAnswerUpdateRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@Table(name = "question_answer")
public class QuestionAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="question_answer_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_family_id")
    private QuestionFamily questionFamily;

    @Column(name="user_id")
    private Long userId;

    @Column(name="question_answer")
    private String answer;

    @Column(name="question_created_at")
    private LocalDate createdAt;

    @Column(name="question_modified_at")
    private LocalDate modifiedAt;

    public void updateQuestionAnswer(QuestionAnswerUpdateRequest questionAnswerUpdateRequest) {
        this.answer = questionAnswerUpdateRequest.getContent();
        this.modifiedAt = questionAnswerUpdateRequest.getModifiedAt();
    }

}
