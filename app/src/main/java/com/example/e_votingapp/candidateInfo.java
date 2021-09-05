package com.example.e_votingapp;

public class candidateInfo {
    private String id, name, dob, gender, party, phone;
    private int votes;

    private candidateInfo(){}

    private candidateInfo(String id, String name, String dob, String gender, String party, String phone, int votes){
        this.id=id;
        this.name=name;
        this.dob=dob;
        this.gender=gender;
        this.party=party;
        this.phone=phone;
        this.votes=votes;
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

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }
}
