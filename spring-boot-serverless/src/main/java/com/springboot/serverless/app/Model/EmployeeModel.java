/**
 * 
 */
package com.springboot.serverless.app.Model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author Raj
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String employeeId;
	private String employeeName;
	private String employeeRole;
	private double employeeSalary;

	public EmployeeModel() {
	}

	public EmployeeModel(String employeeId, String employeeName, String employeeRole, double employeeSalary) {
		this.employeeId = employeeId;
		this.employeeName = employeeName;
		this.employeeRole = employeeRole;
		this.employeeSalary = employeeSalary;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeRole() {
		return employeeRole;
	}

	public void setEmployeeRole(String employeeRole) {
		this.employeeRole = employeeRole;
	}

	public double getEmployeeSalary() {
		return employeeSalary;
	}

	public void setEmployeeSalary(double employeeSalary) {
		this.employeeSalary = employeeSalary;
	}

}
