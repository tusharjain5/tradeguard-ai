package com.tradeguard.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;




@Service
public class PaymentService {

private final RazorpayClient razorpayClient;
    
    @Value("${razorpay.key.secret}")
    private String keySecret;

    public PaymentService(RazorpayClient razorpayClient) {
        this.razorpayClient = razorpayClient;
    }

    public String createOrder(int amount) throws Exception {
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount * 100);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "order_" + System.currentTimeMillis());
        orderRequest.put("payment_capture", 1);

        Order order = razorpayClient.orders.create(orderRequest);
        System.out.println("📦 Order Created: " + order.get("id"));
        return order.get("id").toString();
    }
}
