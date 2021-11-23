package com.ssafy.cafe.model.service;

import java.util.List;
import java.util.Map;
import com.ssafy.cafe.model.dto.Product;

public interface ProductService {
    /**
     * 모든 상품 정보를 반환한다.
     * @return
     */
    List<Product> getProductList();
    
    /**
     * backend 관통 과정에서 추가됨
     * 상품의 정보, 판매량, 평점 정보를 함께 반환
     * @param productId
     * @return
     */
    List<Map<String, Object>> selectWithComment(Integer productId);
    
    // 판매량 순으로 정보를 반환한다.
    List<Product> getHotProductList();
    
    // 판매량을 갱신한다.
    int updateProductSales(Product product);
}
