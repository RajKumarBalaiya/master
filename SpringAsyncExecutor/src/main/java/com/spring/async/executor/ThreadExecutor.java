package com.spring.async.executor;

public class ThreadExecutor implements Runnable {

	private String name;

	public ThreadExecutor(String name) {
		this.name = name;

	}

	@Override
	public void run() {

		System.out.println("Employee name:" + name);
	}

}
