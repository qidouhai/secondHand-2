package cn.chenny3.secondHand.service;

import cn.chenny3.secondHand.model.Cart;

import java.util.List;

public interface CartService {
    void addCart(Cart cart);

    void updateCart(Cart cart);

    void deleteCart(int cartId);

    List<Cart> selectCartList(int userId, int curPage, int pageSize);

    List<Cart> selectCartList(int userId);

    int selectCartCount(int userId);
}
