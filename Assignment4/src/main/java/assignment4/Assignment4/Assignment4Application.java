package assignment4.Assignment4;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Assignment4Application { 
    
    private static final String FILE_PATH = "shopping_list.txt";
    
    public static void main(String[] args) {
        try {
            createFileIfNotExists();
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("1. Add item to shopping list");
                System.out.println("2. View shopping list");
                System.out.println("3. Update item in shopping list");
                System.out.println("4. Delete item from shopping list");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        addItem(scanner);
                        break;
                    case 2:
                        viewItems();
                        break;
                    case 3:
                        updateItem(scanner);
                        break;
                    case 4:
                        deleteItem(scanner);
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createFileIfNotExists() throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    private static void addItem(Scanner scanner) throws IOException {
        try (FileWriter writer = new FileWriter(FILE_PATH, true);
             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            System.out.print("Enter item name: ");
            String itemName = scanner.nextLine();
            System.out.print("Enter quantity: ");
            int quantity = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (quantity <= 0) {
                System.out.println("Quantity must be a positive integer.");
                return;
            }

            bufferedWriter.write(itemName + "," + quantity + "\n");
            System.out.println("Item added successfully.");
        }
    }

    private static void viewItems() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            System.out.println("Shopping List:");
            System.out.println("Item Name\tQuantity");
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                System.out.println(parts[0] + "\t\t" + parts[1]);
            }
        }
    }

    private static void updateItem(Scanner scanner) throws IOException {
        viewItems();
        System.out.print("Enter the name of the item to update: ");
        String itemNameToUpdate = scanner.nextLine();

        List<String> updatedLines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String itemName = parts[0];
                int quantity = Integer.parseInt(parts[1]);

                if (itemName.equals(itemNameToUpdate)) {
                    found = true;
                    System.out.print("Enter new item name: ");
                    String newItemName = scanner.nextLine();
                    System.out.print("Enter new quantity: ");
                    int newQuantity = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    if (newQuantity <= 0) {
                        System.out.println("Quantity must be a positive integer.");
                        return;
                    }

                    updatedLines.add(newItemName + "," + newQuantity);
                    System.out.println("Item updated successfully.");
                } else {
                    updatedLines.add(itemName + "," + quantity);
                }
            }
        }

        if (!found) {
            System.out.println("No item found with the specified name.");
            return;
        }

        try (FileWriter writer = new FileWriter(FILE_PATH);
             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            for (String line : updatedLines) {
                bufferedWriter.write(line + "\n");
            }
        }
    }

    private static void deleteItem(Scanner scanner) throws IOException {
        viewItems();
        System.out.print("Enter the name of the item to delete: ");
        String itemNameToDelete = scanner.nextLine();

        List<String> remainingLines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String itemName = parts[0];

                if (itemName.equals(itemNameToDelete)) {
                    found = true;
                    System.out.println("Item deleted successfully.");
                } else {
                    remainingLines.add(line);
                }
            }
        }

        if (!found) {
            System.out.println("No item found with the specified name.");
            return;
        }

        try (FileWriter writer = new FileWriter(FILE_PATH);
             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            for (String line : remainingLines) {
                bufferedWriter.write(line + "\n");
            }
        }
    }
}
