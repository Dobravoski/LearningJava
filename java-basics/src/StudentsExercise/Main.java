package StudentsExercise;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
    static void main() {
        var sc = new Scanner(System.in);

        System.out.print("Students quantity for course A: ");
        Set<Integer> ACourse = setScanner(Integer.parseInt(sc.nextLine()), sc);
        System.out.print("Students quantity for course B: ");
        Set<Integer> BCourse = setScanner(Integer.parseInt(sc.nextLine()), sc);
        System.out.print("Students quantity for course C: ");
        Set<Integer> CCourse = setScanner(Integer.parseInt(sc.nextLine()), sc);

        ACourse.addAll(BCourse);
        ACourse.addAll(CCourse);

        System.out.println("Total students: " + ACourse.size());

        sc.close();
    }

    public static Set<Integer> setScanner(int quantity, Scanner sc) {
        Set<Integer> studentsCode = new HashSet<>();
        for (int i = 0; i < quantity; i++) {
            studentsCode.add(Integer.parseInt(sc.nextLine()));
        }
        return studentsCode;
    }
}
