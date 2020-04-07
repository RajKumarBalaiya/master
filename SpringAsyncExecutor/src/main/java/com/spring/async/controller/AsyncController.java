package com.spring.async.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.async.executor.AsyncService;
import com.spring.async.executor.ThreadExecutor;

@RestController
@RequestMapping("/async")
public class AsyncController {
	@Autowired
	private AsyncService asyncService;

	/*
	 * public void setAsyncService(AsyncService asyncService) { this.asyncService =
	 * asyncService; }
	 */

	@GetMapping("/employee")
	public ResponseEntity<String> getEmployeeName(@RequestParam String name) {
		asyncService.process(new ThreadExecutor(name));
		return ResponseEntity.status(HttpStatus.OK).body(name);
	}
}