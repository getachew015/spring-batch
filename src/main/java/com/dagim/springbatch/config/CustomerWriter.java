package com.dagim.springbatch.config;

import com.dagim.springbatch.entity.Customer;
import com.dagim.springbatch.repository.CustomerRepository;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class CustomerWriter implements ItemWriter<Customer> {

    @Autowired
    private CustomerRepository customerRepository;
    private int insertCounter;
    @Override
    public void write(List<? extends Customer> list) throws Exception {
        insertCounter++;
        log.info("Insert Performed by Thread - {} and current bulk insert count - {}",Thread.currentThread().getName(), insertCounter);
        customerRepository.saveAll(list);
    }
}
