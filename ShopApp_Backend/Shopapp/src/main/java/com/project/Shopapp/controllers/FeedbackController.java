package com.project.Shopapp.controllers;

import com.project.Shopapp.dtos.FeedbackDTO;
import com.project.Shopapp.exceptions.DataNotFoundException;
import com.project.Shopapp.models.Account;
import com.project.Shopapp.responses.ResponseObject;
import com.project.Shopapp.responses.feedback.FeedbackResponse;
import com.project.Shopapp.services.feedback.IFeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

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
    public ResponseEntity<ResponseObject> updateFeedback(
            @PathVariable int id,
            @RequestBody FeedbackDTO feedbackDTO
    ) throws Exception {
        Account loginAccount = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!Objects.equals(loginAccount.getUSERID(), feedbackDTO.getUSERID())) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("You can not update feedback as another user")
                    .status(HttpStatus.BAD_REQUEST)
                    .data(null)
                    .build());
        }

        feedbackService.updateFeedback(feedbackDTO, id);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Update feedback successfully")
                .status(HttpStatus.OK)
                .data(null)
                .build());

    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<ResponseObject> insertFeedback(
            @Valid @RequestBody FeedbackDTO feedbackDTO
    ) throws Exception {
        Account loginAccount = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (loginAccount.getUSERID() != feedbackDTO.getUSERID()) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("You can not feedback as another user")
                    .status(HttpStatus.BAD_REQUEST)
                    .data(null)
                    .build());
        }
        feedbackService.insertFeedback(feedbackDTO);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Insert feedback successfully")
                .status(HttpStatus.OK)
                .data(null)
                .build());
    }

    @PostMapping("/generateFakeComments")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> generateFakeFeedbacks() throws Exception {
        feedbackService.generateFakeFeedbacks();
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Insert fake feedbacks succcessfully")
                .data(null)
                .status(HttpStatus.OK)
                .build());
    }
}
