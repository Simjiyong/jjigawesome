package kr.co.company.jjigawesome;

/**
 * Created by BHY on 2018. 7. 19..
 */

class Member {
    private String name;
    private String ID;
    private String password;
    private String phone_number;
    private int type;
    private String token;

    public Member(){
    }

    public Member(String name, String ID, String password, String phone_number, int type) {
        this.name = name;
        this.ID = ID;
        this.password = password;
        this.phone_number = phone_number;
        this.type = type;
    }

    public Member(String ID, String password) {
        this.ID = ID;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
