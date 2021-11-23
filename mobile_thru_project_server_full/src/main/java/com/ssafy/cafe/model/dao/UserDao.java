package com.ssafy.cafe.model.dao;

import java.util.List;
import java.util.Map;

import com.ssafy.cafe.model.dto.Noti;
import com.ssafy.cafe.model.dto.User;

public interface UserDao {
    /**
     * 사용자 정보를 추가한다.
     * @param user
     * @return
     */
    int insert(User user);

    
    /**
     * 사용자 정보를 조회한다.
     * @param userId
     * @return
     */

    User select(String userId);

    /**
     * 사용자 정보를 삭제한다.
     * @param userId
     * @return
     */
    int delete(String userId);
    
    List<User> selectAll(); 
    
    int update(User user);
    
    // 모든 유저의 토큰 값을 조회한다.
    List<String> selectUserToken();
    
    // 유저의 스탬프 값을 수정한다.
    int updateUserStamp(User user);
    
    // 유저의 알림들을 조회한다.
    List<Map<String, Object>> selectNotiByUser(String userId);
    
    int deleteNoti(int nId);
    
    int insert(Noti noti);
    
   
    
   
    

}
