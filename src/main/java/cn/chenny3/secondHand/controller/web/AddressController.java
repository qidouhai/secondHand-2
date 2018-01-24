package cn.chenny3.secondHand.controller.web;

import cn.chenny3.secondHand.commons.bean.UserHolder;
import cn.chenny3.secondHand.commons.result.EasyResult;
import cn.chenny3.secondHand.commons.vo.ViewObject;
import cn.chenny3.secondHand.controller.BaseController;
import cn.chenny3.secondHand.model.Address;
import cn.chenny3.secondHand.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AddressController extends BaseController{
    @Autowired
    private AddressService addressService;
    @Autowired
    private UserHolder userHolder;

    @RequestMapping(value = "/member/address", method = RequestMethod.GET)
    public String addressList(Model model) {
        Address address = addressService.select(userHolder.get().getAddressId());
        model.addAttribute("vo",new ViewObject().put("address",address));
        return "member/address_list";
    }

    @RequestMapping(value = "/member/address",method = RequestMethod.POST)
    @ResponseBody
    public EasyResult addAddress(Address address){
        try{
            //todo:address参数校验
            addressService.add(address);
            //同步数据
            userHolder.get().setAddressId(address.getId());
            return new EasyResult(0,"地址保存成功");
        }catch (Exception e){
            logger.error(e.getMessage());
            return new EasyResult(1,"地址保存失败");
        }
    }

    @RequestMapping(value = "/member/address",method = RequestMethod.PATCH)
    @ResponseBody
    public EasyResult updateAddress(Address address){
        try{
            //todo:address参数校验
            addressService.update(address);
            return new EasyResult(0,"地址修改成功");
        }catch (Exception e){
            logger.error(e.getMessage());
            return new EasyResult(1,"地址修改失败");
        }
    }

    @RequestMapping(value = "/member/address",method = RequestMethod.DELETE)
    @ResponseBody
    public EasyResult deleteAddress(){
        try{
            addressService.updateStatus(userHolder.get().getAddressId(),0);
            return new EasyResult(0,"地址删除成功");
        }catch (Exception e){
            logger.error(e.getMessage());
            return new EasyResult(1,"地址删除失败");
        }
    }

}
