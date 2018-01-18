package cn.chenny3.secondHand.service.impl;

import cn.chenny3.secondHand.dao.UserDao;
import cn.chenny3.secondHand.model.User;
import cn.chenny3.secondHand.service.UserService;
import cn.chenny3.secondHand.util.SecondHandUtil;
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
        user.setCreated(new Date());
        user.setUpdated(user.getUpdated());
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
    public void deleteUser(int userId) {
        userDao.updateStatus(userId,0);
    }
}
