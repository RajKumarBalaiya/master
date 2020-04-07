package com.spring.async.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Service;

@Service
public class AsyncService {
	private ExecutorService executorService;

	@PostConstruct
	private void create() {
		executorService = Executors.newSingleThreadExecutor();
	}

	public void process(ThreadExecutor operation) {
		// no result operation
		executorService.submit(operation);

	}

	public Future<Integer> calculate(Integer input) {
		return executorService.submit(() -> {
			System.out.println("Calculate from service...");
			// Thread.sleep(1000);
			return input * input;
		});
	}

	@PreDestroy
	private void destroy() {
		executorService.shutdown();
	}
}
