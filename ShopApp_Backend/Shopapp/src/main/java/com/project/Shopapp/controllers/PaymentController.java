package com.project.Shopapp.controllers;

import com.project.Shopapp.dtos.PaymentDTO;
import com.project.Shopapp.responses.ResponseObject;
import com.project.Shopapp.services.vnpay.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final VNPayService vnPayService;

    @PostMapping("/create_payment_url")
    public ResponseEntity<ResponseObject> createPayment(@RequestBody PaymentDTO paymentDTO, HttpServletRequest request){
        try{
            String paymentUrl=vnPayService.createPaymentUrl(paymentDTO,request);

            return ResponseEntity.ok(ResponseObject.builder()
                            .message("Payment URL generated successfully")
                            .status(HttpStatus.OK)
                            .data(paymentUrl)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseObject.builder()
                            .message("Error generating payment URL: "+e.getMessage())
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .build());
        }
    }
}




