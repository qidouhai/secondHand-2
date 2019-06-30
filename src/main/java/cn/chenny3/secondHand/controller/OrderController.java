package cn.chenny3.secondHand.controller;

import cn.chenny3.secondHand.Constants;
import cn.chenny3.secondHand.common.bean.PageHelper;
import cn.chenny3.secondHand.common.bean.UserHolder;
import cn.chenny3.secondHand.common.bean.dto.EasyResult;
import cn.chenny3.secondHand.common.bean.vo.ViewObject;
import cn.chenny3.secondHand.common.utils.SecondHandUtil;
import cn.chenny3.secondHand.model.Goods;
import cn.chenny3.secondHand.model.Order;
import cn.chenny3.secondHand.model.OrderItem;
import cn.chenny3.secondHand.model.User;
import cn.chenny3.secondHand.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserHolder userHolder;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private UserService userService;
    @Autowired
    private CartService cartService;

    /**
     * 系统根据物品id和购买数量，生成订单信息 ，待用户确认订单（此时订单还未真正创建）
     * @param goodsIds
     * @param nums
     * @param model
     * @return 返回订单信息
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String createOrder(@RequestParam("goodsIds") List<Integer> goodsIds, @RequestParam("nums") List<Integer> nums, Model model) {
        try {
            List<OrderItem> orderItemList = new ArrayList<>();
            OrderItem orderItem = null;
            if ((goodsIds != null && nums != null) && (goodsIds.size() == nums.size())) {
                int i = 0;
                int orderTotalPrice = 0;
                for (Integer goodsId : goodsIds) {
                    Goods goods = goodsService.selectGoods(goodsId);
                    orderItem = new OrderItem();
                    orderItem.setGoodsId(goodsId);
                    orderItem.setNum(nums.get(i));
                    orderItem.setGoodsName(goods.getGoodsName());
                    orderItem.setGoodsImg(goods.getImageArr()[0]);
                    orderItem.setGoodsPrice(goods.getPrice());
                    orderItem.setTotalPrice(orderItem.getNum() * orderItem.getGoodsPrice());
                    orderItemList.add(orderItem);
                    orderTotalPrice += orderItem.getTotalPrice();
                    i++;
                }
                //组装订单对象
                Order order = new Order();
                order.setMoney(orderTotalPrice);
                order.setOrderItems(orderItemList);
                model.addAttribute("vo", new ViewObject()
                        .put("order", order)  //订单信息
                        .put("address", addressService.select(userHolder.get().getAddressId())));  //收货地址信息
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/order/order_add";
    }

    /**
     * 根据提交的确认订单信息，创建订单（此时订单信息已保存至数据库）
     * @param order
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public EasyResult addOrder(@RequestBody String order) {
        EasyResult easyResult = null;
        try {
            Order orderEntity = SecondHandUtil.getObjectFromJson(order, Order.class);
            easyResult = orderService.addOrder(orderEntity);
            //把对应商品从用户购物车中删除
            int userId = userHolder.get().getId();
            for(OrderItem orderItem:orderEntity.getOrderItems()){
                cartService.deleteCart(orderItem.getGoodsId(),userId);
            }

        } catch (IOException e) {
            e.printStackTrace();
            easyResult = new EasyResult(1, "订单创建失败");
        }
        return easyResult;
    }

    //等待付款和等待发货的订单 都可以进行取消订单操作
    @RequestMapping(value = "cancel", method = RequestMethod.POST)
    @ResponseBody
    public EasyResult cancelOrder(@RequestParam String orderId) {
        EasyResult easyResult = null;
        try {
            User curUser = userHolder.get();
            Order order = orderService.selectOrderById(orderId);
            //只有处于等待付款的状态订单才能被取消，并且只能是订单本人进行操作
            if (order.getStatus() <= 2 && order.getUserId() == curUser.getId()) {
                orderService.cancelOrder(order);
                easyResult = new EasyResult(0, "订单取消成功");
                return easyResult;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        easyResult = new EasyResult(1, "订单取消失败");
        return easyResult;
    }

    @RequestMapping(value = "preCombinedPayment")
    public String preCombinedPayment(@RequestParam("orderIds") List<String> orderIds, Model model) {
        List<Order> qualifyOrderList = new ArrayList<>();
        ViewObject vo = new ViewObject();
        int totalPrice=0;
        if (orderIds != null && orderIds.size() > 0) {
            for (String orderId : orderIds) {
                Order order = orderService.selectOrderById(orderId);
                //验证订单是否属于当前所有者
                if (order.getUserId() == userHolder.get().getId()) {
                    //验证订单是否是在线付款类型，以及是否是未付款状态
                    if (checkOrderPaymentType(order, Constants.ORDER_PAYMENT_TYPE_ONLINE) && checkOrderStatus(order, Constants.ORDER_STATUS_WAIT_PAY)) {
                        qualifyOrderList.add(order);
                        totalPrice+=order.getMoney();
                    }
                }
            }

            //提交的订单全部符合上述条件
            if (qualifyOrderList.size() == orderIds.size()) {
                vo.put("orderList", qualifyOrderList);
                vo.put("paymentMoney",totalPrice);
                vo.put("userMoney",userService.selectMoney(userHolder.get().getId()));
                model.addAttribute("vo",vo);
                return "/order/combinedPayment";
            } else {
                vo.put("msg", "合并付款的订单中，有订单状态存在异常！用户只能支付自己的订单，并且订单必须满足在线付款类型和处于待付款两个条件。");
            }

        } else {
            vo.put("msg", "合并付款失败，请联系管理员！");
        }
        model.addAttribute("vo",vo);
        return "redirect:/error.html";
    }

    @RequestMapping(value = "combinedPayment")
    public String combinedPayment(@RequestParam("orderIds")List<String> orderIds, Model model) {
        int curUserId = userHolder.get().getId();
        List<Order> qualifyOrderList = new ArrayList<>();
        ViewObject vo = new ViewObject();
        if (orderIds != null && orderIds.size() > 0) {
            for (String orderId : orderIds) {
                Order order = orderService.selectOrderById(orderId);
                //验证订单是否属于当前所有者
                if (order.getUserId() == curUserId) {
                    //验证订单是否是在线付款类型，以及是否是未付款状态
                    if (checkOrderPaymentType(order, Constants.ORDER_PAYMENT_TYPE_ONLINE) && checkOrderStatus(order, Constants.ORDER_STATUS_WAIT_PAY)) {
                        qualifyOrderList.add(order);
                    }
                }
            }
            //提交的订单全部符合上述条件
            if (qualifyOrderList.size() == orderIds.size()) {
                orderService.combinePayment(curUserId,qualifyOrderList);
                vo.put("orderList", qualifyOrderList);
                vo.put("msg","付款已成功，等待卖家发货！");
                model.addAttribute("vo",vo);
                return "/order/success";
            } else {
                vo.put("msg", "合并付款的订单中，有订单状态存在异常！用户只能支付自己的订单，并且订单必须满足在线付款类型和处于待付款两个条件。");

            }
        } else {
            vo.put("msg", "合并付款失败，请联系管理员！");
        }
        model.addAttribute("vo",vo);
        return "error";
    }

    @RequestMapping(value = "success", method = RequestMethod.GET)
    public String success(@RequestParam("orderIds") List<String> orderIds, Model model) {
        ViewObject vo = new ViewObject();
        boolean flag = true;
        if (orderIds != null && orderIds.size() > 0) {
            for (String orderId : orderIds) {
                Order order = orderService.selectOrderById(orderId);
                if (!checkOrderStatus(order, Constants.ORDER_STATUS_WAIT_SHIP)) {
                    flag = false;
                }
                /*if (checkOrderPaymentType(order, Constants.ORDER_PAYMENT_TYPE_UNLINE) && !checkOrderStatus(order, Constants.ORDER_STATUS_WAIT_SHIP)) {
                    flag = false;
                }*/
            }

            if (flag) {
                //查询订单支付类型
                int payType = orderService.selectOrderById(orderIds.get(0)).getPayType();
                if(payType==Constants.ORDER_PAYMENT_TYPE_ONLINE){
                    vo.put("msg","订单已付款，等待买家发货！");
                }else{
                    vo.put("msg", "订单已提交，等待卖家发货！");
                }
                model.addAttribute("vo",vo);
                return "/order/success";
            }
        }
        vo.put("msg", "执行操作异常");
        model.addAttribute("vo",vo);
        return "error";
    }

    @RequestMapping(value = {"/order/list", "/order/list/{curPage}", "/order/list/{curPage}/{pageSize}"}, method = RequestMethod.GET)
    public String orderList(@PathVariable(required = false) Integer curPage,
                            @PathVariable(required = false) Integer pageSize,
                            Model model) {
        //当前页，页大小初始化
        if (curPage == null) {
            curPage = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }

        //模型数据初始化
        PageHelper<Order> pageHelper = new PageHelper<>();
        pageHelper.setCurPage(curPage);
        pageHelper.setPageSize(pageSize);

        model.addAttribute("vo", new ViewObject().put("pageHelper", pageHelper));
        int userId = userHolder.get().getId();
        //查询内容数量
        int count = orderService.selectCountByUserId(userId);
        pageHelper.setCount(count);

        if (count == 0) {
            return "/member/order_list";
        }
        List<Order> contents = orderService.selectOrderListByUserId(userId, curPage, pageSize);
        pageHelper.setContents(contents);
        return "/member/order_list";
    }

    @RequestMapping(value = "/detail/{orderId}", method = RequestMethod.GET)
    public String detail(@PathVariable(value = "orderId") String orderId, Model model) {
        int userId = userHolder.get().getId();
        ViewObject vo = new ViewObject();
        if (!StringUtils.isEmpty(orderId)) {
            Order order = orderService.selectOrderById(orderId);
            if (order != null) {
                //买家信息
                User buyer = userService.selectUser(order.getUserId());
                //获取卖家信息
                OrderItem orderItem = order.getOrderItems().get(0);
                int goodsId=orderItem.getGoodsId();
                int sellerId = goodsService.selectGoods(goodsId).getOwnerId();
                User seller = userService.selectUser(sellerId);
                vo.put("order", order);
                vo.put("address", addressService.select(userHolder.get().getId()));  //收货地址信息
                vo.put("seller", seller);  //卖家信息
                vo.put("buyer", buyer);  //卖家信息

                model.addAttribute("vo", vo);
                return "/order/order_detail";

            }
        }
        vo.put("msg", "操作异常");
        model.addAttribute("vo", vo);
        return "/error";
    }



    /**
     * 检测订单支付类型
     *
     * @return
     */
    private boolean checkOrderPaymentType(Order order, int paymentType) {
        if (order.getPayType() == paymentType) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检测订单状态
     *
     * @param order
     * @param orderStatus
     * @return
     */
    private boolean checkOrderStatus(Order order, int orderStatus) {
        if (order.getStatus() == orderStatus) {
            return true;
        } else {
            return false;
        }
    }
}
