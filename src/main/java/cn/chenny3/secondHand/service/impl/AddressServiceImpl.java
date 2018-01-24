package cn.chenny3.secondHand.service.impl;

import cn.chenny3.secondHand.dao.AddressDao;
import cn.chenny3.secondHand.model.Address;
import cn.chenny3.secondHand.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressDao addressDao;

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
        return addressDao.select(id);
    }

    @Override
    public int updateStatus(int id, int status) {
        return addressDao.updateStatus(id,status);
    }
}
