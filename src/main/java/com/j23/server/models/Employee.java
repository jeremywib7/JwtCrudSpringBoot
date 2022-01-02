package com.j23.server.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Entity
public class Employee implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    private String firstName;
    private String lastName;
    private String gender;
    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private java.sql.Date dateJoined;

    private Long phoneNumber;
    private String address;
    private String imageUrl;
    private String employeeCode;
    private Long bankAccount;

    public Employee(Long id, String firstName, String lastName, String gender, String email, Date dateJoined, Long phoneNumber, String address, String imageUrl, String employeeCode, Long bankAccount) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.dateJoined = dateJoined;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.imageUrl = imageUrl;
        this.employeeCode = employeeCode;
        this.bankAccount = bankAccount;
    }

    public Employee() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public java.sql.Date getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(java.sql.Date dateJoined) {
        this.dateJoined = dateJoined;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
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
                ", name='" + firstName + '\'' +
                ", gender='" + gender + '\'' +
                ", dateJoined=" + dateJoined +
                ", phoneNumber=" + phoneNumber +
                ", address='" + address + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", bankAccount=" + bankAccount +
                '}';
    }
}
