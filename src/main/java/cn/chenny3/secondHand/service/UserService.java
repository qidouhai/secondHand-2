package cn.chenny3.secondHand.service;


import cn.chenny3.secondHand.common.bean.dto.SupplementDTO;
import cn.chenny3.secondHand.model.User;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface UserService {
    int addUser(User user);
    User selectUser(int id);
    User selectUser(String username);
    int selectCount(int status);
    int updateStatus(int id,int status);
    int updateUser(User user);
    void deleteUser(int userId);

    void updateAuthenticateStatus(User user);

    void updatePassword(int id, String password);

    void updatePhone(int id, String phone);

    void updateEmail(int id, String email);

    boolean checkUniqueAtField(String fieldName, String fieldValue);

    boolean checkExistAtField(User user, String fieldName, String fieldValue) throws NoSuchFieldException, IllegalAccessException;

    void supplementInfo(User user, SupplementDTO supplementDTO);

    List<User> selectUserList(int isDel,int start,int offset);

    int selectUserListCount(int isDel);

    void batchUpdateStatus(int ids[],int status);

    //查询用户金额
    int selectMoney(int id);

    //充值金额
    int rechargeMoney( int id, int money);

    //消费金额
    int consumeMoney(int id, int money);
}
