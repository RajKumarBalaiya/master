/**
 * 
 */
package com.springboot.serverless.app.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.springboot.serverless.app.Model.EmployeeModel;
import com.springboot.serverless.app.employee.manager.IEmployeeManger;

/**
 * @author Raj
 *
 */
@Service
public class EmployeeRepository implements IEmployeeManger {

	List<EmployeeModel> employeeList = new ArrayList<EmployeeModel>();

	@Override
	public List<EmployeeModel> getAllEmployee() {
		return employeeList;
	}

	@Override
	public EmployeeModel getEmployeeById(String employeeId) {
		for (EmployeeModel employeeModel : employeeList) {
			if (employeeModel.getEmployeeId().equalsIgnoreCase(employeeId)) {
				return employeeModel;
			}
		}
		return null;
	}

	@Override
	public void addEmployee(EmployeeModel employeeModel) {
		employeeList.add(employeeModel);
	}

	@Override
	public EmployeeModel updateEmployee(EmployeeModel employee) {
		for (EmployeeModel employeeModel : employeeList) {
			if (employeeModel.getEmployeeId().equalsIgnoreCase(employee.getEmployeeId())) {
				employeeModel.setEmployeeId(employee.getEmployeeId());
				employeeModel.setEmployeeName(employee.getEmployeeName());
				employeeModel.setEmployeeRole(employee.getEmployeeRole());
				employeeModel.setEmployeeSalary(employee.getEmployeeSalary());
				return employeeModel;
			}
		}
		return null;
	}

	@Override
	public void deleteEmployeeById(String employeeId) {
		for (EmployeeModel employeeModel : employeeList) {
			if (employeeModel.getEmployeeId().equalsIgnoreCase(employeeId)) {
				employeeList.remove(employeeModel);
				break;
			}
		}
	}

}
