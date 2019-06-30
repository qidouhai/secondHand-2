package cn.chenny3.secondHand.service.impl;

import cn.chenny3.secondHand.dao.LoginRecordDao;
import cn.chenny3.secondHand.model.LoginRecord;
import cn.chenny3.secondHand.service.LoginRecordService;
import cn.chenny3.secondHand.common.utils.LocationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class LoginRecordServiceImpl implements LoginRecordService {
    @Autowired
    private LoginRecordDao loginRecordDao;
    @Autowired
    private LocationUtils locationUtil;

    @Override
    public int addLoginRecord(LoginRecord loginRecord) {
        //通过ip地址获取真实地理位置
        String location = "";
        try {
            //0:0:0:0:0:0:0:1
            if (loginRecord.getIp().equals("0:0:0:0:0:0:0:1")) {
                loginRecord.setIp("127.0.0.1");
            }
            location = locationUtil.getRealLocation(loginRecord.getIp());
        } catch (IOException e) {
            e.printStackTrace();
        }
        loginRecord.setAddress(location);
        //设置登录时间
        loginRecord.setLoginTime(new Date());

        return loginRecordDao.addLoginRecord(loginRecord);
    }

    @Override
    public List<LoginRecord> selectLastLoginRecord(int userId) {
        return loginRecordDao.selectLastLoginRecord(userId);
    }

    @Override
    public int selectLoginCount(int userId) {
        return loginRecordDao.selectLoginCount(userId);
    }
}
