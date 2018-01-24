package cn.chenny3.secondHand.service.impl;

import cn.chenny3.secondHand.dao.UserDao;
import cn.chenny3.secondHand.model.User;
import cn.chenny3.secondHand.service.UserService;
import cn.chenny3.secondHand.commons.utils.SecondHandUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserDao userDao;

    @Override
    public int addUser(User user) {
        String salt= UUID.randomUUID().toString().replaceAll("-","");
        user.setSalt(salt);
        user.setPassword(SecondHandUtil.MD5(user.getPassword()+user.getSalt()));
        user.setStatus(1);
        user.setAuthenticateId(0);
        user.setAddressId(0);
        user.setCreated(new Date());
        user.setUpdated(user.getCreated());
        return userDao.addUser(user);
    }

    @Override
    public User selectUser(int id) {
        return userDao.selectUser(id);
    }

    @Override
    public int selectCount(int status) {
        return userDao.selectCount(status);
    }

    @Override
    public int updateStatus(int id, int status) {
        return userDao.updateStatus(id,status);
    }

    @Override
    public int updateUser(User user) {
        user.setUpdated(new Date());
        return userDao.updateUser(user);
    }

    @Override
    public void deleteUser(int userId) {
        userDao.updateStatus(userId,0);
    }

    @Override
    public void updateAuthenticateStatus(User user) {
        User temp = new User();
        temp.setId(user.getId());
        temp.setAuthenticateId(user.getAuthenticateId());
        updateUser(temp);
    }
}
