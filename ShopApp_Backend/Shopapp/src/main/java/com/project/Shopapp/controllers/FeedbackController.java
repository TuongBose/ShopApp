package com.project.Shopapp.controllers;

import com.project.Shopapp.dtos.FeedbackDTO;
import com.project.Shopapp.exceptions.DataNotFoundException;
import com.project.Shopapp.models.Account;
import com.project.Shopapp.models.Feedback;
import com.project.Shopapp.responses.FeedbackResponse;
import com.project.Shopapp.services.feedback.IFeedbackService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {
    private final IFeedbackService feedbackService;

    @GetMapping("")
    public ResponseEntity<?> getAllFeedbacks(
            @RequestParam(value = "user_id", required = false) Integer userId,
            @RequestParam(value = "product_id") Integer productId
    ) {
        try {
            List<FeedbackResponse> feedbackResponses;
            if (userId == null) {
                feedbackResponses = feedbackService.getFeedbacksByProduct(productId);
            } else {
                feedbackResponses = feedbackService.getFeedbacksByAccountAndProduct(userId, productId);
            }
            return ResponseEntity.ok(feedbackResponses);
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> updateFeedback(
            @PathVariable int id,
            @RequestBody FeedbackDTO feedbackDTO
    ) {
        try {
            Account loginAccount = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (loginAccount.getUSERID() != feedbackDTO.getUSERID()) {
                return ResponseEntity.badRequest().body("You can not update feedback as another user");
            }

            feedbackService.updateFeedback(feedbackDTO, id);
            return ResponseEntity.ok("Update feedback successfully");
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> insertFeedback(
            @Valid @RequestBody FeedbackDTO feedbackDTO
    ) {
        try {
            Account loginAccount = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (loginAccount.getUSERID() != feedbackDTO.getUSERID()) {
                return ResponseEntity.badRequest().body("You can not feedback as another user");
            }

            return ResponseEntity.ok(feedbackService.insertFeedback(feedbackDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
