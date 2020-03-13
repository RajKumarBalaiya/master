/**
 * 
 */
package com.springboot.serverless.app.employee.manager;

import java.util.List;

import com.springboot.serverless.app.Model.EmployeeModel;

/**
 * @author Raj
 *
 */

public interface IEmployeeManger {

	public List<EmployeeModel> getAllEmployee();

	public EmployeeModel getEmployeeById(String employeeId);

	public void addEmployee(EmployeeModel employeeModel);

	public EmployeeModel updateEmployee(EmployeeModel employeeModel);

	public void deleteEmployeeById(String employeeId);
}
