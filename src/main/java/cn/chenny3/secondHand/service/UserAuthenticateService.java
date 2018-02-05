package cn.chenny3.secondHand.service;

import cn.chenny3.secondHand.model.UserAuthenticate;

public interface UserAuthenticateService {
    int addAuthenticate(UserAuthenticate authenticate);
    UserAuthenticate selectAuthenticate(int authenticateId);
    UserAuthenticate selectAuthenticateByStuId(String stuId);

}
