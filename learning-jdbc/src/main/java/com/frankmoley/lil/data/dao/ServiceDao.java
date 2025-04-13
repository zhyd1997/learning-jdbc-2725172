package com.frankmoley.lil.data.dao;

import com.frankmoley.lil.data.entity.Service;
import com.frankmoley.lil.data.util.DatabaseUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

public class ServiceDao implements Dao<Service, UUID> {
    private static final Logger LOGGER = Logger.getLogger(ServiceDao.class.getName());
    private static final String GET_ALL = "select * from wisdom.services";
    private static final String GET_BY_ID = "select * from wisdom.services where service_id = ?";
    private static final String CREATE = "insert into wisdom.services (service_id, name, price) values (?, ?, ?)";
    private static final String UPDATE = "update wisdom.services set name = ?, price = ? where service_id = ?";
    private static final String DELETE = "delete from wisdom.services where service_id = ?";

    @Override
    public List<Service> getAll() {
        List<Service> services = new ArrayList<>();
        Connection connection = DatabaseUtils.getConnection();
        try(Statement statement = connection.createStatement()){
            ResultSet rs = statement.executeQuery(GET_ALL);
            services = this.processResultSet(rs);
        } catch (SQLException e) {
            DatabaseUtils.handleSqlException("ServiceDao.getAll", e, LOGGER);
        }
        return services;
    }

    @Override
    public Service create(Service entity) {
        UUID serviceId = UUID.randomUUID();
        Connection connection = DatabaseUtils.getConnection();
        try{
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(CREATE);
            statement.setObject(1, serviceId);
            statement.setString(2, entity.getName());
            statement.setBigDecimal(3, entity.getPrice());
            statement.execute();
            connection.commit();
            statement.close();
        }catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException sqle) {
                DatabaseUtils.handleSqlException("ServiceDao.create.rollback", sqle, LOGGER);
            }
            DatabaseUtils.handleSqlException("ServiceDao.create", e, LOGGER);
        }
        Optional<Service> service = this.getOne(serviceId);
        if (!service.isPresent()) {
            return null;
        }
        return service.get();
    }

    @Override
    public Optional<Service> getOne(UUID id) {
        try (PreparedStatement statement = DatabaseUtils.getConnection().prepareStatement(GET_BY_ID)) {
            statement.setObject(1, id);
            ResultSet rs = statement.executeQuery();
            List<Service> services = this.processResultSet(rs);
            if (services.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(services.get(0));
        } catch (SQLException e) {
            DatabaseUtils.handleSqlException("ServiceDao.getOne", e, LOGGER);
        }
        return Optional.empty();
    }

    @Override
    public Service update(Service entity) {
        Connection connection = DatabaseUtils.getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(UPDATE);
            statement.setString(1, entity.getName());
            statement.setBigDecimal(2, entity.getPrice());
            statement.setObject(3, entity.getServiceId());
            statement.execute();
            connection.commit();
            statement.close();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException sqle) {
                DatabaseUtils.handleSqlException("ServiceDao.update.rollback", sqle, LOGGER);
            }
            DatabaseUtils.handleSqlException("ServiceDao.update", e, LOGGER);
        }
        return this.getOne(entity.getServiceId()).get();
    }

    @Override
    public void delete(UUID id) {
        Connection connection = DatabaseUtils.getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(DELETE);
            statement.setObject(1, id);
            statement.execute();
            connection.commit();
            statement.close();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException sqle) {
                DatabaseUtils.handleSqlException("ServiceDao.delete.rollback", sqle, LOGGER);
            }
            DatabaseUtils.handleSqlException("ServiceDao.delete", e, LOGGER);
        }
    }

    private List<Service> processResultSet(ResultSet rs) throws SQLException {
        List<Service> services = new ArrayList<>();
        while (rs.next()) {
            Service service = new Service();
            service.setServiceId((UUID) rs.getObject("service_id"));
            service.setName(rs.getString("name"));
            service.setPrice(rs.getBigDecimal("price"));
            services.add(service);
        }
        return services;
    }
}
