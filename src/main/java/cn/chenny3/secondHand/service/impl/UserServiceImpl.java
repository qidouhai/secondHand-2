package cn.chenny3.secondHand.service.impl;

import cn.chenny3.secondHand.common.bean.dto.SupplementDTO;
import cn.chenny3.secondHand.common.bean.enums.RoleType;
import cn.chenny3.secondHand.dao.UserDao;
import cn.chenny3.secondHand.model.Address;
import cn.chenny3.secondHand.model.User;
import cn.chenny3.secondHand.service.AddressService;
import cn.chenny3.secondHand.service.UserService;
import cn.chenny3.secondHand.common.utils.SecondHandUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private AddressService addressService;
    @Value("${user.avatar.default}")
    private String defaultAvatar;
    @Value("${user.money.default}")
    private Integer defaultMoney;

    @Override
    public int addUser(User user) {
        //设置默认头像
        user.setHeadUrl(defaultAvatar);
        //设置默认角色
        user.setRole(RoleType.USER);

        String salt = UUID.randomUUID().toString().replaceAll("-", "");
        user.setSalt(salt);
        user.setPassword(SecondHandUtil.MD5(user.getPassword() + user.getSalt()));
        user.setStatus(1);
        user.setMoney(defaultMoney);
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
    public User selectUser(String username) {
        return userDao.selectUserByName(username);
    }

    @Override
    public int selectCount(int status) {
        return userDao.selectCount(status);
    }

    @Override
    public int updateStatus(int id, int status) {
        return userDao.updateStatus(id, status);
    }

    @Override
    public int updateUser(User user) {
        user.setUpdated(new Date());
        return userDao.updateUser(user);
    }

    @Override
    public void deleteUser(int userId) {
        userDao.updateStatus(userId, 0);
    }

    @Override
    public void updateAuthenticateStatus(User user) {
        User temp = new User();
        temp.setId(user.getId());
        temp.setAuthenticateId(user.getAuthenticateId());
        updateUser(temp);
    }

    @Override
    public void updatePassword(int id, String password) {
        User user = selectUser(id);
        //新密码用盐进行md5加密
        user.setPassword(SecondHandUtil.MD5(password + user.getSalt()));
        user.setUpdated(new Date());
        updateUser(user);
    }

    @Override
    public void updatePhone(int id, String phone) {
        User temp = new User();
        temp.setId(id);
        temp.setPhone(phone);
        updateUser(temp);
    }

    @Override
    public void updateEmail(int id, String email) {
        User temp = new User();
        temp.setId(id);
        temp.setEmail(email);
        updateUser(temp);
    }

    @Override
    public boolean checkUniqueAtField(String fieldName, String fieldValue) {
        int count = userDao.checkCountByField(fieldName, fieldValue);
        return count == 0 ? true : false;
    }

    @Override
    public boolean checkExistAtField(User user, String fieldName, String fieldValue) throws NoSuchFieldException, IllegalAccessException {
        //通过反射获取User当前类本身拥有的的指定属性
        Field field = user.getClass().getDeclaredField(fieldName);
        //设置访问权限
        field.setAccessible(true);
        if (field.get(user).equals(fieldValue)) {
            return true;
        }
        return false;
    }

    /**
     * 补全用户信息
     * @param user userHolder.get() 当前登录用户
     * @param supplementDTO
     */
    @Override
    public void supplementInfo(User user, SupplementDTO supplementDTO) {
        //填充地址信息
        Address address = new Address();
        address.setArea(supplementDTO.getArea());
        address.setHostelId(supplementDTO.getHostelId());
        address.setHouseId(supplementDTO.getHouseId());
        //保存地址信息至数据库
        addressService.add(address);

        //填充user信息
        user.setQq(supplementDTO.getQq());
        user.setWechat(supplementDTO.getWechat());
        user.setAlipay(supplementDTO.getAlipay());
        //将address的自增id设置到user中
        user.setAddressId(address.getId());
        user.setUpdated(new Date());

        //修改用户信息
        userDao.updateUser(user);
    }

    @Override
    public List<User> selectUserList(int isDel,int start,int offset) {
        return userDao.selectUserList(isDel,start,offset);
    }

    @Override
    public int selectUserListCount(int isDel) {
        return userDao.selectUserListCount(isDel);
    }

    @Override
    public void batchUpdateStatus(int[] ids, int status) {
         userDao.batchUpdateStatus(ids,status);
    }

    @Override
    public int selectMoney(int id) {
        return userDao.selectMoney(id);
    }

    @Override
    public int rechargeMoney(int id, int money) {
        return userDao.rechargeMoney(id,money);
    }

    @Override
    public int consumeMoney(int id, int money) {
        return userDao.consumeMoney(id,money);
    }
}
