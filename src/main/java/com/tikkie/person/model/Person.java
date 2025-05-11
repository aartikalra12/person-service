package com.tikkie.person.handler;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class Person {
    private String id;
    
    @NotBlank(message = "First name is required")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    private String lastName;
    
    private String phone;
    private Address address;

    public Person() {}

    public Person(String firstName, String lastName, String phone, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
    }

    // Getters and setters

    public void setId(String id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getId() {
        return this.id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getPhone() {
        return this.phone;
    }

    public Address getAddress() {
        return this.address;
    }

    public static class Address {
        private String street;
        private String postalCode;
        private String city;
        private String country;


        public void setStreet(String street) {
            this.street = street;
        }

        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getStreet(){
            return this.street;
        }

        public String getPostalCode(){
            return this.postalCode;
        }

        public String getCity(){
            return this.city;
        }

        public String getCountry(){
            return this.country;
        }
        
    }
}
