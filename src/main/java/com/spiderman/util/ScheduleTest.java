package com.spiderman.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduleTest {

	public void runTest(){
		ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

		service.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				System.out.println("test schedule!");
			}
		}, 0, 30 , TimeUnit.SECONDS);
	}

	public static void main(String[] args){
		new ScheduleTest().runTest();
	}
}
