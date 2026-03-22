package StreamEmployeesExercise;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class EmployeeService {
    private static List<String> getStringEmployees() {
        List<String> logs = new ArrayList<>();
        final Path path = Path.of("src", "StreamEmployeesExercise", "Employees.csv");
        try(var br = Files.newBufferedReader(path)) {
            String line;
            while((line = br.readLine()) != null) {
                logs.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error opening file: " + e.getMessage());
        }
        return logs;
    }

    private static List<Employee> mapStringToEmployee(List<String> employees) {
        return employees.stream().map(log -> {
            String[] data = log.split(";");
            return new Employee(data[0], data[1], Double.parseDouble(data[2]), Integer.parseInt(data[3]));
        }).toList();
    }

    public static List<Employee> getListEmployees() {
        return mapStringToEmployee(getStringEmployees());
    }
}
