package userRegistrationSystem.application;

import db.DB;
import db.DbException;
import userRegistrationSystem.dao.impl.DepartmentDaoImpl;
import userRegistrationSystem.dao.impl.EmployeeDaoImpl;
import userRegistrationSystem.dao.interfaces.DepartmentDao;
import userRegistrationSystem.dao.interfaces.EmployeeDao;
import userRegistrationSystem.entities.Department;
import userRegistrationSystem.entities.Employee;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {
    static void main() {
        Connection conn = DB.getConnection();
        menu(conn);
        DB.closeConnection();
    }

    public static void menu(Connection conn) {
        EmployeeDao employeeDao = new EmployeeDaoImpl(conn);
        DepartmentDao departmentDao = new DepartmentDaoImpl(conn);
        var sc = new Scanner(System.in);

        int choice;
        do {
            System.out.print("""
                    
                    ==== MENU ====
                    
                    1 - List all
                    2 - Find by ID
                    3 - Insert new employee/department
                    4 - Update
                    5 - Delete
                    6 - Find employees by department
                    0 - Exit
                    """);
            choice = readInt(sc, 0, 6);

            switch (choice) {
                case 1:
                    System.out.println("""
                            Employee - 1
                            Department - 2""");
                    if (readInt(sc, 1, 2) == 1) {
                        listEmployees(employeeDao.findAll());
                    } else  {
                        listDepartment(departmentDao.findAll());
                    }
                    break;
                case 2:
                    findById(sc, employeeDao, departmentDao);
                    break;
                case 3:
                    insertEmployeeDepartment(sc, employeeDao, departmentDao);
                    break;
                case 4:
                    update(sc, employeeDao, departmentDao);
                    break;
                case 5:
                    delete(sc, employeeDao, departmentDao);
                    break;
                case 6:
                    findEmployeesByDepartment(sc, employeeDao, departmentDao);
                    break;
                case 0:
                    break;
            }
        } while (choice != 0);
    }

    public static void listEmployees(List<Employee> employees) {
        if (!employees.isEmpty()) {
            System.out.println("\n==== Employee(s) ====\n");
            employees.forEach(e -> System.out.printf("""
                            Id: %d
                            Name: %s
                            Email: %s
                            Birthdate: %s
                            Base salary: %.2f
                            Department: %s

                            """, e.getId(), e.getName(), e.getEmail(),
                    e.getBirthDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")),
                    e.getBaseSalary(), e.getDepartment().getName()));
            System.out.println("===================");
        } else {
            System.out.println("There is no employees found!");
        }
    }

    public static void listDepartment(List<Department> departments) {
        if (!departments.isEmpty()) {
            System.out.println("\n==== Department(s) ====\n");
            departments.forEach(d -> System.out.printf("""
                            Id: %d
                            Department: %s
                            
                            """, d.getId(), d.getName()));
        } else {
            System.out.println("There is no department found!");
        }
    }

    public static void findById(Scanner sc, EmployeeDao employeeDao, DepartmentDao departmentDao) {
        System.out.println("""
                            Employee - 1
                            Department - 2
                            Go back - 3""");
        int temp = readInt(sc, 1, 3);
        switch (temp) {
            case 1:
                listEmployees(List.of(findEmployeeById(sc, employeeDao)));
                break;
            case 2:
                listDepartment(List.of(findDepartmentById(sc, departmentDao)));
                break;
        }
    }

    public static Employee findEmployeeById(Scanner sc, EmployeeDao employeeDao) {
        System.out.print("Employee ID: ");
        while (true) {
            try {
                return employeeDao.findById(Integer.parseInt(sc.nextLine()));
            } catch (DbException dbException) {
                System.out.println(dbException.getMessage());
                System.out.print(">> ");
            } catch (NumberFormatException numberFormatException) {
                System.out.println("ID must be an integer");
                System.out.print(">> ");
            }
        }
    }

    public static Department findDepartmentById(Scanner sc, DepartmentDao departmentDao) {
        System.out.print("Department ID: ");
        while (true) {
            try {
                return departmentDao.findById(Integer.parseInt(sc.nextLine()));
            } catch (DbException dbException) {
                System.out.println(dbException.getMessage());
                System.out.print(">> ");
            } catch (NumberFormatException numberFormatException) {
                System.out.println("ID must be an integer");
                System.out.print(">> ");
            }
        }
    }

    public static void insertEmployeeDepartment(Scanner sc, EmployeeDao employeeDao, DepartmentDao departmentDao) {
        System.out.println("""
                            Employee - 1
                            Department - 2""");
        if (readInt(sc, 1, 2) == 1) {
            employeeDao.insert(newEmployee(sc, departmentDao));
        } else {
            departmentDao.insert(newDepartment(sc));
        }
    }

    public static Employee newEmployee(Scanner sc, DepartmentDao departmentDao) {
        System.out.println("\n===== Employee factory =====\n");
        System.out.print("Name: ");
        String name = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        LocalDate birthDate = readDate(sc); // System.out.print("Birthdate: "); inside readDate()
        System.out.print("Base Salary: ");
        double baseSalary;
        do {
            baseSalary = Double.parseDouble(sc.nextLine());
            if (baseSalary < 0) {
                System.out.println("Error: Base Salary must be a positive number");
                System.out.print(">> ");
            }
        } while (baseSalary < 0);
        Department department = findDepartmentById(sc, departmentDao); // System.out.print("Department ID: ") inside findDepartmentById()
        return new Employee(null, name, email, birthDate,  baseSalary, department); // ID auto-increment
    }

    public static Department newDepartment(Scanner sc) {
        System.out.println("\n===== Department factory =====\n");
        System.out.print("Department name: ");
        String name = sc.nextLine();
        return new Department(null, name); // ID auto-increment
    }

    public static void update(Scanner sc, EmployeeDao employeeDao, DepartmentDao departmentDao) {
        System.out.println("""
                            Employee - 1
                            Department - 2""");
        if (readInt(sc, 1, 2) == 1) {
            listEmployees(employeeDao.findAll());
            Employee employee = findEmployeeById(sc, employeeDao);
            System.out.println("""
                    1 - Name
                    2 - Email
                    3 - Birthdate
                    4 - Base Salary
                    5 - Department
                    6 - Everything""");
            switch (readInt(sc, 1, 6)) {
                case 1:
                    System.out.print("New Name: ");
                    employee.setName(sc.nextLine());
                    employeeDao.update(employee);
                    break;
                case 2:
                    System.out.print("New Email: ");
                    employee.setEmail(sc.nextLine());
                    employeeDao.update(employee);
                    break;
                case 3:
                    System.out.print("New "); // System.out.print("Birthdate: ") inside readDate()
                    employee.setBirthDate(readDate(sc));
                    employeeDao.update(employee);
                    break;
                case 4:
                    System.out.print("New Base Salary: ");
                    employee.setBaseSalary(Double.parseDouble(sc.nextLine()));
                    employeeDao.update(employee);
                    break;
                case 5:
                    System.out.print("New "); // System.out.print("Department ID: ") inside findDepartmentById()
                    employee.setDepartment(findDepartmentById(sc, departmentDao));
                    employeeDao.update(employee);
                    break;
                case 6:
                    int id = employee.getId();
                    employee = newEmployee(sc, departmentDao);
                    employee.setId(id);
                    employeeDao.update(employee);
                    break;
            }
        } else {
            listDepartment(departmentDao.findAll());
            Department department = findDepartmentById(sc, departmentDao);
            System.out.print("New Deparment name: ");
            department.setName(sc.nextLine());
            departmentDao.update(department);
        }
    }

    public static void delete(Scanner sc, EmployeeDao employeeDao, DepartmentDao departmentDao) {
        System.out.println("""
                            Employee - 1
                            Department - 2
                            Go back - 3
                            """);
        switch (readInt(sc, 1, 3)) {
            case 1:
                employeeDao.deleteById(findEmployeeById(sc, employeeDao).getId());
                break;
            case 2:
                departmentDao.deleteById(findDepartmentById(sc, departmentDao).getId());
                break;
            case 3:
                break;
        }
    }

    public static void findEmployeesByDepartment(Scanner sc, EmployeeDao employeeDao, DepartmentDao departmentDao) {
        listEmployees(employeeDao.findByDepartment(findDepartmentById(sc, departmentDao)));
    }

    public static int readInt(Scanner sc, int start, int end) {
        while (true) {
            try {
                System.out.print(">> ");
                int num = Integer.parseInt(sc.nextLine());
                if (num >= start && num <= end) {
                    return num;
                }
            } catch (NumberFormatException e) {
                System.out.printf("Invalid input, the number must be between %d and %d\n", start, end);
            }
        }
    }

    public static LocalDate readDate(Scanner sc) {
        System.out.print("Birthdate: ");
        while (true) {
            try {
                return LocalDate.parse(sc.nextLine(), DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            } catch (DateTimeParseException e) {
                System.out.println("Error: The date must be (MM/dd/yyyy)");
                System.out.print(">> ");
            }
        }
    }
}
