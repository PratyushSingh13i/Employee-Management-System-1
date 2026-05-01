public class EmployeeDetails {
    private int empId;
    private String email;
    private String phone;
    private String role;
    private String status;

    public EmployeeDetails(int empId, String email, String phone, String role, String status) {
        this.empId = empId;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.status = status;
    }

    public int getEmpId() { return empId; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getRole() { return role; }
    public String getStatus() { return status; }
}