import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class EmployeeGUI {

    // ---- Color Palette ----
    private static final Color BG_DARK      = new Color(15, 23, 42);
    private static final Color SIDEBAR_BG   = new Color(20, 30, 55);
    private static final Color ACCENT_BLUE  = new Color(59, 130, 246);
    private static final Color ACCENT_GREEN = new Color(34, 197, 94);
    private static final Color ACCENT_RED   = new Color(239, 68, 68);
    private static final Color ACCENT_AMBER = new Color(251, 191, 36);
    private static final Color TEXT_WHITE   = new Color(241, 245, 249);
    private static final Color TEXT_MUTED   = new Color(148, 163, 184);
    private static final Color TABLE_ROW1   = new Color(30, 41, 59);
    private static final Color TABLE_ROW2   = new Color(24, 34, 51);
    private static final Color TABLE_SEL    = new Color(59, 130, 246, 120);
    private static final Color BORDER_COLOR = new Color(51, 65, 85);

    JFrame frame;
    JTable table;
    DefaultTableModel model;
    JTextField searchField;

    JComboBox<String> deptFilter;
    JComboBox<String> statusFilter;
    JTextField minSalaryField, maxSalaryField;

    EmployeeDAO dao = new EmployeeDAO();

    public EmployeeGUI() {
        frame = new JFrame("Employee Management System");
        frame.setSize(1100, 660);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(BG_DARK);
        frame.setLayout(new BorderLayout());

        // ===== HEADER =====
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(SIDEBAR_BG);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
            BorderFactory.createEmptyBorder(14, 25, 14, 25)
        ));

        JLabel titleLabel = new JLabel("Employee Management System");
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_WHITE);

        JLabel userBadge = new JLabel("● Admin");
        userBadge.setFont(new Font("Arial", Font.BOLD, 12));
        userBadge.setForeground(ACCENT_GREEN);

        header.add(titleLabel, BorderLayout.WEST);
        header.add(userBadge, BorderLayout.EAST);
        frame.add(header, BorderLayout.NORTH);

        // ===== LEFT SIDEBAR (Filters) =====
        JPanel sidebar = new JPanel();
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER_COLOR),
            BorderFactory.createEmptyBorder(20, 15, 20, 15)
        ));

        sidebar.add(sidebarLabel("🔍  Search"));
        sidebar.add(Box.createVerticalStrut(6));
        searchField = new JTextField();
        styleTextField(searchField);
        searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        sidebar.add(searchField);
        sidebar.add(Box.createVerticalStrut(20));

        sidebar.add(sidebarLabel("🏢  Department"));
        sidebar.add(Box.createVerticalStrut(6));
        deptFilter = new JComboBox<>(new String[]{"All", "IT", "HR", "Finance", "Marketing", "Admin"});
        styleCombo(deptFilter);
        deptFilter.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        sidebar.add(deptFilter);
        sidebar.add(Box.createVerticalStrut(16));

        sidebar.add(sidebarLabel("📋  Status"));
        sidebar.add(Box.createVerticalStrut(6));
        statusFilter = new JComboBox<>(new String[]{"All", "Active", "Inactive"});
        styleCombo(statusFilter);
        statusFilter.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        sidebar.add(statusFilter);
        sidebar.add(Box.createVerticalStrut(16));

        sidebar.add(sidebarLabel("💰  Salary Range"));
        sidebar.add(Box.createVerticalStrut(6));
        minSalaryField = new JTextField();
        maxSalaryField = new JTextField();
        styleTextField(minSalaryField);
        styleTextField(maxSalaryField);
        minSalaryField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        maxSalaryField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        sidebar.add(minSalaryField);
        sidebar.add(Box.createVerticalStrut(6));
        sidebar.add(maxSalaryField);
        sidebar.add(Box.createVerticalStrut(16));

        JButton applyFilter = sidebarButton("Apply Filters", ACCENT_BLUE);
        JButton resetFilter = sidebarButton("Reset Filters", new Color(71, 85, 105));
        applyFilter.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        resetFilter.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        sidebar.add(applyFilter);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(resetFilter);
        sidebar.add(Box.createVerticalGlue());

        JLabel totalLabel = new JLabel("Total: 0 employees");
        totalLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        totalLabel.setForeground(TEXT_MUTED);
        totalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(totalLabel);

        frame.add(sidebar, BorderLayout.WEST);

        // ===== TABLE =====
        model = new DefaultTableModel() {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Age");
        model.addColumn("Department");
        model.addColumn("Salary (₹)");

        table = new JTable(model) {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
                Component c = super.prepareRenderer(renderer, row, col);
                if (isRowSelected(row)) {
                    c.setBackground(TABLE_SEL);
                } else {
                    c.setBackground(row % 2 == 0 ? TABLE_ROW1 : TABLE_ROW2);
                }
                c.setForeground(TEXT_WHITE);
                return c;
            }
        };

        table.setBackground(TABLE_ROW1);
        table.setForeground(TEXT_WHITE);
        table.setRowHeight(36);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setSelectionBackground(TABLE_SEL);
        table.setSelectionForeground(TEXT_WHITE);
        table.setFillsViewportHeight(true);

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setBackground(new Color(15, 25, 50));
        tableHeader.setForeground(ACCENT_BLUE);
        tableHeader.setFont(new Font("Arial", Font.BOLD, 13));
        tableHeader.setPreferredSize(new Dimension(0, 42));
        tableHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT_BLUE));

        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(60);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setPreferredWidth(110);

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(BG_DARK);
        scroll.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));

        JLabel hint = new JLabel("  💡 Double-click any row to view employee details");
        hint.setFont(new Font("Arial", Font.ITALIC, 11));
        hint.setForeground(TEXT_MUTED);
        hint.setBackground(BG_DARK);
        hint.setOpaque(true);
        hint.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(BG_DARK);
        centerPanel.add(scroll, BorderLayout.CENTER);
        centerPanel.add(hint, BorderLayout.SOUTH);
        frame.add(centerPanel, BorderLayout.CENTER);

        // ===== BOTTOM BUTTONS =====
        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 12));
        btnBar.setBackground(SIDEBAR_BG);
        btnBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));

        JButton addBtn    = actionButton("➕  Add Employee",    ACCENT_GREEN);
        JButton editBtn   = actionButton("✏️  Edit Employee",   ACCENT_AMBER);
        JButton deleteBtn = actionButton("🗑  Delete Employee", ACCENT_RED);
        JButton refreshBtn= actionButton("🔄  Refresh",         ACCENT_BLUE);

        btnBar.add(addBtn);
        btnBar.add(editBtn);
        btnBar.add(deleteBtn);
        btnBar.add(refreshBtn);
        frame.add(btnBar, BorderLayout.SOUTH);

        loadTable();
        updateTotalLabel(totalLabel);

        // ===== SEARCH =====
        final TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { applySearch(sorter); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { applySearch(sorter); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { applySearch(sorter); }
        });

        applyFilter.addActionListener(e -> { applyFilters(sorter); updateTotalLabel(totalLabel); });

        resetFilter.addActionListener(e -> {
            searchField.setText("");
            deptFilter.setSelectedIndex(0);
            statusFilter.setSelectedIndex(0);
            minSalaryField.setText("");
            maxSalaryField.setText("");
            sorter.setRowFilter(null);
            loadTable();
            updateTotalLabel(totalLabel);
        });

        // ===== BUTTON ACTIONS =====
        addBtn.addActionListener(e -> new AddEditEmployeePage(this, true, -1));

        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(frame, "Please select an employee to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = (int) model.getValueAt(table.convertRowIndexToModel(row), 0);
            new AddEditEmployeePage(this, false, id);
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(frame, "Please select an employee to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id   = (int)    model.getValueAt(table.convertRowIndexToModel(row), 0);
            String name = (String) model.getValueAt(table.convertRowIndexToModel(row), 1);
            int confirm = JOptionPane.showConfirmDialog(frame,
                "Are you sure you want to delete \"" + name + "\"?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean ok = dao.deleteEmployee(id);
                if (ok) { loadTable(); updateTotalLabel(totalLabel); }
                else JOptionPane.showMessageDialog(frame, "Error deleting employee.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        refreshBtn.addActionListener(e -> {
            loadTable();
            updateTotalLabel(totalLabel);
            sorter.setRowFilter(null);
            searchField.setText("");
        });

        // ===== DOUBLE CLICK → DETAILS =====
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        int id = (int) model.getValueAt(table.convertRowIndexToModel(row), 0);
                        showDetails(id);
                    }
                }
            }
        });

        frame.setVisible(true);
    }

    // ===== LOAD TABLE =====
    public void loadTable() {
        model.setRowCount(0);
        ArrayList<Employee> list = dao.getAllEmployees();
        for (Employee e : list) {
            model.addRow(new Object[]{
                e.getId(), e.getName(), e.getAge(),
                e.getDepartment(), String.format("%.2f", e.getSalary())
            });
        }
    }

    private void updateTotalLabel(JLabel lbl) {
        lbl.setText("Total: " + model.getRowCount() + " employees");
    }

    private void applySearch(TableRowSorter<DefaultTableModel> sorter) {
        String text = searchField.getText().trim();
        sorter.setRowFilter(text.isEmpty() ? null : RowFilter.regexFilter("(?i)" + text));
    }

    private void applyFilters(TableRowSorter<DefaultTableModel> sorter) {
        String dept   = (String) deptFilter.getSelectedItem();
        String minStr = minSalaryField.getText().trim();
        String maxStr = maxSalaryField.getText().trim();
        loadTable();
        sorter.setRowFilter(new RowFilter<>() {
            public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                String rowDept   = entry.getStringValue(3);
                String rowSalStr = entry.getStringValue(4).replace(",", "");
                if (!dept.equals("All") && !rowDept.equalsIgnoreCase(dept)) return false;
                try {
                    double salary = Double.parseDouble(rowSalStr);
                    if (!minStr.isEmpty() && salary < Double.parseDouble(minStr)) return false;
                    if (!maxStr.isEmpty() && salary > Double.parseDouble(maxStr)) return false;
                } catch (NumberFormatException ignored) {}
                return true;
            }
        });
    }

    // ===== DETAILS POPUP =====
    public void showDetails(int id) {
        EmployeeDetails d = dao.getEmployeeDetails(id);
        Employee emp      = dao.getEmployeeById(id);

        JFrame f = new JFrame("Employee Details");
        f.setSize(450, 380);
        f.setLocationRelativeTo(frame);

        // Load details background image using ImageLoader
        Image bgImg = ImageLoader.load("details.jpg", 450, 380);
        JLabel bg;
        if (bgImg != null) {
            bg = new JLabel(new ImageIcon(bgImg));
        } else {
            bg = new JLabel();
            bg.setBackground(new Color(20, 30, 55));
            bg.setOpaque(true);
        }
        bg.setLayout(null);

        // Dark overlay
        JPanel overlay = new JPanel() {
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(0, 0, 0, 150));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        overlay.setOpaque(false);
        overlay.setLayout(null);
        overlay.setBounds(0, 0, 450, 380);

        String empName = emp != null ? emp.getName() : "Employee #" + id;
        JLabel nameLabel = new JLabel(empName, JLabel.CENTER);
        nameLabel.setBounds(50, 25, 350, 40);
        nameLabel.setFont(new Font("Georgia", Font.BOLD, 22));
        nameLabel.setForeground(Color.WHITE);

        JSeparator sep = new JSeparator();
        sep.setBounds(100, 70, 250, 2);
        sep.setForeground(new Color(100, 160, 255));

        if (d != null) {
            String[] texts = {
                "📧  Email:    " + d.getEmail(),
                "📞  Phone:   " + d.getPhone(),
                "💼  Role:      " + d.getRole(),
                "📋  Status:   " + d.getStatus()
            };
            Color statusColor = d.getStatus().equalsIgnoreCase("Active") ?
                new Color(100, 220, 120) : new Color(255, 120, 100);

            int y = 90;
            for (int i = 0; i < texts.length; i++) {
                JLabel lbl = new JLabel(texts[i]);
                lbl.setBounds(60, y, 330, 38);
                lbl.setFont(new Font("Arial", Font.PLAIN, 14));
                lbl.setForeground(i == 3 ? statusColor : new Color(220, 230, 255));
                lbl.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(255, 255, 255, 30)));
                overlay.add(lbl);
                y += 50;
            }
        } else {
            JLabel noData = new JLabel("No details found for this employee.", JLabel.CENTER);
            noData.setBounds(50, 150, 350, 30);
            noData.setFont(new Font("Arial", Font.ITALIC, 13));
            noData.setForeground(new Color(200, 200, 200));
            overlay.add(noData);
        }

        JButton closeBtn = new JButton("Close") {
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getModel().isRollover() ? new Color(80, 150, 255) : new Color(59, 130, 246));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        closeBtn.setBounds(170, 315, 110, 36);
        closeBtn.setFont(new Font("Arial", Font.BOLD, 13));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setFocusPainted(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> f.dispose());

        overlay.add(nameLabel);
        overlay.add(sep);
        overlay.add(closeBtn);
        bg.add(overlay);

        f.setContentPane(bg);
        f.setVisible(true);
    }

    // ===== UI HELPERS =====
    private JLabel sidebarLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.BOLD, 12));
        lbl.setForeground(TEXT_MUTED);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private void styleTextField(JTextField field) {
        field.setBackground(new Color(51, 65, 85));
        field.setForeground(TEXT_WHITE);
        field.setCaretColor(TEXT_WHITE);
        field.setFont(new Font("Arial", Font.PLAIN, 12));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void styleCombo(JComboBox<String> box) {
        box.setBackground(new Color(51, 65, 85));
        box.setForeground(TEXT_WHITE);
        box.setFont(new Font("Arial", Font.PLAIN, 12));
        box.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        box.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private JButton sidebarButton(String text, Color color) {
        JButton btn = new JButton(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getModel().isRollover() ? color.brighter() : color);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        return btn;
    }

    private JButton actionButton(String text, Color color) {
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
        btn.setPreferredSize(new Dimension(170, 38));
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}