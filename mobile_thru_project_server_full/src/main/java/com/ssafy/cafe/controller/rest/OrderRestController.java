package com.ssafy.cafe.controller.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ssafy.cafe.model.dto.Order;
import com.ssafy.cafe.model.service.OrderService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/rest/order")
@CrossOrigin("*")
public class OrderRestController {
    @Autowired
    private OrderService oService;
    
    @PostMapping
    @ApiOperation(value="order 객체를 저장하고 추가된 Order의 id를 반환한다.", response = Integer.class )
    @Transactional
    public Integer makeOrder(@RequestBody Order order) {
    	System.out.println("하히후헤호");
    	System.out.println(order);
        oService.makeOrder(order);
        return order.getId();
    }
    
    @GetMapping("/{orderId}")
    @ApiOperation(value="{orderId}에 해당하는 주문의 상세 내역을 목록 형태로 반환한다."
            + "이 정보는 사용자 정보 화면의 주문 내역 조회에서 사용된다.", response = List.class)
    public List<Map> getOrderDetail(@PathVariable Integer orderId) {
        return oService.selectOrderTotalInfo(orderId);
    }
    
    @GetMapping("/byUser")
    @ApiOperation(value="{id}에 해당하는 사용자의 최근 1개월간 주문 내역을 반환한다."
            + "반환 정보는 1차 주문번호 내림차순, 2차 주문 상세 내림차순으로 정렬된다.", response = List.class)
    public List<Map<String, Object>> getLastMonthOrder(String id) {
        return oService.getLastMonthOrder(id);
    }
    
    @GetMapping("/orderList")
    @ApiOperation(value = "모든 주문내역을 반환한다.", response = List.class)
    public List<Map> getAllOrder() {
    	return oService.getAllOrder();
    }
    
    @GetMapping("/dateOrderComList/{date}/{completed}/{storeId}")
    @ApiOperation(value = "{date}에 해당하는 완료된 주문 리스트를 목록 형태로 반환한다.", response = List.class)
    public List<Map> getDateComOrderList(@PathVariable String date,
    		@PathVariable String completed,
    		@PathVariable String storeId) {
    	HashMap<String, String> parameterMap = new HashMap<String, String>();
    	parameterMap.put("orderTime", date);
    	parameterMap.put("completed", completed);
    	parameterMap.put("storeId", storeId);
    	
    	return oService.selectDateComOrderList(parameterMap);
    }
    
    @GetMapping("/dateOrderNotComList/{date}/{storeId}")
    @ApiOperation(value = "{date}에 해당하는 완료되지 않은 주문 리스트를 목록 형태로 반환한다.", response = List.class)
    public List<Map> getDateNotComOrderList(@PathVariable String date,
    		@PathVariable String storeId) {
    	HashMap<String, String> parameterMap = new HashMap<String, String>();
    	parameterMap.put("orderTime", date);
    	parameterMap.put("storeId", storeId);
    	
    	return oService.selectDateNotComOrderList(parameterMap);
    }
    
    @PutMapping("/dateOrderNotComList")
    @Transactional
    @ApiOperation(value="order 객체를 수정한다.", response = Boolean.class)
    public Boolean update(@RequestBody Order order) {
    	oService.updateOrder(order);
    	return true;
    }
    
}
