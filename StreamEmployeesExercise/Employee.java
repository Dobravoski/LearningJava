package StreamEmployeesExercise;

public class Employee {
    private final String name;
    private final String department;
    private final double salary;
    private final int age;

    public Employee(String name, String department, double salary, int age) {
        this.name = name;
        this.department = department;
        this.salary = salary;
        this.age = age;
    }

    public String getName() {return name;}
    public String getDepartment() {return department;}
    public double getSalary() {return salary;}
    public int getAge() {return age;}
}
