import java.sql.*;
import java.util.ArrayList;

public class EmployeeDAO {

    // =====================
    // ADD EMPLOYEE
    // =====================
    public boolean addEmployee(Employee emp, EmployeeDetails details) {
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            // Insert into employees
            String q1 = "INSERT INTO employees(name, age, department, salary) VALUES (?, ?, ?, ?)";
            PreparedStatement ps1 = con.prepareStatement(q1, Statement.RETURN_GENERATED_KEYS);
            ps1.setString(1, emp.getName());
            ps1.setInt(2, emp.getAge());
            ps1.setString(3, emp.getDepartment());
            ps1.setDouble(4, emp.getSalary());
            ps1.executeUpdate();

            // Get generated ID
            ResultSet keys = ps1.getGeneratedKeys();
            int generatedId = -1;
            if (keys.next()) generatedId = keys.getInt(1);

            // Insert into employee_details
            if (generatedId != -1 && details != null) {
                String q2 = "INSERT INTO employee_details(emp_id, email, phone, role, status) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement ps2 = con.prepareStatement(q2);
                ps2.setInt(1, generatedId);
                ps2.setString(2, details.getEmail());
                ps2.setString(3, details.getPhone());
                ps2.setString(4, details.getRole());
                ps2.setString(5, details.getStatus());
                ps2.executeUpdate();
                ps2.close();
            }

            con.commit();
            ps1.close();
            return true;

        } catch (Exception e) {
            try { if (con != null) con.rollback(); } catch (Exception ex) {}
            e.printStackTrace();
            return false;
        } finally {
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }

    // =====================
    // GET ALL EMPLOYEES
    // =====================
    public ArrayList<Employee> getAllEmployees() {
        ArrayList<Employee> list = new ArrayList<>();
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM employees ORDER BY id");
            while (rs.next()) {
                list.add(new Employee(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("department"),
                        rs.getDouble("salary")
                ));
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
        return list;
    }

    // =====================
    // DELETE EMPLOYEE
    // =====================
    public boolean deleteEmployee(int id) {
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            // Delete details first (foreign key)
            String q1 = "DELETE FROM employee_details WHERE emp_id=?";
            PreparedStatement ps1 = con.prepareStatement(q1);
            ps1.setInt(1, id);
            ps1.executeUpdate();
            ps1.close();

            // Delete employee
            String q2 = "DELETE FROM employees WHERE id=?";
            PreparedStatement ps2 = con.prepareStatement(q2);
            ps2.setInt(1, id);
            ps2.executeUpdate();
            ps2.close();

            con.commit();
            return true;

        } catch (Exception e) {
            try { if (con != null) con.rollback(); } catch (Exception ex) {}
            e.printStackTrace();
            return false;
        } finally {
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }

    // =====================
    // UPDATE EMPLOYEE
    // =====================
    public boolean updateEmployee(Employee emp, EmployeeDetails details) {
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            String q1 = "UPDATE employees SET name=?, age=?, department=?, salary=? WHERE id=?";
            PreparedStatement ps1 = con.prepareStatement(q1);
            ps1.setString(1, emp.getName());
            ps1.setInt(2, emp.getAge());
            ps1.setString(3, emp.getDepartment());
            ps1.setDouble(4, emp.getSalary());
            ps1.setInt(5, emp.getId());
            ps1.executeUpdate();
            ps1.close();

            if (details != null) {
                String q2 = "UPDATE employee_details SET email=?, phone=?, role=?, status=? WHERE emp_id=?";
                PreparedStatement ps2 = con.prepareStatement(q2);
                ps2.setString(1, details.getEmail());
                ps2.setString(2, details.getPhone());
                ps2.setString(3, details.getRole());
                ps2.setString(4, details.getStatus());
                ps2.setInt(5, emp.getId());
                ps2.executeUpdate();
                ps2.close();
            }

            con.commit();
            return true;

        } catch (Exception e) {
            try { if (con != null) con.rollback(); } catch (Exception ex) {}
            e.printStackTrace();
            return false;
        } finally {
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
    }

    // =====================
    // GET EMPLOYEE DETAILS
    // =====================
    public EmployeeDetails getEmployeeDetails(int empId) {
        EmployeeDetails d = null;
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            String query = "SELECT * FROM employee_details WHERE emp_id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, empId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                d = new EmployeeDetails(
                        rs.getInt("emp_id"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("role"),
                        rs.getString("status")
                );
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
        return d;
    }

    // =====================
    // GET EMPLOYEE BY ID
    // =====================
    public Employee getEmployeeById(int id) {
        Employee emp = null;
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            String query = "SELECT * FROM employees WHERE id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                emp = new Employee(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("department"),
                        rs.getDouble("salary")
                );
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
        return emp;
    }
}