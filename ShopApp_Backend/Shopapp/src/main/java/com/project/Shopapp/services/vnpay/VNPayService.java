package com.project.Shopapp.services.vnpay;

import com.project.Shopapp.components.VNPayUtils;
import com.project.Shopapp.configurations.VNPayConfig;
import com.project.Shopapp.dtos.PaymentDTO;
import com.project.Shopapp.dtos.PaymentQueryDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class VNPayService implements IVNPayService{
    private final VNPayConfig vnPayConfig;
    private final VNPayUtils vnPayUtils;

    @Override
    public String createPaymentUrl(PaymentDTO paymentDTO, HttpServletRequest request) {
        String version="2.1.0";
        String command="pay";
        String orderType="other";
        long amount=paymentDTO.getAmount()*100;
        String bankCode=paymentDTO.getBankCode();

        String transactionReference=vnPayUtils.getRandomNumber(8); // Ma giao dich
        String clientIpAddress=vnPayUtils.getIpAddress(request);

        String terminalCode=vnPayConfig.getVnpTmnCode();

        Map<String,String> params=new HashMap<>();
        params.put("vnp_Version",version);
        params.put("vnp_Command",command);
        params.put("vnp_TmnCode",terminalCode);
        params.put("vnp_Amount",String.valueOf(amount));
        params.put("vnp_CurrCode","VND");

        if(bankCode!=null&&!bankCode.isEmpty()){
            params.put("vnp_BankCode",bankCode);
        }
        params.put("vnp_TxnRef",transactionReference);
        params.put("vnp_OrderInfo","Thanh toán đơn hàng: "+transactionReference);
        params.put("vnp_OrderType",orderType   );

        String locale=paymentDTO.getLanguage();
        if(locale!=null&&!locale.isEmpty()){
            params.put("vnp_Locale",locale);
        }
        else {
            params.put("vnp_Locale","vn");
        }

        params.put("vnp_ReturnUrl",vnPayConfig.getVnpReturnUrl());
        params.put("vnp_IpAddr",clientIpAddress);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String createdDate=dateFormat.format(calendar.getTime());
        params.put("vnp_CreateDate",createdDate);

        calendar.add(Calendar.MINUTE,15);
        String expirationDate=dateFormat.format(calendar.getTime());
        params.put("vnp_ExpireDate",expirationDate);

        List<String> sortedFieldNames= new ArrayList<>(params.keySet());
        Collections.sort(sortedFieldNames);

        StringBuilder hashData=new StringBuilder();
        StringBuilder queryData=new StringBuilder();

        for(Iterator<String> iterator= sortedFieldNames.iterator();iterator.hasNext();){
            String fieldName=iterator.next()    ;
            String fieldValue=params.get(fieldName);

            if(fieldValue!=null&&!fieldValue.isEmpty()){
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                queryData   .append(URLEncoder.encode(fieldName,StandardCharsets.US_ASCII))
                        .append('=')
                        .append(URLEncoder.encode(fieldValue,StandardCharsets.US_ASCII));
                if(iterator.hasNext()){
                    hashData.append('&');
                    queryData.append('&');
                }
            }
        }

        String secureHash=vnPayUtils.hmacSHA512(vnPayConfig.getSecretKey(),hashData.toString());
        queryData.append("&vnp_SecureHash=").append(secureHash);

        return vnPayConfig.getVnpPayUrl()+"?"+queryData;
    }

    @Override
    public String queryTransaction(PaymentQueryDTO paymentQueryDTO, HttpServletRequest request) throws IOException {
        // Chuan bi tham so cho VNPay
        String version="2.1.0";
        String command="pay";
        String orderType="other";
        long amount=paymentDTO.getAmount()*100;
        String bankCode=paymentDTO.getBankCode();

        String transactionReference=vnPayUtils.getRandomNumber(8); // Ma giao dich
        String clientIpAddress=vnPayUtils.getIpAddress(request);

        String terminalCode=vnPayConfig.getVnpTmnCode();

    }


}
