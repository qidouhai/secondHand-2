package cn.chenny3.secondHand.service;


import cn.chenny3.secondHand.model.User;

public interface UserService {
    int addUser(User user);
    User selectUser(int id);
    int selectCount(int status);
    int updateStatus(int id,int status);
    int updateUser(User user);
    void deleteUser(int userId);

    void updateAuthenticateStatus(User user);
}
