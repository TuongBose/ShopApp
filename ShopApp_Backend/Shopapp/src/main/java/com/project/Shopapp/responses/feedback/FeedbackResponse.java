package com.project.Shopapp.responses.feedback;

import com.project.Shopapp.models.Feedback;
import com.project.Shopapp.responses.account.AccountResponse;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedbackResponse {
    private int FEEDBACKID;
    private AccountResponse accountResponse;
    private String NOIDUNG;
    private int SOSAO;
    private int MASANPHAM;
    private LocalDateTime createdAt;

    public static FeedbackResponse fromFeedback(Feedback feedback)
    {
        return FeedbackResponse.builder()
                .FEEDBACKID(feedback.getFEEDBACKID())
                .accountResponse(AccountResponse.fromAccount(feedback.getUSERID()))
                .NOIDUNG(feedback.getNOIDUNG())
                .SOSAO(feedback.getSOSAO())
                .MASANPHAM(feedback.getMASANPHAM().getMASANPHAM())
                .createdAt(feedback.getCreatedAt())
                .build();
    }
}
