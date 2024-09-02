package com.flywire.exercise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/active")
    public List<Employee> getAllActiveEmployees() {
        return employeeService.getActiveEmployeesSortedByLastName();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeService.EmployeeWithDirectReports> getEmployeeById(@PathVariable int id) {
        EmployeeService.EmployeeWithDirectReports employeeWithDirectReports = employeeService.getEmployeeWithDirectReportsById(id);
        if (employeeWithDirectReports == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(employeeWithDirectReports);
    }

    @PostMapping
    public ResponseEntity<String> createEmployee(
            @RequestBody Employee newEmployee,
            @RequestParam Optional<Integer> managerId,
            @RequestParam List<Integer> directReportIds) {
        try {
            employeeService.addEmployee(newEmployee, managerId, directReportIds);
            return ResponseEntity.ok("Employee added successfully");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error adding employee");
        }
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<String> deactivateEmployee(@PathVariable int id) {
        try {
            employeeService.deactivateEmployee(id);
            return ResponseEntity.ok("Employee deactivated successfully");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error deactivating employee");
        }
    }
}
