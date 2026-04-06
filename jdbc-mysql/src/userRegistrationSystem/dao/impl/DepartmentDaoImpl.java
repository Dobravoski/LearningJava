package userRegistrationSystem.dao.impl;

import db.DbException;
import db.DbIntegrityException;
import userRegistrationSystem.dao.interfaces.DepartmentDao;
import userRegistrationSystem.entities.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DepartmentDaoImpl implements DepartmentDao {

    private Connection conn;

    public DepartmentDaoImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Department department) {
        try(PreparedStatement ps = conn.prepareStatement("INSERT INTO department " +
                    "(name) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, department.getName());

            if (ps.executeUpdate() > 0) {
                try(ResultSet rs = ps.getGeneratedKeys()) {
                    rs.next();
                    System.out.println("ID: " + rs.getInt(1));
                }
            } else  {
                throw new DbException("Error while inserting employee record");
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void update(Department department) {
        try(PreparedStatement ps = conn.prepareStatement("UPDATE department " +
                "SET name = ? WHERE id = ?")) {

            ps.setString(1, department.getName());
            ps.setInt(2, department.getId());

            if(ps.executeUpdate() ==  0) {
                throw new DbException("Error while updating department");
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Integer id) {
        try(PreparedStatement ps = conn.prepareStatement("DELETE FROM department WHERE id = ?")) {
            ps.setInt(1, id);
            if(ps.executeUpdate() ==  0) {
                throw new DbException("No department found with id " + id);
            }
        } catch (SQLException e) {
            if (e.getSQLState().startsWith("23")) { // 23xxx code -> SQL Integrity standard error
                throw new DbIntegrityException("Integrity violation: department is in use");
            }
            throw new DbException("Error while deleting department. " + e.getMessage());
        }
    }

    @Override
    public Department findById(Integer id) {
        try(PreparedStatement ps = conn.prepareStatement("SELECT * FROM department WHERE id = ?")) {

            ps.setInt(1, id);

            try(ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return new Department(resultSet.getInt("id"), resultSet.getString("name"));
                } else {
                    throw new DbException("No department found with id " + id);
                }
            }
        } catch (SQLException e) {
            throw new DbException("Error while finding employee: " + e.getMessage());
        }
    }

    @Override
    public List<Department> findAll() {
        try(PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM department ORDER BY department.id")) {

            List<Department> departments = new ArrayList<>();

            try(ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    departments.add(new Department(resultSet.getInt("id"), resultSet.getString("name")));
                }
            }
            return departments;
        } catch (SQLException e) {
            throw new DbException("Error while finding all departments: " + e.getMessage());
        }
    }
}
