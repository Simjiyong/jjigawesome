package kr.co.company.jjigawesome;

/**
 * Created by BHY on 2018. 7. 19..
 */

class Member {
    private String name;
    private String ID;
    private String password;
    private String email;
    private int type;
    private String token;
    private int stampCount;


    public Member(){
    }

    public Member(String name, String ID, String password, String email, int type) {
        this.name = name;
        this.ID = ID;
        this.password = password;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public int getStampCount() {
        return stampCount;
    }

    public void setStampCount(int stampCount) {
        this.stampCount = stampCount;
    }

}
