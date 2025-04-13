package com.frankmoley.lil.data.dao;

import com.frankmoley.lil.data.entity.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ServiceDao implements Dao<Service, UUID> {
    @Override
    public List<Service> getAll() {
        return List.of();
    }

    @Override
    public Service create(Service entity) {
        return null;
    }

    @Override
    public Optional<Service> getOne(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public Service update(Service entity) {
        return null;
    }

    @Override
    public void delete(UUID uuid) {

    }

    private List<Service> processResultSet(ResultSet rs) throws SQLException {
        List<Service> services = new ArrayList<>();
        while (rs.next()) {
            Service service = new Service();
            service.setServiceId((UUID) rs.getObject("serviceId"));
            service.setName(rs.getString("name"));
            service.setPrice(rs.getBigDecimal("price"));
            services.add(service);
        }
        return services;
    }
}
