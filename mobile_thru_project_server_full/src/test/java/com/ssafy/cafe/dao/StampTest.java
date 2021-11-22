package com.ssafy.cafe.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import com.ssafy.cafe.model.dao.StampDao;
import com.ssafy.cafe.model.dto.Comment;
import com.ssafy.cafe.model.dto.Stamp;

class StampTest extends AbstractDaoTest{

    @Test
    @Order(1)
    public void selectTest() {
        Stamp selected = sDao.select(1);
        assertEquals(selected.getUserId(), "id 01");

    }

    @Test
    @Order(2)
    public void insertTest() {
        Stamp data = new Stamp("id 02", 1,10);
        int result = sDao.insert(data);
        assertEquals(result, 1);
    }
    static Stamp last;

    @Test
    @Order(3)
    public void selectAll() {
        //int result = sDao.selectAll("id 01");
        
//        last = result.get(0);
//        assertEquals(last.getQuantity(), 10);
    }
    
    @Test
    public void selectByUser() {
    	List<Map<String, Object>> result = sDao.selectByUserId("id 01");
        
        Stamp selected = (Stamp) result.get(0);
        assertEquals(selected.getUserId(), "id 01");
    }
}
