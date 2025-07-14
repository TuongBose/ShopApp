package com.project.Shopapp.services.sanphamredis;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.Shopapp.responses.SanPhamResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SanPhamRedisService implements ISanPhamRedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper redisObjectMapper;

    private String getKeyFrom(String keyword, int MALOAISANPHAM, PageRequest pageRequest) {
        int pageNumber = pageRequest.getPageNumber();
        int pageSize = pageRequest.getPageSize();
        Sort sort = pageRequest.getSort();
        String sortDirection = Objects.requireNonNull(sort.getOrderFor("MASANPHAM")).getDirection() == Sort.Direction.ASC ? "asc" : "desc";
        String key;
        key = String.format("all_products:%d:%d:%s", pageNumber, pageSize, sortDirection);
        return key;
        /*
        {
            "all_products:1:10:asc": "list of products object"
        }
        */
    }

    @Override
    public void clear() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Override
    public List<SanPhamResponse> getAllSanPham(String keyword, int MALOAISANPHAM, PageRequest pageRequest) throws Exception {
        String key = this.getKeyFrom(keyword, MALOAISANPHAM, pageRequest);
        String json = (String) redisTemplate.opsForValue().get(key);
        List<SanPhamResponse> sanPhamResponseList;
        sanPhamResponseList = json != null ?
                redisObjectMapper.readValue(json, new TypeReference<List<SanPhamResponse>>() {
                })
                : null;
        return sanPhamResponseList;
    }

    @Override
    public void saveAllSanPham(List<SanPhamResponse> sanPhamResponseList, String keyword,
                               int MALOAISANPHAM, PageRequest pageRequest) throws Exception {
        String key = this.getKeyFrom(keyword, MALOAISANPHAM, pageRequest);
        String json = redisObjectMapper.writeValueAsString(sanPhamResponseList);
        redisTemplate.opsForValue().set(key, json);
    }
}
