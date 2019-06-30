package cn.chenny3.secondHand.service.impl;

import cn.chenny3.secondHand.dao.OrderItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceImpl {
    @Autowired
    private OrderItemDao orderItemDao;
}
