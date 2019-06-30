package cn.chenny3.secondHand.service.impl;

import cn.chenny3.secondHand.Constants;
import cn.chenny3.secondHand.common.bean.UserHolder;
import cn.chenny3.secondHand.common.bean.dto.EasyResult;
import cn.chenny3.secondHand.common.utils.RedisKeyUtils;
import cn.chenny3.secondHand.common.utils.RedisUtils;
import cn.chenny3.secondHand.common.utils.SecondHandUtil;
import cn.chenny3.secondHand.dao.GoodsDao;
import cn.chenny3.secondHand.dao.OrderDao;
import cn.chenny3.secondHand.dao.OrderItemDao;
import cn.chenny3.secondHand.model.Goods;
import cn.chenny3.secondHand.model.Order;
import cn.chenny3.secondHand.model.OrderItem;
import cn.chenny3.secondHand.model.User;
import cn.chenny3.secondHand.service.OrderService;
import cn.chenny3.secondHand.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private UserHolder userHolder;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtils redisUtils;

    @Override
    public EasyResult addOrder(Order order) {
        synchronized(this){
            EasyResult easyResult=new EasyResult();
            Map<Integer,List<OrderItem>> map=new HashMap();
            List<String> orderIds=new ArrayList<>();
            //判断所购买的商品是否满足库存要求
            List<OrderItem> orderItems = order.getOrderItems();
            if(orderItems!=null){
                for(OrderItem item:orderItems){
                    Goods curGoods = goodsDao.selectGoods(item.getGoodsId());
                    //商品卖家
                    int goodsOwnerId = curGoods.getOwnerId();
                    //用户不能购买自己的商品
                    if(goodsOwnerId==userHolder.get().getId()){
                        easyResult.setCode(-1);
                        easyResult.setMsg("用户不能购买自己发布的商品");
                        return easyResult;
                    }
                    //商品实际库存量
                    int actualInventory=curGoods.getInventory();
                    if(item.getNum()>actualInventory){
                        easyResult.setCode(-1);
                        easyResult.setMsg("【商品id:"+String.valueOf(item.getGoodsId())+",名称为："+item.getGoodsName()+"】的商品已超出实际库存量");
                        return easyResult;
                    }
                    //订单商品补全字段信息
                    item.setGoodsImg(curGoods.getImageArr()[0]);
                    item.setGoodsName(curGoods.getGoodsName());
                    item.setGoodsPrice(curGoods.getPrice());
                    item.setTotalPrice(item.getGoodsPrice()*item.getNum());


                    //将用户下单的商品根据商品卖家进行分类
                    if(map.containsKey(goodsOwnerId)){
                        List<OrderItem> items = map.get(goodsOwnerId);
                        items.add(item);
                    }else{
                        List<OrderItem> items=new ArrayList<OrderItem>();
                        items.add(item);
                        map.put(goodsOwnerId,items);
                    }
                }
            }else{
                easyResult.setCode(-1);
                easyResult.setMsg("订单中未包含任何一件商品");
                return easyResult;
            }

            //根据卖家个数 生成对应数量订单号
            for(Integer key:map.keySet()){
                //日期
                String date = SecondHandUtil.date2String("yyyyMMddHHmmss");
                //买家id
                String buyerId=SecondHandUtil.fillZero(userHolder.get().getId(),5);
                //卖家id
                String sellerId=SecondHandUtil.fillZero(key,5);
                //生成订单号
                String orderId=new StringBuilder(date).append(buyerId).append(sellerId).toString();
                orderIds.add(orderId);

                Order o=new Order();
                o.setId(orderId);
                //初始化订单状态
                o.setStatus(1);
                //初始化买家信息
                User buyuser = userHolder.get();
                o.setUserId(buyuser.getId());
                o.setAddressId(buyuser.getAddressId());
                o.setBuyerNick(buyuser.getName());
                o.setUserId(buyuser.getId());
                o.setBuyerMessage(order.getBuyerMessage());
                //设置支付方式
                o.setPayType(order.getPayType());
                if(o.getPayType()==Constants.ORDER_PAYMENT_TYPE_ONLINE){
                    o.setStatus(Constants.ORDER_STATUS_WAIT_PAY);
                }else{
                    o.setStatus(Constants.ORDER_STATUS_WAIT_SHIP);
                }
                //初始化订单创建、修改时间
                Timestamp timestamp = new Timestamp(new Date().getTime());
                o.setCreated(timestamp);
                o.setUpdated(timestamp);

                //订单总金额
                int orderTotalMoney=0;
                //保存订单商品信息
                for(OrderItem oi:map.get(key)){
                    oi.setOrderId(orderId);
                    orderTotalMoney+=oi.getTotalPrice();
                    orderItemDao.addOrderItem(oi);
                    //修改商品库存量
                    goodsDao.decreaseInventory(oi.getGoodsId(),oi.getNum());
                    //清除掉redis中的缓存
                    redisUtils.del(RedisKeyUtils.getCacheGoodsKey(oi.getGoodsId()));
                }
                o.setMoney(orderTotalMoney);
                orderDao.addOrder(o);

            }
            easyResult.setCode(0);
            easyResult.setMsg(orderIds);
            return easyResult;
        }
    }

    @Override
    public void updateOrderStatus(String orderId, int status, Date updated) {
        orderDao.updateOrderStatus(orderId,status,updated);
    }

    @Override
    public List<Order> selectOrderListByUserId(int userId,int curPage,int pageSize) {
        int offset=(curPage-1)*pageSize;
        List<Order> orderList = orderDao.selectOrderListByUserId(userId, offset, pageSize);
        for(Order order:orderList){
            List<OrderItem> orderItems = orderItemDao.selectOrderItemsByOrderId(order.getId());
            order.setOrderItems(orderItems);
        }
        return orderList;
    }

    @Override
    public Order selectOrderById(String orderId) {
        Order order = orderDao.selectOrderById(orderId);
        if(order!=null){
            List<OrderItem> orderItemList = orderItemDao.selectOrderItemsByOrderId(orderId);
            order.setOrderItems(orderItemList);
        }
        return order;
    }

    @Override
    public void cancelOrder(Order order) {
        String orderId=order.getId();
        //修改订单状态
        orderDao.updateOrderStatus(orderId, Constants.ORDER_STATUS_CANCEL,new Date());
        //恢复物品库存量
        List<OrderItem> orderItems = orderItemDao.selectOrderItemsByOrderId(orderId);
        for(OrderItem orderItem:orderItems){
            goodsDao.increaseInventory(orderItem.getGoodsId(),orderItem.getNum());
            //清除掉redis中的缓存
            redisUtils.del(RedisKeyUtils.getCacheGoodsKey(orderItem.getGoodsId()));
        }
        //退款给用户
        userService.rechargeMoney(order.getUserId(),order.getMoney());
    }

    //合并支付
    @Override
    public void combinePayment(int userId,List<Order> orderList) {
        int totalPayMoney=0;
        Date curDate = new Date();
        for(Order order:orderList){
            //修改订单状态为已付款状态
            orderDao.updateOrderStatus(order.getId(),Constants.ORDER_STATUS_WAIT_SHIP,curDate);
            //统计支付金额
            totalPayMoney+=order.getMoney();
        }
        //扣除用户金额
        userService.consumeMoney(userId,totalPayMoney);
    }

    @Override
    public int selectCountByUserId(int userId) {
        return orderDao.selectCountByUserId(userId);
    }

    @Override
    public List<Order> selectOrderListBySellerId(int sellerId) {
        List<Order> orders = orderDao.selectOrderListBySellerId(sellerId);
        if(orders==null||orders.size()==0){
            return new ArrayList<>();
        }

        for(Order order:orders){
            List<OrderItem> orderItemList = orderItemDao.selectOrderItemsByOrderId(order.getId());
            order.setOrderItems(orderItemList);
        }

        return orders;
    }
}
