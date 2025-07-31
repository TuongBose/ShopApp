package com.project.Shopapp.services.vnpay;

import com.project.Shopapp.dtos.PaymentDTO;
import com.project.Shopapp.dtos.PaymentQueryDTO;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public interface IVNPayService {
    String createPaymentUrl(PaymentDTO paymentDTO, HttpServletRequest request);
    String queryTransaction(PaymentQueryDTO paymentQueryDTO, HttpServletRequest request) throws IOException;
}
