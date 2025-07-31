package com.project.Shopapp.services.feedback;

import com.github.javafaker.Faker;
import com.project.Shopapp.dtos.FeedbackDTO;
import com.project.Shopapp.exceptions.DataNotFoundException;
import com.project.Shopapp.models.Account;
import com.project.Shopapp.models.Feedback;
import com.project.Shopapp.models.SanPham;
import com.project.Shopapp.repositories.AccountRepository;
import com.project.Shopapp.repositories.FeedbackRepository;
import com.project.Shopapp.repositories.SanPhamRepository;
import com.project.Shopapp.responses.feedback.FeedbackResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackService implements IFeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final AccountRepository accountRepository;
    private final SanPhamRepository sanPhamRepository;

    @Override
    @Transactional
    public FeedbackResponse insertFeedback(FeedbackDTO feedbackDTO) throws DataNotFoundException {
        Account existingAccount = accountRepository.findById(feedbackDTO.getUSERID())
                .orElseThrow(() -> new DataNotFoundException("Account not found"));

        SanPham existingSanPham = sanPhamRepository.findById(feedbackDTO.getMASANPHAM())
                .orElseThrow(() -> new DataNotFoundException("Product not found"));

        Feedback newfeedback = Feedback
                .builder()
                .USERID(existingAccount)
                .SOSAO(feedbackDTO.getSOSAO())
                .NOIDUNG(feedbackDTO.getNOIDUNG())
                .MASANPHAM(existingSanPham)
                .build();
        feedbackRepository.save(newfeedback);

        return FeedbackResponse.fromFeedback(newfeedback);
    }

    @Override
    @Transactional
    public void deleteFeedback(int feedbackId) {
        feedbackRepository.deleteById(feedbackId);
    }

    @Override
    @Transactional
    public void updateFeedback(FeedbackDTO feedbackDTO, int feedbackId) throws DataNotFoundException {
        Feedback existingFeedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new DataNotFoundException("Feedback not found"));

        Account existingAccount = accountRepository.findById(feedbackDTO.getUSERID())
                .orElseThrow(() -> new DataNotFoundException("Account not found"));

        SanPham existingSanPham = sanPhamRepository.findById(feedbackDTO.getMASANPHAM())
                .orElseThrow(() -> new DataNotFoundException("Product not found"));

        existingFeedback.setNOIDUNG(feedbackDTO.getNOIDUNG());
        existingFeedback.setSOSAO(feedbackDTO.getSOSAO());

        feedbackRepository.save(existingFeedback);
    }

    @Override
    public List<FeedbackResponse> getFeedbacksByAccount(Integer accountId) throws DataNotFoundException {
        Account existingAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new DataNotFoundException("Account not found"));

        List<Feedback> feedbacks = feedbackRepository.findByUSERID(existingAccount);
        return feedbacks.stream()
                .map(FeedbackResponse::fromFeedback)
                .collect(Collectors.toList());
    }

    @Override
    public List<FeedbackResponse> getFeedbacksByProduct(Integer productId) throws DataNotFoundException {
        SanPham existingSanPham = sanPhamRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Product not found"));

        List<Feedback> feedbacks = feedbackRepository.findByMASANPHAM(existingSanPham);
        return feedbacks.stream()
                .map(FeedbackResponse::fromFeedback)
                .collect(Collectors.toList());
    }

    @Override
    public List<FeedbackResponse> getFeedbacksByAccountAndProduct(Integer accountId, Integer productId)
            throws DataNotFoundException {
        Account existingAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new DataNotFoundException("Account not found"));

        SanPham existingSanPham = sanPhamRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Product not found"));

        List<Feedback> feedbacks = feedbackRepository.findByUSERIDAndMASANPHAM(existingAccount, existingSanPham);
        return feedbacks.stream()
                .map(FeedbackResponse::fromFeedback)
                .collect(Collectors.toList());
    }

    @Override
    public void generateFakeFeedbacks() throws Exception {
        Faker faker = new Faker();
        Random random = new Random();
        List<Account> accounts = accountRepository.findAll();
        List<SanPham> sanPhams = sanPhamRepository.findAll();
        List<Feedback> feedbacks = new ArrayList<>();

        final int totalRecords = 10000;
        final int batchSize = 1000;
        for (int i = 0; i < totalRecords; i++) {
            Account account = accounts.get(random.nextInt(accounts.size()));
            SanPham sanPham = sanPhams.get(random.nextInt(sanPhams.size()));

            // Generate a fake feedback
            Feedback feedback = Feedback.builder()
                    .NOIDUNG(faker.lorem().sentence())
                    .MASANPHAM(sanPham)
                    .USERID(account)
                    .SOSAO(faker.number().numberBetween(1, 5))
                    .build();

            // Set a random created date within the range of 2015 to now
            LocalDateTime startDate = LocalDateTime.of(2015, 1, 1, 0, 0);
            LocalDateTime endDate = LocalDateTime.now();
            long randomEpoch = ThreadLocalRandom.current()
                    .nextLong(startDate.toEpochSecond(ZoneOffset.UTC), endDate.toEpochSecond(ZoneOffset.UTC));
            feedback.setCreatedAt(LocalDateTime.ofEpochSecond(randomEpoch, 0, ZoneOffset.UTC));
            // Save the comment
            feedbacks.add(feedback);
            if (feedbacks.size() >= batchSize) {
                feedbackRepository.saveAll(feedbacks);
                feedbacks.clear();
            }
        }
    }
}
