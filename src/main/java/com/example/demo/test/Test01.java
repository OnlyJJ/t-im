package com.example.demo.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javax.crypto.Cipher;

public class Test01 {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		FutureTask<String> futrue = new FutureTask<String>(new FutrueTest());
		new Thread(futrue).start();
		FutureTask<String> futrue2 = new FutureTask<String>(new FutrueTest2());
		new Thread(futrue2).start();
		System.err.println("begin...");
		String str = futrue.get();
		System.err.println("str1 = " + str );
		String str2 = futrue2.get();
		System.err.println("str2 = " + str2 );
		
		List<Map> list = new ArrayList<Map>();
		Map m = new HashMap<>();
		for(int i=0;i<100;i++) {
			m.put(i+"", i);
			list.add(m);
		}
		list.stream().sorted();
		//
		// 1213
		System.err.println("test");
		
	}
	
	
}

class FutrueTest implements Callable<String> {

	@Override
	public String call() throws Exception {
		Thread.sleep(1000);
		return "hello call 1！";
	}
	
}

class FutrueTest2 implements Callable<String> {

	@Override
	public String call() throws Exception {
		Thread.sleep(1000);
		return "hello call 2！";
	}
	
}