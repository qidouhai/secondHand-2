package cn.chenny3.secondHand.common.bean.enums;


public enum RoleType {
    Admin(0), USER(1), Login(2), All(3);
    private int value;

    RoleType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static RoleType getRole(int value) {
        if (value == 0) {
            return Admin;
        } else if (value == 1) {
            return USER;
        } else if (value == 2) {
            return Login;
        } else if (value == 3) {
            return All;
        } else {
            return null;
        }
    }

}
