package cn.chenny3.secondHand.service;

import cn.chenny3.secondHand.common.bean.dto.EasyResult;
import cn.chenny3.secondHand.model.Order;

import java.util.Date;
import java.util.List;

public interface OrderService {
    //添加订单
    EasyResult addOrder(Order order);
    //修改订单状态
    void updateOrderStatus(String orderId, int status, Date updated);
    //查询指定用购买的订单
    List<Order> selectOrderListByUserId(int userId,int curPage,int pageSize);
    //查询订单的详细信息
    Order selectOrderById(String orderId);
    //取消订单
    void cancelOrder(Order order);
    //合并支付
    void combinePayment(int userid,List<Order> orderList);
    //获取用户的订单数量
    int selectCountByUserId(int userId);
    //查询卖家的所有订单
    List<Order> selectOrderListBySellerId(int sellerId);

}
