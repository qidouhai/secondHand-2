package cn.chenny3.secondHand.service.impl;

import cn.chenny3.secondHand.common.bean.UserHolder;
import cn.chenny3.secondHand.dao.AddressDao;
import cn.chenny3.secondHand.dao.UserAuthenticateDao;
import cn.chenny3.secondHand.model.Address;
import cn.chenny3.secondHand.model.UserAuthenticate;
import cn.chenny3.secondHand.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressDao addressDao;
    @Autowired
    private UserAuthenticateDao userAuthenticateDao;
    @Autowired
    private UserHolder userHolder;

    @Override
    public int add(Address address) {
        address.setCreated(new Date());
        address.setUpdated(address.getCreated());
        address.setStatus(1);
        return addressDao.add(address);
    }

    @Override
    public int update(Address address) {
        address.setUpdated(new Date());
        return addressDao.update(address);
    }

    @Override
    public Address select(int id) {

        Address address = addressDao.select(id);
        if(userHolder.get()!=null){
            UserAuthenticate userAuthenticate = userAuthenticateDao.selectAuthenticate(userHolder.get().getAuthenticateId());
            address.setUserName(userAuthenticate.getName());
        }
        return address;
    }

    @Override
    public int updateStatus(int id, int status) {
        return addressDao.updateStatus(id,status);
    }
}
