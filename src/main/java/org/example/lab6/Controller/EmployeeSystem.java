package org.example.lab6.Controller;

import jakarta.validation.Valid;
import org.example.lab6.Api.ApiResponse;
import org.example.lab6.Modell.Employee;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("api/v1/employee")
public class EmployeeSystem {

    ArrayList<Employee> employees = new ArrayList<>();


    @GetMapping("/get")
    public ArrayList<Employee> getAllEmployees(){
        return employees;
    }


    @PostMapping("/add")
    public ResponseEntity<?> addNewEmployee(@RequestBody @Valid Employee employee , Errors errors){
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        employees.add(employee);
        return ResponseEntity.status(200).body(new ApiResponse("Added new employee successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable String id , @RequestBody @Valid Employee employee , Errors errors){
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
            }
        boolean found = false;
            for (int i = 0; i < employees.size() ; i++) {
                if (employees.get(i).getId().equalsIgnoreCase(id)) {
                    employees.set(i, employee);
                    found = true;
                    break;
                }
                }if (!found){
                    return ResponseEntity.status(400).body("invalid id");
                }
        return ResponseEntity.status(200).body(new ApiResponse("updated employee successfully"));
    }

    @DeleteMapping("/delete/{index}")
    public ResponseEntity<?> deleteEmployee(@PathVariable int index){
        if (index < 0|| index >= employees.size()){
            return ResponseEntity.status(400).body(new ApiResponse("invalid index"));
        }
            employees.remove(index);
        return ResponseEntity.status(200).body(new ApiResponse("deleted employee successfully"));
    }

    @GetMapping("/searchByPosition/{position}")
    public ResponseEntity<?> searchByPosition(@PathVariable String position) {
        ArrayList<Employee> searchByPosition = new ArrayList<>();
        for (Employee e : employees) {
            if (position.equalsIgnoreCase(e.getPosition())) {
                searchByPosition.add(e);
            }
        }
        if (searchByPosition.isEmpty()){
            return ResponseEntity.status(404).body(new ApiResponse("no employee found with this position"));

        }
        return ResponseEntity.status(200).body(searchByPosition);
    }

    @GetMapping("/getEmployeeByRange/{min}/{max}")
    public ResponseEntity<?> getEmployeeByRangeAge(@PathVariable int min , @PathVariable int max){
        ArrayList<Employee> getEmployeeByAge = new ArrayList<>();
        for (Employee e : employees) {
            if (e.getAge() >= min && e.getAge() <= max) {
                getEmployeeByAge.add(e);
            }
        }
        if (min < 25) {
            return ResponseEntity.status(400).body(new ApiResponse("age employee should be 25 more than "));
        }
        if (getEmployeeByAge.isEmpty()) {
            return ResponseEntity.status(400).body(new ApiResponse("i can't find eny employee between " + min + " to " + max));
        }
        return ResponseEntity.status(200).body(getEmployeeByAge);
    }

    @GetMapping("/annualLeave/{id}")
    public ResponseEntity<?> applyForAnnualLeave(@PathVariable String id){
        for (Employee e : employees){
            if (e.getId().equalsIgnoreCase(id)){
                if (e.getAnnualLeave() < 1){
                    return ResponseEntity.status(400).body(new ApiResponse("Sorry! your annual leave "+ e.getAnnualLeave()));
                } else {
                    e.setAnnualLeave(e.getAnnualLeave() - 1);
                    e.setOnLeave(true);
                    return ResponseEntity.status(200).body(new ApiResponse("Enjoy"));
                }
            }
        }
               return ResponseEntity.status(400).body(new ApiResponse("There is no employee with this id"));
    }

    @GetMapping("/EmployeeWithNoAnnual")
    public ResponseEntity<?> employeeWithNoAnnualLeave(){
        ArrayList<Employee> employeesWithNoAnnualLeave = new ArrayList<>();
        for (Employee e : employees){
            if (e.getAnnualLeave() < 1){
                employeesWithNoAnnualLeave.add(e);
            }
        }
        if (employeesWithNoAnnualLeave.isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("all employee has an annual leave"));
        }
        return ResponseEntity.status(200).body(employeesWithNoAnnualLeave);
    }

    @GetMapping("/promoteEmployee/{iDSupervisor}/{iDCoordinator}")
    public ResponseEntity<?> promoteEmployee(@PathVariable String iDSupervisor , @PathVariable String iDCoordinator){
        Employee coordinator = null;
        Employee supervisor = null;
        for (Employee e : employees) {
            if (e.getId().equals(iDSupervisor) && e.getPosition().equalsIgnoreCase("supervisor")) {
                supervisor = e;
            }
                if (e.getId().equalsIgnoreCase(iDCoordinator) && e.getPosition().equalsIgnoreCase("coordinator")) {
                    coordinator = e;
                }
        }
        if (supervisor == null){
            return ResponseEntity.status(404).body(new ApiResponse("the employee id : " + iDSupervisor + " not found"));
        }
        if (coordinator == null){
            return ResponseEntity.status(404).body(new ApiResponse("the employee id : " + iDCoordinator + " not found"));
        }
        if (coordinator.isOnLeave()){
            return ResponseEntity.status(400).body(new ApiResponse("the employee on Leave"));
        }
        if (coordinator.getAge() < 30){
            return ResponseEntity.status(400).body(new ApiResponse("the employee is under 30 years old"));
        }

        coordinator.setPosition("supervisor");
        return ResponseEntity.status(200).body(new ApiResponse(coordinator.getName() + " was promoted to supervisor"));
    }

}
