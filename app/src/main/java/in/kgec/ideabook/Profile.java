package in.kgec.ideabook;

public class Profile {
    String name,mobile,age,work,address,grnder;

    public Profile() {
    }

    public Profile(String name, String mobile, String age, String work, String address, String grnder) {
        this.name = name;
        this.mobile = mobile;
        this.age = age;
        this.work = work;
        this.address = address;
        this.grnder = grnder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGrnder() {
        return grnder;
    }

    public void setGrnder(String grnder) {
        this.grnder = grnder;
    }
}
