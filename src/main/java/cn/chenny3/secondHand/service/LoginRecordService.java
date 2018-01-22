package cn.chenny3.secondHand.service;

import cn.chenny3.secondHand.model.LoginRecord;

import java.util.List;

public interface LoginRecordService {
    int addLoginRecord(LoginRecord loginRecord);
    List<LoginRecord> selectLastLoginRecord(int userId);
}
