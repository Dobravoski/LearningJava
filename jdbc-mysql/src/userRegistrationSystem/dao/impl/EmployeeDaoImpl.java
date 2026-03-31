package userRegistrationSystem.dao.impl;

import db.DbException;
import userRegistrationSystem.dao.interfaces.EmployeeDao;
import userRegistrationSystem.entities.Department;
import userRegistrationSystem.entities.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeDaoImpl implements EmployeeDao {

    private Connection conn;

    public EmployeeDaoImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Employee employee) {
        try(PreparedStatement ps = conn.prepareStatement("INSERT INTO employee " +
                "(name, email, birthDate, baseSalary, departmentId)" +
                " VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, employee.getName());
            ps.setString(2, employee.getEmail());
            ps.setDate(3, java.sql.Date.valueOf(employee.getBirthDate()));
            ps.setDouble(4, employee.getBaseSalary());
            ps.setInt(5, employee.getDepartment().getId());

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
    public void update(Employee employee) {
        try(PreparedStatement ps = conn.prepareStatement("UPDATE employee " +
                "SET name = ?, email = ?, birthDate = ?, baseSalary = ?, departmentId = ? " +
                "WHERE id = ?")) {

            ps.setString(1, employee.getName());
            ps.setString(2, employee.getEmail());
            ps.setDate(3, java.sql.Date.valueOf(employee.getBirthDate()));
            ps.setDouble(4, employee.getBaseSalary());
            ps.setInt(5, employee.getDepartment().getId());
            ps.setInt(6, employee.getId());

            if(ps.executeUpdate() ==  0) {
                throw new DbException("No employee found with id " + employee.getId());
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Integer id) {
        try(PreparedStatement ps = conn.prepareStatement("DELETE FROM employee WHERE id = ?")) {

            ps.setInt(1, id);

            if(ps.executeUpdate() ==  0) {
                throw new DbException("No employee found with id " + id);
            }

        } catch (SQLException e) {
            System.out.println("Error while deleting employee. " + e.getMessage());
        }
    }

    @Override
    public Employee findById(Integer id) {
        try(PreparedStatement ps = conn.prepareStatement(
                "SELECT e.Id, e.Name, e.Email, e.BirthDate, e.BaseSalary, " +
                        "d.Name AS department, d.Id AS departmentId " +
                        "FROM employee e " +
                        "INNER JOIN department d ON e.DepartmentId = d.Id " +
                        "WHERE e.Id = ?")) {

            ps.setInt(1, id);

            try(ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return new Employee(resultSet.getInt("id"), resultSet.getString("name"),
                            resultSet.getString("email"), resultSet.getDate("birthDate").toLocalDate(),
                            resultSet.getDouble("baseSalary"), new Department(resultSet.getInt("departmentId"),
                            resultSet.getString("department")));
                } else {
                    throw new DbException("No employee found with id " + id);
                }
            }
        } catch (SQLException e) {
            throw new DbException("Error while finding employee: " + e.getMessage());
        }
    }

    @Override
    public List<Employee> findAll() {
        try(PreparedStatement ps = conn.prepareStatement(
                "SELECT e.id, e.name, e.email, e.birthDate, e.baseSalary, " +
                        "d.Id AS departmentId, d.name AS departmentName " +
                        "FROM employee e " +
                        "INNER JOIN department d ON e.departmentId = d.id")) {

            List<Employee> employees = new ArrayList<>();

            try(ResultSet resultSet = ps.executeQuery()) {
                Map<Integer, Department> departmentMap = new HashMap<>();

                while (resultSet.next()) {
                    Department department = departmentMap.get(resultSet.getInt("departmentId"));

                    if (department == null) {
                        department = new Department(resultSet.getInt("departmentId"), resultSet.getString("departmentName"));
                        departmentMap.put(department.getId(), department);
                    }

                    employees.add(new Employee(resultSet.getInt("id"), resultSet.getString("name"),
                            resultSet.getString("email"), resultSet.getDate("birthDate").toLocalDate(),
                            resultSet.getDouble("baseSalary"), department));
                }
            }
            return employees;
        } catch (SQLException e) {
            throw new DbException("Error while finding all employees: " + e.getMessage());
        }
    }

    @Override
    public List<Employee> findByDepartment(Department department) {
        try(PreparedStatement ps = conn.prepareStatement(
                "SELECT e.id, e.name, e.email, e.birthDate, e.baseSalary, " +
                        "d.Id AS departmentId, d.name AS departmentName " +
                        "FROM employee e " +
                        "INNER JOIN department d ON e.departmentId = d.id " +
                        "WHERE e.departmentId = ?")) {

            ps.setInt(1, department.getId());

            List<Employee> employees = new ArrayList<>();

            try(ResultSet resultSet = ps.executeQuery()) {
                Map<Integer, Department> departmentMap = new HashMap<>();
                while (resultSet.next()) {
                    Department fullDepartment = departmentMap.get(resultSet.getInt("departmentId"));
                    if (fullDepartment == null) {
                        fullDepartment = new Department(resultSet.getInt("departmentId"), resultSet.getString("departmentName"));
                        departmentMap.put(fullDepartment.getId(), fullDepartment);
                    }
                    employees.add(new Employee(resultSet.getInt("id"), resultSet.getString("name"),
                            resultSet.getString("email"), resultSet.getDate("birthDate").toLocalDate(),
                            resultSet.getDouble("baseSalary"), fullDepartment));
                }
            }
            return employees;
        } catch (SQLException e) {
            throw new DbException("Error while finding employees by Department: " + e.getMessage());
        }
    }
}
