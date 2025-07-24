package com.project.Shopapp.components;

import com.project.Shopapp.models.LoaiSanPham;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@KafkaListener(id = "groupA", topics = {"get-all-categories", "insert-a-category"})
public class MyKafkaListener {
    @KafkaHandler
    public void listenCategory(LoaiSanPham loaiSanPham) {
        System.out.println("Received: "+loaiSanPham);
    }

    @KafkaHandler(isDefault = true)
    public void unknown(Object object  ){
        System.out.println("Received unknown: "+object);
    }

    @KafkaHandler
    public void listenListOfCategories(List<LoaiSanPham> loaiSanPhams)
    {
        System.out.println("Received: "+loaiSanPhams);
    }
}
