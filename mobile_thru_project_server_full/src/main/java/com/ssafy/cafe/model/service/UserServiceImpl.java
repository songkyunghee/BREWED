package com.ssafy.cafe.model.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ssafy.cafe.model.dao.UserDao;
import com.ssafy.cafe.model.dto.Noti;
import com.ssafy.cafe.model.dto.User;

/**
 * @author itsmeyjc
 * @since 2021. 6. 23.
 */
@Service
public class UserServiceImpl implements UserService {
    
    private static UserServiceImpl instance = new UserServiceImpl();

    private UserServiceImpl() {}

    public static UserServiceImpl getInstance() {
        return instance;
    }
    
    @Autowired
    private UserDao userDao;

    @Override
    public void join(User user) {
        userDao.insert(user);

    }

    @Override
    public User login(String id, String pass) {
        User user = userDao.select(id);
        if (user != null && user.getPass().equals(pass)) {
            return user;
        } else {
            return null;
        }
    }

    @Override
    public void leave(String id) {
        userDao.delete(id);
    }

    @Override
    public boolean isUsedId(String id) {
        return userDao.select(id)!=null;
    }

	@Override
	public int update(User user) {
		return userDao.update(user);
		
	}

	@Override
	public List<Map<String, Object>> selectNotiByUser(String id) {
		// TODO Auto-generated method stub
		return userDao.selectUserNoti(id);
	}

	@Override
	public int deleteNoti(int n_id) {
		// TODO Auto-generated method stub
		return userDao.deleteNoti(n_id);
	}

	@Override
	public int insertNoti(Noti noti) {
		// TODO Auto-generated method stub
		return userDao.insertNoti(noti);
	}

	@Override
	public User selectUser(String id) {
		return userDao.selectUser(id);
	}

	@Override
	public List<String> selectWithCommentUserName(int productId) {
		return userDao.selectWithCommentUserName(productId);
	}
	
	
	
}
