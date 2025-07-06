import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;

public class StudentApp extends Application {

    private final String URL = "jdbc:mysql://localhost:3306/studentdb";
    private final String USER = "root";
    private final String PASSWORD = "";

    private TableView<StudentData> tableView = new TableView<>();
    private ObservableList<StudentData> studentList = FXCollections.observableArrayList();
    private TextField idField = new TextField();
    private TextField nameField = new TextField();
    private TextField emailField = new TextField();

    @Override
    public void start(Stage stage) {
        // Configure TableView columns
        TableColumn<StudentData, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cellData -> 
            new SimpleIntegerProperty(cellData.getValue().getId()).asObject());

        TableColumn<StudentData, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getName()));

        TableColumn<StudentData, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getEmail()));

        tableView.getColumns().addAll(idCol, nameCol, emailCol);

        // Input fields setup
        idField.setPromptText("ID");
        nameField.setPromptText("Name");
        emailField.setPromptText("Email");
        HBox inputFields = new HBox(10, idField, nameField, emailField);

        // Buttons setup
        Button viewBtn = new Button("View Data");
        Button insertBtn = new Button("Insert");
        Button updateBtn = new Button("Update");
        Button deleteBtn = new Button("Delete");

        viewBtn.setOnAction(e -> loadData());
        insertBtn.setOnAction(e -> insertStudent());
        updateBtn.setOnAction(e -> updateStudent());
        deleteBtn.setOnAction(e -> deleteStudent());

        HBox buttonRow = new HBox(10, viewBtn, insertBtn, updateBtn, deleteBtn);

        // Footer
        Label footer = new Label("Name: Niraj Bhandari | ID: 23093760 | Date: " + java.time.LocalDate.now());

        // Layout
        VBox layout = new VBox(15, tableView, inputFields, buttonRow, footer);
        layout.setStyle("-fx-padding: 20;");

        // Scene and stage
        Scene scene = new Scene(layout, 700, 500);
        stage.setTitle("Student Management System");
        stage.setScene(scene);
        stage.show();

        // Load initial data
        loadData();
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private void loadData() {
        studentList.clear();
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM students")) {
            
            while (rs.next()) {
                studentList.add(new StudentData(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email")
                ));
            }
            tableView.setItems(studentList);
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load data: " + e.getMessage());
        }
    }

    private void insertStudent() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();

        if (name.isEmpty() || email.isEmpty()) {
            showAlert("Input Error", "Name and email cannot be empty");
            return;
        }

        String sql = "INSERT INTO students (name, email) VALUES (?, ?)";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.executeUpdate();

            // Get generated ID
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    idField.setText(String.valueOf(rs.getInt(1)));
                }
            }
            showAlert("Success", "Student added successfully");
            loadData();
        } catch (SQLException e) {
            showAlert("Database Error", "Insert failed: " + e.getMessage());
        }
    }

    private void updateStudent() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();

            if (name.isEmpty() || email.isEmpty()) {
                showAlert("Input Error", "Name and email cannot be empty");
                return;
            }

            String sql = "UPDATE students SET name = ?, email = ? WHERE id = ?";
            try (Connection conn = connect();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setInt(3, id);
                int affectedRows = stmt.executeUpdate();

                if (affectedRows > 0) {
                    showAlert("Success", "Student updated successfully");
                    loadData();
                } else {
                    showAlert("Error", "No student found with ID " + id);
                }
            }
        } catch (NumberFormatException e) {
            showAlert("Input Error", "ID must be a number");
        } catch (SQLException e) {
            showAlert("Database Error", "Update failed: " + e.getMessage());
        }
    }

    private void deleteStudent() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            
            String sql = "DELETE FROM students WHERE id = ?";
            try (Connection conn = connect();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setInt(1, id);
                int affectedRows = stmt.executeUpdate();

                if (affectedRows > 0) {
                    showAlert("Success", "Student deleted successfully");
                    clearFields();
                    loadData();
                } else {
                    showAlert("Error", "No student found with ID " + id);
                }
            }
        } catch (NumberFormatException e) {
            showAlert("Input Error", "ID must be a number");
        } catch (SQLException e) {
            showAlert("Database Error", "Delete failed: " + e.getMessage());
        }
    }

    private void clearFields() {
        idField.clear();
        nameField.clear();
        emailField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
