package com.springboot.serverless.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.springboot.serverless.app.Model.EmployeeModel;
import com.springboot.serverless.app.employee.manager.IEmployeeManger;

/**
 * @author Raj
 *
 */
@EnableWebMvc
@RestController
@CrossOrigin("*")
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private IEmployeeManger iEmployeeManger;

	@GetMapping("getAll")
	public ResponseEntity<List<EmployeeModel>> getAllEmployee() {
		List<EmployeeModel> employeeModel = null;
		try {
			employeeModel = iEmployeeManger.getAllEmployee();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.OK).body(employeeModel);
		}
		return ResponseEntity.status(HttpStatus.OK).body(employeeModel);
	}

	@GetMapping("getById")
	public ResponseEntity<EmployeeModel> getEmployeeById(@RequestParam String employeeId) {
		EmployeeModel employeeModel = null;
		try {
			employeeModel = iEmployeeManger.getEmployeeById(employeeId);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(employeeModel);
		}
		return ResponseEntity.status(HttpStatus.OK).body(employeeModel);
	}

	@PostMapping("add")
	public ResponseEntity<String> addEmployee(@RequestBody EmployeeModel employeeModel) {
		try {
			iEmployeeManger.addEmployee(employeeModel);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
		}
		return ResponseEntity.status(HttpStatus.ACCEPTED).body("");
	}

	@PutMapping("update")
	public ResponseEntity<EmployeeModel> updateEmployee(@RequestBody EmployeeModel employee) {
		EmployeeModel employeeModel = null;
		try {
			employeeModel = iEmployeeManger.updateEmployee(employee);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(employeeModel);
		}
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(employeeModel);
	}

	@DeleteMapping("delete")
	public ResponseEntity<String> deleteEmployeeById(@RequestParam String employeeId) {
		try {
			iEmployeeManger.deleteEmployeeById(employeeId);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
		}
		return ResponseEntity.status(HttpStatus.OK).body("");
	}
}
