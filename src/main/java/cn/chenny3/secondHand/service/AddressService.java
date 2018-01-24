package cn.chenny3.secondHand.service;

import cn.chenny3.secondHand.model.Address;

public interface AddressService {
    int add(Address address);

    int update(Address address);

    Address select(int id);

    int updateStatus(int id,int status);
}
