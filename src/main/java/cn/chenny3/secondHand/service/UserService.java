package cn.chenny3.secondHand.service;


import cn.chenny3.secondHand.common.bean.dto.SupplementDTO;
import cn.chenny3.secondHand.model.User;

public interface UserService {
    int addUser(User user);
    User selectUser(int id);
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
}
