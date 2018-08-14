package com.inno72;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Test {
	public static void main(String[] args) throws InterruptedException {
		ExecutorService executor = Executors.newCachedThreadPool();

		List<Future<Integer>> list = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			Task task = new Task();
			Future<Integer> result = executor.submit(task);
			System.out.println("主线程添加任务");
			list.add(result);
		}

		executor.shutdown();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		System.out.println("主线程在执行任务");

		try {
			for (int i = 0; i < list.size(); i++) {
				Future<Integer> result = list.get(i);
				System.out.println("task运行结果"+result.get());
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		System.out.println("所有任务执行完毕");
	}
}
class Task implements Callable<Integer> {
	@Override
	public Integer call() throws Exception {
		Thread.sleep(10000);
		System.out.println(Thread.currentThread().getName() + "子线程在进行计算");
		Thread.sleep(10000);
		int sum = 0;
		for(int i=0;i<100;i++)
			sum += i;
		return sum;
	}
}