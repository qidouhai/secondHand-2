package cn.chenny3.secondHand.common.bean.dto;

/**
 * 前后端数据交互标准
 */
public class EasyResult {
    private int code;
    private Object msg;

    public EasyResult() {

    }

    public EasyResult(int code, Object msg) {
        this.code = code;
        this.msg = msg;
    }

    public EasyResult(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }
}
