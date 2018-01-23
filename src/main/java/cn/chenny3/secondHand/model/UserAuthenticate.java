package cn.chenny3.secondHand.model;

public class UserAuthenticate {
    private int id;
    private String stuId;
    private String name;
    private String sex;
    private String schoolName;
    private String deptName;
    private String subjectName;
    private String className;
    private int registerYear;

    public UserAuthenticate() {
    }

    public UserAuthenticate(String stuId, String name, String sex, String schoolName, String deptName, String subjectName, String className, int registerYear) {
        this.stuId = stuId;
        this.name = name;
        this.sex = sex;
        this.schoolName = schoolName;
        this.deptName = deptName;
        this.subjectName = subjectName;
        this.className = className;
        this.registerYear = registerYear;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getRegisterYear() {
        return registerYear;
    }

    public void setRegisterYear(int registerYear) {
        this.registerYear = registerYear;
    }
}
