package com.example.e_votingapp;

public class voterInfo {
    private String id, name, dob, gender, phone;
    private Long voted;

    private voterInfo(){}

    private voterInfo(String id, String name, String dob, String gender, String phone, Long voted){
        this.id=id;
        this.name=name;
        this.dob=dob;
        this.gender=gender;
        this.phone=phone;
        this.voted=voted;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getVoted() { return voted; }

    public void setVoted(Long voted) { this.voted = voted; }
}
