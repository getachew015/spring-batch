package com.dagim.springbatch.config;

import com.dagim.springbatch.entity.Customer;
import org.springframework.batch.item.ItemProcessor;


public class CustomerItemProcessor implements ItemProcessor<Customer, Customer> {

    @Override
    public Customer process(Customer customer) throws Exception {

        return customer;
    }
}
