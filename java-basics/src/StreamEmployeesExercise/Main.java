package StreamEmployeesExercise;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Main {

    static final List<Employee> employees = EmployeeService.getListEmployees();


    static void main() {
        dictDepartmentEmployees();
        System.out.println();
        countingDepartmentEmployees();
        System.out.println();
        employeesAverageSalary();
        System.out.println();
        partitioningBy3000();
        System.out.println();
        highestSalaryPerDepartment();
    }

    public static void dictDepartmentEmployees() {
        employees.stream().collect(Collectors
                .groupingBy(Employee::getDepartment)) // Collectors.mapping(employee -> employee, Collectors.toList())
                .forEach((department, employeeList) -> {
                    System.out.printf("%s -> %s%n", department, employeeList.stream().map(Employee::getName).collect(Collectors.joining(", ")));
                });
    }

    public static void countingDepartmentEmployees() {
        employees.stream().collect(Collectors.groupingBy(Employee::getDepartment, Collectors.counting())).forEach((department, employeeCount) -> {
            System.out.printf("%s -> %d\n",  department, employeeCount);
        });
    }

    public static void employeesAverageSalary() {
        // System.out.printf("Average salary: $%s", employees.stream().collect(Collectors.averagingDouble(Employee::getSalary)));
        employees.stream().mapToDouble(Employee::getSalary).average().ifPresent(avg -> System.out.printf("Average salary: $%.2f", avg));
    }

    public static void partitioningBy3000() {
        employees.stream().collect(Collectors.partitioningBy(employee -> employee.getSalary() >= 3000))
                .forEach((condition, employeesList) ->
                        System.out.printf("\n%s -> %s", (condition) ? "Grater than 3000" : "less than 3000", employeesList.stream().map(Employee::getName).collect(Collectors.joining(", "))));
    }

    public static void highestSalaryPerDepartment() {
        employees.stream().collect(Collectors.groupingBy(Employee::getDepartment, Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparing(Employee::getSalary)), Optional::get)))
                .forEach((department, employee) -> System.out.printf("\n%s -> %s: $%.2f",  department, employee.getName(), employee.getSalary()));
    }


}
