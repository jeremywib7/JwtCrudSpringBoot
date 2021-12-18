package com.j23.server.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.security.Timestamp;
import java.time.LocalDate;
import java.util.Date;

@Entity
public class Member implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    private String name;
    private String gender;

    @JsonFormat(pattern = "YYYY-MM-DD", shape = JsonFormat.Shape.STRING)
    private LocalDate dateJoined;

    private Long phoneNumber;
    private String rank;
    private String nationality;
    private String address;
    private String imageUrl;
    private String memberCode;
    private Long bankAccount;

    public Member(Long id, String name, String gender, LocalDate dateJoined, Long phoneNumber, String rank, String nationality, String address, String imageUrl, String memberCode, Long bankAccount) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.dateJoined = dateJoined;
        this.phoneNumber = phoneNumber;
        this.rank = rank;
        this.nationality = nationality;
        this.address = address;
        this.imageUrl = imageUrl;
        this.memberCode = memberCode;
        this.bankAccount = bankAccount;
    }

    public Member() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(LocalDate dateJoined) {
        this.dateJoined = dateJoined;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public Long getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(Long bankAccount) {
        this.bankAccount = bankAccount;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", dateJoined=" + dateJoined +
                ", phoneNumber=" + phoneNumber +
                ", rank='" + rank + '\'' +
                ", nationality='" + nationality + '\'' +
                ", address='" + address + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", memberCode='" + memberCode + '\'' +
                ", bankAccount=" + bankAccount +
                '}';
    }
}
