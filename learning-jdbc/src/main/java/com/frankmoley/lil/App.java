package com.frankmoley.lil;

import com.frankmoley.lil.data.dao.ServiceDao;
import com.frankmoley.lil.data.entity.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        ServiceDao serviceDao = new ServiceDao();

        List<Service> services = serviceDao.getAll();
        System.out.println("**** SERVICES ****");
        System.out.println("\n*** GET ALL ***");
        services.forEach(System.out::println);

        Optional<Service> service = serviceDao.getOne(services.get(0).getServiceId());
        System.out.println("\n*** GET ONE ***\n");
        System.out.println(service.get());

        Service newService = new Service();
        newService.setName("FooBarBaz" + System.currentTimeMillis());
        newService.setPrice(new BigDecimal(10.35));
        Service createdService= serviceDao.create(newService);
        System.out.println("\n*** CREATE ***\n");
        System.out.println(createdService);

        createdService.setPrice(new BigDecimal(8.35));
        Service updatedService= serviceDao.update(createdService);
        System.out.println("\n*** UPDATE ***\n");
        System.out.println(updatedService);

        serviceDao.delete(updatedService.getServiceId());
        System.out.println("\n*** DELETE ***\n");
    }
}
