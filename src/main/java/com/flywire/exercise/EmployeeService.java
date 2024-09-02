package com.flywire.exercise;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private List<Employee> employees;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @PostConstruct
    public void init() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonContent = new String(Files.readAllBytes(Paths.get("src/main/resources/json/data.json")));
        employees = mapper.readValue(jsonContent, new TypeReference<List<Employee>>() {});
    }

    public List<Employee> getActiveEmployeesSortedByLastName() {
        return employees.stream()
                .filter(Employee::isActive)
                .sorted((e1, e2) -> extractLastName(e1).compareToIgnoreCase(extractLastName(e2)))
                .collect(Collectors.toList());
    }

    public EmployeeService.EmployeeWithDirectReports getEmployeeWithDirectReportsById(int id) {
        Employee employee = employees.stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElse(null);

        if (employee == null) {
            return null;
        }

        List<String> directHireNames = employee.getDirectReports().stream()
                .map(this::getEmployeeById)
                .filter(Objects::nonNull)
                .map(Employee::getName)
                .collect(Collectors.toList());

        return new EmployeeWithDirectReports(employee, directHireNames);
    }

    public Employee getEmployeeById(int id) {
        return employees.stream()
                .filter(employee -> employee.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void addEmployee(Employee newEmployee, Optional<Integer> managerId, List<Integer> directReportIds) throws IOException {
        // Set today's date as hireDate in string format
        newEmployee.setHireDate(dateFormat.format(new Date()));
        newEmployee.setDirectReports(directReportIds);

        // Add the new employee to the list
        employees.add(newEmployee);

        // If a manager is specified, update the manager's direct reports
        managerId.ifPresent(id -> {
            Employee manager = getEmployeeById(id);
            if (manager != null) {
                manager.getDirectReports().add(newEmployee.getId());
            }
        });

        // Save the updated employee list back to the JSON file
        writeToJson();
    }

    public void deactivateEmployee(int id) throws IOException {
        Employee employee = getEmployeeById(id);
        if (employee != null) {
            employee.setActive(false);
            writeToJson();
        }
    }

    private void writeToJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(Paths.get("src/main/resources/json/data.json").toFile(), employees);
    }

    // Convert hireDate from String to Date for use in comparisons or sorting
    private Date parseHireDate(String hireDate) {
        try {
            return dateFormat.parse(hireDate);
        } catch (ParseException e) {
            throw new RuntimeException("Failed to parse hireDate: " + hireDate, e);
        }
    }

    // Extract the last name from the full name for sorting purposes
    private String extractLastName(Employee employee) {
        String name = employee.getName();
        if (name != null && !name.isEmpty()) {
            String[] nameParts = name.split(" ");
            return nameParts[nameParts.length - 1];
        }
        return "";
    }

    // Custom DTO to encapsulate Employee and direct hires' names
    public static class EmployeeWithDirectReports {
        private Employee employee;
        private List<String> directHireNames;

        public EmployeeWithDirectReports(Employee employee, List<String> directHireNames) {
            this.employee = employee;
            this.directHireNames = directHireNames;
        }

        public Employee getEmployee() {
            return employee;
        }

        public List<String> getDirectHireNames() {
            return directHireNames;
        }
    }
}
