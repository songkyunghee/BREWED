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
		int stampNum = (int) userInfo.get(0).get("quantity"); // 현재 스탬프 개수
		int couponId = (int) userInfo.get(0).get("c_id");
		int n = user.getStamps() + stamp.getQuantity();
		int couponCnt = n / 10;
		int stampCnt = n % 10;
		for(int i = 0; i < couponCnt; i++) {
			sDao.insertCoupon(new Coupon(0, 2000, stamp.getUserId()));
		}
	
		sDao.updateStamp(new Stamp(0, stamp.getUserId(), stamp.getOrderId(), stampCnt));
		uDao.update(new User(user.getId(), user.getName(), user.getPass(), stampCnt, user.getToken()));
		return 1;
	}

	@Override
	public int deleteCoupon(int c_id) {
		return sDao.deleteCoupon(c_id);
	}
}
