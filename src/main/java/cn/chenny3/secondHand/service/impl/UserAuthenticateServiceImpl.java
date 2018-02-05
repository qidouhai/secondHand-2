package cn.chenny3.secondHand.service.impl;

import cn.chenny3.secondHand.dao.UserAuthenticateDao;
import cn.chenny3.secondHand.model.UserAuthenticate;
import cn.chenny3.secondHand.service.UserAuthenticateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticateServiceImpl implements UserAuthenticateService{
    @Autowired
    private UserAuthenticateDao userAuthenticateDao;

    @Override
    public int addAuthenticate(UserAuthenticate authenticate) {
        return userAuthenticateDao.addAuthenticate(authenticate);
    }

    @Override
    public UserAuthenticate selectAuthenticate(int authenticateId) {
        return userAuthenticateDao.selectAuthenticate(authenticateId);
    }

    @Override
    public UserAuthenticate selectAuthenticateByStuId(String stuId) {
        return userAuthenticateDao.selectAuthenticateByStuId(stuId);
    }


}
