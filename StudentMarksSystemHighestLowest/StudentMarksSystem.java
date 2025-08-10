import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

// Abstract Person class
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

// Student class
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

// StudentManager class
class StudentManager {
    private Student[] students;
    private int studentCount;
    private String unitName;

    public StudentManager() {
        students = new Student[100];
        studentCount = 0;
        unitName = "";
    }

    // Modified readFromFile: returns true if success, false otherwise
    public boolean readFromFile(String fileName) {
        studentCount = 0;
        unitName = "";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("#") || line.isEmpty()) {
                    continue;
                }

                if (unitName.isEmpty()) {
                    unitName = line;
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

    public void displayTopFiveHighest() {
        sortDescending();
        System.out.println("Top 5 Highest Scoring Students:");
        int limit = Math.min(5, studentCount);
        for (int i = 0; i < limit; i++) {
            students[i].displayDetails();
        }
    }

    public void displayTopFiveLowest() {
        sortAscending();
        System.out.println("Top 5 Lowest Scoring Students:");
        int limit = Math.min(5, studentCount);
        for (int i = 0; i < limit; i++) {
            students[i].displayDetails();
        }
    }

    public void displayAll() {
        System.out.println("Unit: " + unitName);
        for (int i = 0; i < studentCount; i++) {
            students[i].displayDetails();
        }
    }
}

// Main program
public class StudentMarksSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StudentManager manager = new StudentManager();

        String fileName;
        while (true) {
            System.out.print("Enter filename: ");
            fileName = sc.nextLine();

            if (manager.readFromFile(fileName)) {
                break; // success
            } else {
                System.out.println("File does not exist or cannot be opened. Please try again.");
            }
        }

        manager.calculateTotals();

        int choice;
        do {
            System.out.println("\n=== MENU ===");
            System.out.println("1. Display all students");
            System.out.println("2. Show top 5 highest scoring students");
            System.out.println("3. Show top 5 lowest scoring students");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            while (!sc.hasNextInt()) {
                System.out.println("Invalid input! Please enter a number between 1 and 4.");
                sc.next(); // discard invalid input
                System.out.print("Enter choice: ");
            }
            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    manager.displayAll();
                    break;
                case 2:
                    manager.displayTopFiveHighest();
                    break;
                case 3:
                    manager.displayTopFiveLowest();
                    break;
                case 4:
                    System.out.println("Exiting program...");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        } while (choice != 4);

        sc.close();
    }
}
