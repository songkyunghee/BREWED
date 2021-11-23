package com.ssafy.cafe.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.ssafy.cafe.model.dao.OrderDao;
import com.ssafy.cafe.model.dao.OrderDetailDao;
import com.ssafy.cafe.model.dao.StampDao;
import com.ssafy.cafe.model.dao.UserDao;
import com.ssafy.cafe.model.dto.Coupon;
import com.ssafy.cafe.model.dto.Order;
import com.ssafy.cafe.model.dto.OrderDetail;
import com.ssafy.cafe.model.dto.Stamp;
import com.ssafy.cafe.model.dto.User;

/**
 * @author itsmeyjc
 * @since 2021. 6. 23.
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderDao oDao;
    @Autowired
    OrderDetailDao dDao;
    @Autowired
    StampDao sDao;
    @Autowired
    UserDao uDao;

    @Override
    @Transactional
    public void makeOrder(Order order) {
        // 주문 및 주문 상세 테이블 저장
    	System.out.print("makeOrder :: ");
    	System.out.println(order);
        oDao.insert(order);
        List<OrderDetail> details = order.getDetails();
        int quantitySum = 0;
        for(OrderDetail detail: details) {
            detail.setOrderId(order.getId());
            dDao.insert(detail);
            quantitySum += detail.getQuantity();
        }
        
        // 스템프 정보 저장
//        Stamp stamp = Stamp.builder().userId(order.getUserId()).quantity(quantitySum).orderId(order.getId()).build();
        Stamp stamp = new Stamp(order.getUserId(), order.getId(), quantitySum);
        User user = uDao.select(stamp.getUserId());
		int n = user.getStamps() + stamp.getQuantity();
		int couponCnt = n / 10;
		int stampCnt = n % 10;
		for(int i = 0; i < couponCnt; i++) {
			sDao.insertCoupon(new Coupon(0, 2000, stamp.getUserId()));
		}
	
		sDao.updateStamp(new Stamp(0, stamp.getUserId(), stamp.getOrderId(), stampCnt));
		uDao.updateUserStamp(new User(user.getId(), user.getName(), user.getPass(), stampCnt, user.getToken()));
    }

    @Override
    public Order getOrderWithDetails(Integer orderId) {
        return oDao.selectWithDetail(orderId);
    }

    @Override
    public List<Order> getOrdreByUser(String id) {
        return oDao.selectByUser(id);
    }

    @Override
    public void updateOrder(Order order) {
        oDao.update(order);
    }

    @Override
    public List<Map> selectOrderTotalInfo(int id) {
        return oDao.selectOrderTotalInfo(id);
    }

    @Override
    public List<Map<String, Object>> getLastMonthOrder(String id) {
        return oDao.getLastMonthOrder(id);
    }
    
    @Override
    public List<Map> getAllOrder() {
        // TODO Auto-generated method stub
        return oDao.getAllOrder();
    }

	@Override
	public List<Map> selectDateComOrderList(HashMap<String, String> map) {
		// TODO Auto-generated method stub

		return oDao.getDateComOrderList(map);
	}

	@Override
	public List<Map> selectDateNotComOrderList(HashMap<String, String> map) {
		// TODO Auto-generated method stub
		
		return oDao.getDateNotComOrderList(map);
	}

}
