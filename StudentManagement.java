import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class StudentManagement extends JFrame {

    JTextField nameField, ageField, courseField, searchField;
    JButton addButton, deleteButton, editButton, toggleThemeButton;
    JTable table;
    DefaultTableModel tableModel;
    List<Student> students = new ArrayList<>();
    boolean darkMode = false;

    // 🎨 Color Palette
    Color primary = new Color(10, 186, 181);
    Color secondary = new Color(86, 223, 207);
    Color background = new Color(173, 238, 217);
    Color light = new Color(255, 237, 243);

    public StudentManagement() {
        setTitle("Student Management App");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(light);
        searchField = new JTextField(20);
        searchField.setToolTipText("Search by name or course...");
        searchPanel.add(new JLabel("🔍 Search:"));
        searchPanel.add(searchField);
        add(searchPanel, BorderLayout.BEFORE_FIRST_LINE);

        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                filterTable(searchField.getText().trim().toLowerCase());
            }
        });

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Student Form"));
        inputPanel.setBackground(light);

        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Age:"));
        ageField = new JTextField();
        inputPanel.add(ageField);

        inputPanel.add(new JLabel("Course:"));
        courseField = new JTextField();
        inputPanel.add(courseField);

        addButton = new JButton("Add Student");
        addButton.setBackground(primary);
        addButton.setForeground(Color.WHITE);
        inputPanel.add(addButton);
        addButton.addActionListener(e -> addStudent());

        deleteButton = new JButton("Delete Selected");
        deleteButton.setBackground(secondary);
        deleteButton.setForeground(Color.BLACK);
        inputPanel.add(deleteButton);
        deleteButton.addActionListener(e -> deleteStudent());

        editButton = new JButton("Edit Selected");
        editButton.setBackground(new Color(255, 204, 100));
        editButton.setForeground(Color.BLACK);
        inputPanel.add(editButton);
        editButton.addActionListener(e -> editStudent());

        toggleThemeButton = new JButton("🌙 Toggle Theme");
        toggleThemeButton.setBackground(new Color(200, 200, 200));
        toggleThemeButton.setForeground(Color.BLACK);
        inputPanel.add(toggleThemeButton);
        toggleThemeButton.addActionListener(e -> toggleTheme());

        add(inputPanel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(new String[]{"Name", "Age", "Course"}, 0);
        table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true); // Sorting by columns
        table.setBackground(background);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(24);
        add(new JScrollPane(table), BorderLayout.CENTER);

        getContentPane().setBackground(light);
        setVisible(true);
    }

    void addStudent() {
        String name = nameField.getText();
        String ageText = ageField.getText();
        String course = courseField.getText();

        if (name.isEmpty() || ageText.isEmpty() || course.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        try {
            int age = Integer.parseInt(ageText);
            Student student = new Student(name, age, course);
            students.add(student);
            tableModel.addRow(new Object[]{name, age, course});

            nameField.setText("");
            ageField.setText("");
            courseField.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age must be a number.");
        }
    }

    void deleteStudent() {
        int row = table.getSelectedRow();
        if (row != -1) {
            students.remove(row);
            tableModel.removeRow(row);
        } else {
            JOptionPane.showMessageDialog(this, "Select a student to delete.");
        }
    }

    void editStudent() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a student to edit.");
            return;
        }

        String name = nameField.getText();
        String ageText = ageField.getText();
        String course = courseField.getText();

        if (name.isEmpty() || ageText.isEmpty() || course.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        try {
            int age = Integer.parseInt(ageText);
            Student updated = new Student(name, age, course);
            students.set(row, updated);

            tableModel.setValueAt(name, row, 0);
            tableModel.setValueAt(age, row, 1);
            tableModel.setValueAt(course, row, 2);

            nameField.setText("");
            ageField.setText("");
            courseField.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age must be a number.");
        }
    }

    void filterTable(String query) {
        tableModel.setRowCount(0);
        for (Student s : students) {
            if (s.name.toLowerCase().contains(query) || s.course.toLowerCase().contains(query)) {
                tableModel.addRow(new Object[]{s.name, s.age, s.course});
            }
        }
    }

    void toggleTheme() {
        Color darkBg = new Color(45, 45, 45);
        Color darkText = new Color(220, 220, 220);
        Color darkPanel = new Color(60, 63, 65);

        if (!darkMode) {
            getContentPane().setBackground(darkPanel);
            table.setBackground(darkBg);
            table.setForeground(darkText);
            table.setSelectionBackground(new Color(80, 80, 80));
        } else {
            getContentPane().setBackground(light);
            table.setBackground(background);
            table.setForeground(Color.BLACK);
            table.setSelectionBackground(new Color(200, 200, 255));
        }
        darkMode = !darkMode;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentManagement::new);
    }
}

class Student {
    String name;
    int age;
    String course;

    Student(String name, int age, String course) {
        this.name = name;
        this.age = age;
        this.course = course;
    }
}
