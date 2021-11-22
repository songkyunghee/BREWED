package com.ssafy.cafe.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ssafy.cafe.model.dao.StampDao;
import com.ssafy.cafe.model.dao.UserDao;
import com.ssafy.cafe.model.dto.Coupon;
import com.ssafy.cafe.model.dto.Stamp;
import com.ssafy.cafe.model.dto.User;

/**
 * @author itsmeyjc
 * @since 2021. 6. 23.
 */
@Service
public class StampServiceImpl implements StampService{

    @Autowired
    StampDao sDao;
    
    @Autowired
    UserDao uDao;
    
    @Override
    public List<Map<String, Object>> selectByUser(String id) {
        return sDao.selectByUserId(id);
    }

	@Override
	public int updateStamp(Stamp stamp) {
		// TODO Auto-generated method stub
		return sDao.updateStamp(stamp);
	}

	@Override
	public int updateStampCoupon(Stamp stamp) {
		User user = uDao.select(stamp.getUserId());
		List<Map<String, Object>> userInfo = sDao.selectByUserId(stamp.getUserId());
		int stampNum = (int) userInfo.get(0).get("quantity");
		int couponId = (int) userInfo.get(0).get("c_id");
		int Num = stamp.getQuantity();
		// Num / 10 쿠폰 개수
		// Num % 10 스탬프 개수
		// userInfo.size 쿠폰의 개수
		int updateCouponNum = Num / 10;
		int updateStampNum = Num % 10;
		if(userInfo.size() < updateCouponNum) {
			int diff = updateCouponNum - userInfo.size();
			for(int i = 0; i < diff; i++) {
				sDao.insertCoupon(new Coupon(0, 2000, stamp.getUserId()));
			}
		}
		sDao.updateStamp(new Stamp(0, stamp.getUserId(), stamp.getOrderId(), updateStampNum));
		uDao.update(new User(user.getId(), user.getName(), user.getPass(), Num, user.getToken()));
		return 1;
	}
}
