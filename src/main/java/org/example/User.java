package org.example;

import java.sql.Date;

public class User {
    private String NIC;
    private String name;
    private String password;
    private String email;
    private Date DOB;
    private String departmentId;
    private String department;
    private Role role;

    public User(String NIC, Role role) {
        this.NIC = NIC;
        this.role = role;
    }

    public User(String NIC, String name, String password, String email, Date DOB, String departmentId, String department, Role role) {
        this.NIC = NIC;
        this.name = name;
        this.password = password;
        this.email = email;
        this.DOB = DOB;
        this.departmentId = departmentId;
        this.department = department;
        this.role = role;
    }

    public String getNIC() { return NIC; }
    public void setNIC(String NIC) { this.NIC = NIC; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Date getDOB() { return DOB; }
    public void setDOB(Date DOB) { this.DOB = DOB; }
    public String getDepartmentId() { return departmentId; }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getUsername() { return NIC; }
}