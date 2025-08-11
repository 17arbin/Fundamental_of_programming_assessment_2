 import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

// ===== ABSTRACT CLASS =====
abstract class Person {
    private String name;
    private String id;

    public Person(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() { return name; }
    public String getId() { return id; }

    public abstract void displayDetails();
}

// ===== STUDENT CLASS =====
class Student extends Person {
    private int mark1;
    private int mark2;
    private int mark3;
    private int total;

    public Student(String name, String id, int m1, int m2, int m3) {
        super(name, id);
        this.mark1 = m1;
        this.mark2 = m2;
        this.mark3 = m3;
        this.total = 0;
    }

    public void calculateTotal() {
        total = mark1 + mark2 + mark3;
    }

    public int getTotal() { return total; }

    @Override
    public void displayDetails() {
        System.out.println("Name: " + getName() + ", ID: " + getId()
                + ", Marks: " + mark1 + ", " + mark2 + ", " + mark3
                + ", Total: " + total);
    }
}

// ===== STUDENT MANAGER CLASS =====
class StudentManager {
    private Student[] students;
    private int studentCount;
    private String unitName;

    public StudentManager() {
        students = new Student[100]; // max 100 students
        studentCount = 0;
        unitName = "";
    }

    // Reads from file and returns true if successful, false if error
    public boolean readFromFile(String fileName) {
        studentCount = 0;
        unitName = "";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("#") || line.isEmpty()) {
                    continue; // skip comments and empty lines
                }

                if (unitName.isEmpty()) {
                    unitName = line; // first valid line is unit name
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length != 5) {
                    System.out.println("Invalid line: " + line);
                    continue;
                }

                try {
                    String name = parts[0].trim();
                    String id = parts[1].trim();
                    int m1 = Integer.parseInt(parts[2].trim());
                    int m2 = Integer.parseInt(parts[3].trim());
                    int m3 = Integer.parseInt(parts[4].trim());

                    Student s = new Student(name, id, m1, m2, m3);
                    students[studentCount++] = s;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid marks: " + line);
                }
            }
            return true; // file read successfully
        } catch (IOException e) {
            return false; // failed to open/read file
        }
    }

    public void calculateTotals() {
        for (int i = 0; i < studentCount; i++) {
            students[i].calculateTotal();
        }
    }

    public void filterByThreshold(int threshold) {
        System.out.println("Students with total marks less than " + threshold + ":");
        boolean found = false;
        for (int i = 0; i < studentCount; i++) {
            if (students[i].getTotal() < threshold) {
                students[i].displayDetails();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No students found below threshold.");
        }
    }

    public void sortAscending() {
        for (int i = 0; i < studentCount - 1; i++) {
            for (int j = 0; j < studentCount - i - 1; j++) {
                if (students[j].getTotal() > students[j + 1].getTotal()) {
                    Student temp = students[j];
                    students[j] = students[j + 1];
                    students[j + 1] = temp;
                }
            }
        }
    }

    public void sortDescending() {
        for (int i = 0; i < studentCount - 1; i++) {
            for (int j = 0; j < studentCount - i - 1; j++) {
                if (students[j].getTotal() < students[j + 1].getTotal()) {
                    Student temp = students[j];
                    students[j] = students[j + 1];
                    students[j + 1] = temp;
                }
            }
        }
    }

    public void displayAll() {
        System.out.println("Unit: " + unitName);
        for (int i = 0; i < studentCount; i++) {
            students[i].displayDetails();
        }
    }
}

// ===== MAIN PROGRAM =====
public class StudentMarksSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StudentManager manager = new StudentManager();

        String fileName;
        while (true) {
            System.out.print("Enter file name: ");
            fileName = sc.nextLine();

            if (manager.readFromFile(fileName)) {
                break; // file read successful, exit loop
            } else {
                System.out.println("File does not exist or cannot be opened. Please try again.");
            }
        }

        manager.calculateTotals();

        int choice;
        do {
            System.out.println("\n=== MENU ===");
            System.out.println("1. Display all students");
            System.out.println("2. Filter students below threshold");
            System.out.println("3. Sort ascending by total");
            System.out.println("4. Sort descending by total");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            while (!sc.hasNextInt()) {
                System.out.println("Invalid input! Please enter a number between 1 and 5.");
                sc.next(); // discard invalid input
                System.out.print("Enter choice: ");
            }
            choice = sc.nextInt();
            sc.nextLine(); // consume leftover newline

            switch (choice) {
                case 1:
                    manager.displayAll();
                    break;
                case 2:
                    int threshold;
                    do {
                        System.out.print("Enter threshold (0 to 300): ");
                        while (!sc.hasNextInt()) {
                            System.out.println("Invalid input! Please enter a number between 0 and 300.");
                            sc.next(); // discard invalid input
                            System.out.print("Enter threshold (0 to 300): ");
                        }
                        threshold = sc.nextInt();
                        sc.nextLine(); // consume leftover newline
                        if (threshold < 0 || threshold > 300) {
                            System.out.println("Invalid threshold! Please enter a value between 0 and 300.");
                        }
                    } while (threshold < 0 || threshold > 300);

                    manager.filterByThreshold(threshold);
                    break;
                case 3:
                    manager.sortAscending();
                    System.out.println("Sorted ascending by total marks.");
                    break;
                case 4:
                    manager.sortDescending();
                    System.out.println("Sorted descending by total marks.");
                    break;
                case 5:
                    System.out.println("Exiting program...");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        } while (choice != 5);

        sc.close();
    }
}
