package com.example.lambda;


import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

// Sample model representing an Employee
class Employee {
    private final String name;
    private final String department;
    private final double salary;

    public Employee(String name, String department, double salary) {
        this.name = name;
        this.department = department;
        this.salary = salary;
    }

    public String getName() { return name; }
    public String getDepartment() { return department; }
    public double getSalary() { return salary; }

    @Override
    public String toString() {
        return String.format("Employee{name='%s', dept='%s', salary=%.2f}", name, department, salary);
    }
}

public class LambdaOperationsMastery {

    public static void main(String[] args) {

        List<Employee> employees = List.of(
                new Employee("Alice", "Engineering", 95000),
                new Employee("Bob", "HR", 60000),
                new Employee("Charlie", "Engineering", 120000),
                new Employee("Diana", "Marketing", 75000),
                new Employee("Ethan", "HR", 55000),
                new Employee("Fiona", "Engineering", 110000)
        );

        // =========================================================================
        // 1. Extracting Data using map()
        // =========================================================================
        System.out.println("--- 1. EXTRACTING DATA WITH MAP ---");
        // Extracting employee names (transforming Employee object -> String)
        List<String> employeeNames = employees.stream()
                .map(Employee::getName)
                .collect(Collectors.toList());

        System.out.println("Employee Names: " + employeeNames);
        System.out.println();


        // =========================================================================
        // 2. Types of Stream Operations (Intermediate vs Terminal)
        // =========================================================================
        // Intermediate: Lazy operations that return a new Stream (e.g., filter, map, sorted).
        // Terminal: Eager operations that produce a result or side-effect (e.g., collect, count, findFirst).
        System.out.println("--- 2. STREAM OPERATION TYPES ---");
        long engineeringCount = employees.stream()
                .filter(e -> "Engineering".equals(e.getDepartment())) // Intermediate
                .count();                                             // Terminal

        System.out.println("Engineering Employee Count: " + engineeringCount);
        System.out.println();


        // =========================================================================
        // 3. Describing and Using the Optional Class
        // =========================================================================
        // Optional is a container object used to represent the presence or absence of a value,
        // helping to prevent NullPointerExceptions.
        System.out.println("--- 3. OPTIONAL CLASS ---");

        // Example A: Finding the highest paid employee
        Optional<Employee> topEarner = employees.stream()
                .max(Comparator.comparingDouble(Employee::getSalary));

        topEarner.ifPresent(e -> System.out.println("Highest Paid: " + e.getName() + " ($" + e.getSalary() + ")"));

        // Example B: Handling potential empty result safely
        Optional<Employee> missingEmp = employees.stream()
                .filter(e -> e.getName().equals("NonExistent"))
                .findFirst();

        Employee fallback = missingEmp.orElse(new Employee("Default User", "None", 0));
        System.out.println("Fallback Result: " + fallback.getName());
        System.out.println();


        // =========================================================================
        // 4. Lazy Processing Demonstration
        // =========================================================================
        // Stream operations do NOT execute until a terminal operation is called.
        // Intermediate operations are queued up and processed in a single pass.
        System.out.println("--- 4. LAZY PROCESSING ---");

        System.out.println("Defining pipeline (no execution yet)...");
        var lazyStream = employees.stream()
                .filter(e -> {
                    System.out.println("Filtering: " + e.getName());
                    return e.getSalary() > 70000;
                })
                .map(e -> {
                    System.out.println("Mapping: " + e.getName());
                    return e.getName().toUpperCase();
                });

        System.out.println("Triggering terminal operation (findFirst)...");
        // Notice it processes only until the first match satisfies the pipeline (Short-circuiting)
        Optional<String> firstHighSalaryName = lazyStream.findFirst();
        firstHighSalaryName.ifPresent(name -> System.out.println("First Match: " + name));
        System.out.println();


        // =========================================================================
        // 5. Sorting a Stream
        // =========================================================================
        System.out.println("--- 5. SORTING A STREAM ---");
        // Sorting by salary descending, then by name ascending
        List<Employee> sortedEmployees = employees.stream()
                .sorted(Comparator.comparingDouble(Employee::getSalary).reversed()
                        .thenComparing(Employee::getName))
                .collect(Collectors.toList());

        System.out.println("Employees sorted by salary (descending):");
        sortedEmployees.forEach(System.out::println);
        System.out.println();


        // =========================================================================
        // 6. Saving Results using collect()
        // =========================================================================
        System.out.println("--- 6. SAVING RESULTS WITH COLLECT ---");

        // Collect into a Set
        Set<String> uniqueDepartments = employees.stream()
                .map(Employee::getDepartment)
                .collect(Collectors.toSet());
        System.out.println("Unique Departments: " + uniqueDepartments);

        // Collect into a Map (Key: Name, Value: Salary)
        Map<String, Double> nameToSalaryMap = employees.stream()
                .collect(Collectors.toMap(Employee::getName, Employee::getSalary));
        System.out.println("Name -> Salary Map: " + nameToSalaryMap);
        System.out.println();


        // =========================================================================
        // 7. Grouping and Partitioning Data using Collectors
        // =========================================================================
        System.out.println("--- 7. GROUPING & PARTITIONING DATA ---");

        // Grouping: Categorize elements into a Map by a classifier function
        Map<String, List<Employee>> employeesByDept = employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment));

        System.out.println("Grouped by Department:");
        employeesByDept.forEach((dept, empList) -> {
            System.out.println("  " + dept + ": " + empList);
        });

        // Partitioning: Special case of grouping that splits data into two boolean buckets (true / false)
        Map<Boolean, List<Employee>> highSalaryPartition = employees.stream()
                .collect(Collectors.partitioningBy(e -> e.getSalary() >= 80000));

        System.out.println("\nPartitioned by Salary >= $80,000:");
        System.out.println("  Earns >= 80k: " + highSalaryPartition.get(true));
        System.out.println("  Earns < 80k:  " + highSalaryPartition.get(false));
    }
}