package com.dagim.springbatch.config;

import com.dagim.springbatch.entity.Customer;
import org.springframework.batch.item.ItemProcessor;


public class CustomerItemProcessor implements ItemProcessor<Customer, Customer> {

    @Override
    public Customer process(Customer customer) throws Exception {
//        if(customer.getCountry().equalsIgnoreCase("United States of America"))
//            return customer;
//        return null;
        return customer;
    }
}
