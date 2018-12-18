package com.example;

import java.util.concurrent.locks.*;
import java.util.concurrent.*;
// 尝试锁测试，使用lock具有更大灵活性
public class AttempLock{
	ReentrantLock lock=new ReentrantLock();

	public void time(){
		boolean capture=false;
		try{
			System.out.println("try lock within time");
			// 尝试2秒内获取锁
			capture=lock.tryLock(2,TimeUnit.SECONDS);
		}catch(InterruptedException e){
			throw new RuntimeException(e);
		}
		System.out.println("capture reuslt within 2s"+capture);
		if(capture){
			lock.unlock();
		}

	}

	public void untime(){
		boolean capture=false;
		try{
			System.out.println("try lock untime");
			capture=lock.tryLock();
			System.out.println("capture reuslt at once"+capture);

		}finally{
			if(capture){
				lock.unlock();

			}
		}
	}
	public static void main(String[] args) {
		final AttempLock attempLock=new AttempLock();
		// attempLock.time();
		// attempLock.untime();
		new Thread(){
			public void run(){
				System.out.println("lock in another thread");
				// 先获取锁
				attempLock.lock.lock();
				try{
					// 一秒后释放
					TimeUnit.SECONDS.sleep(1);
				}catch(InterruptedException e){

				}finally{
					attempLock.lock.unlock();
				}
			}
		}.start();
		Thread.yield();
		attempLock.time();
		// attempLock.untime();
	}
}

