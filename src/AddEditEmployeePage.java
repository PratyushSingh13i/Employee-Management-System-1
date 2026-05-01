import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class AddEditEmployeePage {

    // Colors
    private static final Color BG_DARK      = new Color(15, 23, 42);
    private static final Color CARD_BG      = new Color(30, 41, 59);
    private static final Color ACCENT_BLUE  = new Color(59, 130, 246);
    private static final Color ACCENT_GREEN = new Color(34, 197, 94);
    private static final Color ACCENT_RED   = new Color(239, 68, 68);
    private static final Color TEXT_WHITE   = new Color(241, 245, 249);
    private static final Color TEXT_MUTED   = new Color(148, 163, 184);
    private static final Color INPUT_BG     = new Color(51, 65, 85);
    private static final Color BORDER_COLOR = new Color(71, 85, 105);

    private JFrame frame;
    private EmployeeDAO dao = new EmployeeDAO();
    private EmployeeGUI parentGUI;

    // Mode: true = Add, false = Edit
    private boolean isAddMode;
    private int editEmpId = -1;

    // Fields
    private JTextField nameField, ageField, deptField, salaryField;
    private JTextField emailField, phoneField, roleField;
    private JComboBox<String> statusBox;

    public AddEditEmployeePage(EmployeeGUI parent, boolean addMode, int empId) {
        this.parentGUI = parent;
        this.isAddMode = addMode;
        this.editEmpId = empId;

        frame = new JFrame(addMode ? "Add New Employee" : "Edit Employee");
        frame.setSize(600, 620);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setBackground(BG_DARK);
        frame.setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(20, 30, 55));
        header.setBorder(BorderFactory.createEmptyBorder(18, 25, 18, 25));

        JLabel title = new JLabel(addMode ? "➕  Add New Employee" : "✏️  Edit Employee");
        title.setFont(new Font("Georgia", Font.BOLD, 22));
        title.setForeground(TEXT_WHITE);
        header.add(title, BorderLayout.WEST);

        JLabel subtitle = new JLabel(addMode ? "Fill in all fields to add a new employee" : "Update the employee information below");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitle.setForeground(TEXT_MUTED);
        header.add(subtitle, BorderLayout.SOUTH);

        frame.add(header, BorderLayout.NORTH);

        // Main form panel
        JPanel formPanel = new JPanel();
        formPanel.setBackground(BG_DARK);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        // ---- Section: Basic Info ----
        formPanel.add(sectionLabel("Basic Information"));
        formPanel.add(Box.createVerticalStrut(10));

        JPanel basicGrid = new JPanel(new GridLayout(2, 2, 15, 12));
        basicGrid.setOpaque(false);
        basicGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        nameField   = createField();
        ageField    = createField();
        deptField   = createField();
        salaryField = createField();

        basicGrid.add(labeledField("Full Name", nameField));
        basicGrid.add(labeledField("Age", ageField));
        basicGrid.add(labeledField("Department", deptField));
        basicGrid.add(labeledField("Salary (₹)", salaryField));

        formPanel.add(basicGrid);
        formPanel.add(Box.createVerticalStrut(20));

        // ---- Section: Contact & Role ----
        formPanel.add(sectionLabel("Contact & Role Details"));
        formPanel.add(Box.createVerticalStrut(10));

        JPanel detailGrid = new JPanel(new GridLayout(2, 2, 15, 12));
        detailGrid.setOpaque(false);
        detailGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        emailField = createField();
        phoneField = createField();
        roleField  = createField();
        statusBox  = new JComboBox<>(new String[]{"Active", "Inactive"});
        styleCombo(statusBox);

        detailGrid.add(labeledField("Email", emailField));
        detailGrid.add(labeledField("Phone", phoneField));
        detailGrid.add(labeledField("Role / Position", roleField));
        detailGrid.add(labeledCombo("Status", statusBox));

        formPanel.add(detailGrid);

        JScrollPane scrollForm = new JScrollPane(formPanel);
        scrollForm.setBorder(null);
        scrollForm.getViewport().setBackground(BG_DARK);
        frame.add(scrollForm, BorderLayout.CENTER);

        // ---- Button Panel ----
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        btnPanel.setBackground(new Color(20, 30, 55));

        JButton saveBtn = styledButton(isAddMode ? "Add Employee" : "Save Changes", ACCENT_GREEN);
        JButton cancelBtn = styledButton("Cancel", ACCENT_RED);

        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);
        frame.add(btnPanel, BorderLayout.SOUTH);

        // Pre-fill if edit mode
        if (!isAddMode && empId != -1) {
            prefillData(empId);
        }

        // Actions
        saveBtn.addActionListener(e -> saveEmployee());
        cancelBtn.addActionListener(e -> frame.dispose());

        frame.setVisible(true);
    }

    private void prefillData(int empId) {
        Employee emp = dao.getEmployeeById(empId);
        EmployeeDetails det = dao.getEmployeeDetails(empId);
        if (emp != null) {
            nameField.setText(emp.getName());
            ageField.setText(String.valueOf(emp.getAge()));
            deptField.setText(emp.getDepartment());
            salaryField.setText(String.valueOf(emp.getSalary()));
        }
        if (det != null) {
            emailField.setText(det.getEmail());
            phoneField.setText(det.getPhone());
            roleField.setText(det.getRole());
            statusBox.setSelectedItem(det.getStatus());
        }
    }

    private void saveEmployee() {
        try {
            String name   = nameField.getText().trim();
            String ageStr = ageField.getText().trim();
            String dept   = deptField.getText().trim();
            String salStr = salaryField.getText().trim();
            String email  = emailField.getText().trim();
            String phone  = phoneField.getText().trim();
            String role   = roleField.getText().trim();
            String status = (String) statusBox.getSelectedItem();

            if (name.isEmpty() || ageStr.isEmpty() || dept.isEmpty() || salStr.isEmpty()) {
                showError("Please fill in all basic information fields.");
                return;
            }

            int age       = Integer.parseInt(ageStr);
            double salary = Double.parseDouble(salStr);

            if (age <= 0 || age > 100) { showError("Please enter a valid age."); return; }
            if (salary < 0)            { showError("Salary cannot be negative."); return; }

            Employee emp       = new Employee(isAddMode ? 0 : editEmpId, name, age, dept, salary);
            EmployeeDetails det = new EmployeeDetails(isAddMode ? 0 : editEmpId, email, phone, role, status);

            boolean success;
            if (isAddMode) {
                success = dao.addEmployee(emp, det);
            } else {
                success = dao.updateEmployee(emp, det);
            }

            if (success) {
                JOptionPane.showMessageDialog(frame,
                    isAddMode ? "Employee added successfully!" : "Employee updated successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                if (parentGUI != null) parentGUI.loadTable();
                frame.dispose();
            } else {
                showError("Database error. Please try again.");
            }

        } catch (NumberFormatException ex) {
            showError("Age and Salary must be valid numbers.");
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(frame, msg, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    // ---- UI Helpers ----

    private JLabel sectionLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        lbl.setForeground(ACCENT_BLUE);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(59, 130, 246, 80)));
        return lbl;
    }

    private JTextField createField() {
        JTextField field = new JTextField();
        field.setBackground(INPUT_BG);
        field.setForeground(TEXT_WHITE);
        field.setCaretColor(TEXT_WHITE);
        field.setFont(new Font("Arial", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return field;
    }

    private void styleCombo(JComboBox<String> box) {
        box.setBackground(INPUT_BG);
        box.setForeground(TEXT_WHITE);
        box.setFont(new Font("Arial", Font.PLAIN, 13));
        box.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
    }

    private JPanel labeledField(String label, JTextField field) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 11));
        lbl.setForeground(TEXT_MUTED);
        p.add(lbl, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    private JPanel labeledCombo(String label, JComboBox<String> box) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 11));
        lbl.setForeground(TEXT_MUTED);
        p.add(lbl, BorderLayout.NORTH);
        p.add(box, BorderLayout.CENTER);
        return p;
    }

    private JButton styledButton(String text, Color color) {
        JButton btn = new JButton(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = getModel().isPressed() ? color.darker() :
                          getModel().isRollover() ? color.brighter() : color;
                g2d.setColor(c);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        btn.setPreferredSize(new Dimension(160, 40));
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}