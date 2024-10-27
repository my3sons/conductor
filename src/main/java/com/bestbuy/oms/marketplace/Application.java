package com.bestbuy.oms.marketplace;

import com.bestbuy.oms.marketplace.workers.*;
import com.netflix.conductor.client.automator.TaskRunnerConfigurer;
import com.netflix.conductor.client.http.TaskClient;
import com.netflix.conductor.client.worker.Worker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.config.ObjectMapperProvider;

import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
@SpringBootApplication
@ComponentScan(basePackages = { "com.bestbuy" })
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        TaskClient taskClient = new TaskClient();
        taskClient.setRootURI("http://localhost:8080/api/"); // Point this to the server API

        int threadCount = 2; // number of threads used to execute workers.  To avoid starvation, should be same or more than number of workers

        Worker addressStandardizationWorker = new AddressStandardizationWorker();
        Worker taxCalculationWorker = new TaxCalculationWorker();
        Worker paymentWorker = new PaymentWorker();
        Worker fraudCheckWorker = new FraudCheckWorker();
        Worker shipmentWorker = new ShipmentWorker();
        Worker settlementWorker = new SettlementWorker();
        Worker invoiceWorker = new InvoiceWorker();
        Worker postOrderProcessorWorker = new PostOrderProcessorWorker();

        // Create TaskRunnerConfigurer
        TaskRunnerConfigurer configurer =
                new TaskRunnerConfigurer.Builder(taskClient,
                        Arrays.asList(
                                addressStandardizationWorker,
                                taxCalculationWorker,
                                paymentWorker,
                                fraudCheckWorker,
                                shipmentWorker,
                                settlementWorker,
                                invoiceWorker,
                                postOrderProcessorWorker
                                     ))
                        .withThreadCount(threadCount)
                        .build();

        // Start the polling and execution of tasks
        configurer.init();
    }

}
