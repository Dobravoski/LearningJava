package SalaryFilter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Program {
    static void main() {
        final List<User> users = getUsers();
        final var sc = new Scanner(System.in);

        System.out.print("Enter salary limit: ");
        for (String email : getEmailsFilteredBySalaryLimit(users, Double.parseDouble(sc.nextLine()))) {
            System.out.println(email);
        }

        System.out.print("Enter a letter: ");
        char  letter = sc.nextLine().toUpperCase().charAt(0);
        System.out.printf("Sum of salary of people whose name starts with '%s': %.2f\n", letter, getSumSalaryFilteredByChar(users, letter));

        sc.close();
    }

    public static List<User>  getUsers() {
        Path path = Path.of("src", "SalaryFilter", "users.csv");
        List<User> users = new ArrayList<>();
        try(var br = Files.newBufferedReader(path)) {
            String line;
            while((line = br.readLine()) != null) {
                String[] data = line.split(",");
                users.add(new User(data[0], data[1], Double.parseDouble(data[2])));
            }
        } catch (IOException e) {
            System.out.println("Error opening file");
        }
        return users;
    }

    public static List<String> getEmailsFilteredBySalaryLimit(List<User> users, double salaryLimit) {
        return users.stream().filter(user -> user.salary() > salaryLimit).map(User::email).toList();
    }

    public static double getSumSalaryFilteredByChar(List<User> users, char letter) {
        return users.stream().filter(user -> user.username().charAt(0) == letter).map(User::salary).reduce(0.0, Double::sum);
    }
}
