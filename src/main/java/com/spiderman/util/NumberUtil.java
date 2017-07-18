package com.spiderman.util;

import java.util.Random;

public class NumberUtil {
	
	static Random random = new Random();
	/**
	 * 根据最大值与最小值随机获取
	 * @param min
	 * @param max
	 * @return
	 */
	public static int randDom(int min,int max) {
		return random.nextInt(max) % (max - min + 1) + min;
	}
	
}
