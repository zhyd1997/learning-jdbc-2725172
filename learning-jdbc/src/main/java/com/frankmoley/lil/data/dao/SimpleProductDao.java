package com.frankmoley.lil.data.dao;

import com.frankmoley.lil.data.util.DatabaseUtils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Logger;

public class SimpleProductDao {
    private static final Logger LOGGER = Logger.getLogger(SimpleProductDao.class.getName());
    private static final String CREATE = "select * from createProduct(?,?,?)";

    public UUID createProduct(String name, BigDecimal price, String vendorName) {
        Connection connection = DatabaseUtils.getConnection();
        UUID returnVal = null;
        try {
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(CREATE);
            statement.setString(1, name);
            statement.setBigDecimal(2, price);
            statement.setString(3, vendorName);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                returnVal = (UUID) rs.getObject("createProduct");
            }
            connection.commit();
            rs.close();
            statement.close();
        } catch (SQLException e) {
            try{
                connection.rollback();
            }catch(SQLException sqle){
                DatabaseUtils.handleSqlException("SimpleProductDao.create.rollback", sqle, LOGGER);
            }
            DatabaseUtils.handleSqlException("SimpleProductDao.create", e, LOGGER);
        }
        return returnVal;
    }
}
