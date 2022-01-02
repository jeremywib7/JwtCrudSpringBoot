package com.j23.server.services;

import com.j23.server.exception.UserNotFoundException;
import com.j23.server.models.Employee;
import com.j23.server.repos.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EmployeeService {
    private final EmployeeRepo employeeRepo;

    @Autowired
    public EmployeeService(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    public Employee addEmployee(Employee employee) {
        employee.setEmployeeCode(String.valueOf(UUID.randomUUID()));
        return employeeRepo.save(employee);
    }

    public List<Employee> findAllEmployee() {
        return employeeRepo.findAll();
    }

    public Employee updateEmployee(Employee employee) {
        return employeeRepo.save(employee);
    }

    public Employee findEmployeeById(Long id) {
        return employeeRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Employee by id " + id + " was not found"));
    }

    public void deleteEmployee(Long id) {
        employeeRepo.deleteById(id);
    }
}
