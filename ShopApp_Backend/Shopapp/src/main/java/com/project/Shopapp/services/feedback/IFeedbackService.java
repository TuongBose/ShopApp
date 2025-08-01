package com.project.Shopapp.services.feedback;

import com.project.Shopapp.dtos.FeedbackDTO;
import com.project.Shopapp.exceptions.DataNotFoundException;
import com.project.Shopapp.responses.feedback.FeedbackResponse;

import java.util.List;

public interface IFeedbackService {
    FeedbackResponse insertFeedback(FeedbackDTO feedbackDTO) throws DataNotFoundException;

    void deleteFeedback(int feedbackId) throws Exception;

    void updateFeedback(FeedbackDTO feedbackDTO, int feedbackId) throws DataNotFoundException;

    List<FeedbackResponse> getFeedbacksByAccount(Integer accountId) throws DataNotFoundException;

    List<FeedbackResponse> getFeedbacksByProduct(Integer productId) throws DataNotFoundException;

    List<FeedbackResponse> getFeedbacksByAccountAndProduct(Integer accountId, Integer productId) throws DataNotFoundException;
    void generateFakeFeedbacks() throws Exception;
}
