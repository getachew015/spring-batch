package com.dagim.springbatch.config;

import com.dagim.springbatch.entity.Customer;
import com.dagim.springbatch.repository.CustomerRepository;
import lombok.extern.java.Log;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Log
public class CustomerWriter implements ItemWriter<Customer> {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void write(List<? extends Customer> list) throws Exception {
        log.info("Insert Performed by Thread ... "+Thread.currentThread().getName());
        customerRepository.saveAll(list);
    }
}
