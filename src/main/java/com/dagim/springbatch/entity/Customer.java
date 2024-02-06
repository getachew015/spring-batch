package com.dagim.springbatch.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CUSTOMERS_INFO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Id
    @Column(name = "INDEX")
    private int index;
    @Column(name = "CUSTOMER_ID")
    private String customerId;
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "LAST_NAME")
    private String lastName;
    @Column(name = "COMPANY")
    private String company;
    @Column(name = "CITY")
    private String city;
    @Column(name = "COUNTRY")
    private String country;
    @Column(name = "PHONE1")
    private String phone1;
    @Column(name = "PHONE2")
    private String phone2;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "SUBSCRIPTION_DATE")
    private String subscriptionDate;
    @Column(name = "WEBSITE")
    private String website;

}
