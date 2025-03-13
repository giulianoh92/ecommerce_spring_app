package com.ecommerce.main.services.Users.dto;

import com.ecommerce.main.domain.models.Users;

public class UserGetDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phoneNumber;

    public UserGetDTO() {
    }

    public UserGetDTO(Long id, String firstName, String lastName, String email, String address, String phoneNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public static UserGetDTO mapToDto(Users user) {
        return new UserGetDTO(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getAddress(),
            user.getPhoneNumber()
        );
    }
}
