package com.moonlight.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.moonlight.util.RedisUtil;

@RestController
public class OrderController {
	
	@Autowired
	RedisUtil redisUtil;
	
	@Autowired
	RedisTemplate<String,Object> redisTemplate;
	
	@Autowired
	StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	RedisScript<Boolean> redisScript;
	
	String stockRedisKey = "stock";
	
	@RequestMapping("/updateStock")
	public String updateStock(String stock) {
		redisUtil.set(stockRedisKey, stock);
		return "success";
	}
	
	@GetMapping("/stock")
	public String stock() {
		return redisUtil.get(stockRedisKey);
	}

	@RequestMapping("/buy")
	public String buy() {
		//购买数量
		int buyAmount = 2;
		//redis查库存
		Integer stock = redisUtil.getInt(stockRedisKey);
		if(stock==null) {
			//读DB，更新redis
		}
		if(stock!=0 && stock>=buyAmount) {
			//库存够，先扣库存
			long result = redisUtil.decrby(stockRedisKey,buyAmount);
			if(result<0) {
				//库存扣减后为负数，购买失败，还原扣减
				redisUtil.incrby(stockRedisKey, buyAmount);
				return "库存不足";
			} else {
				//执行购买
				System.out.println("购买成功");
			}
		}else {
			return "库存不足";
		}
		return "success";
	}
	
	/**
	 * 库存扣减 lua实现
	 */
	@RequestMapping("/buylua")
	public void buylua() {
		//购买数量
		String buyAmount = "2";
		List<String> keys = new ArrayList<>();
		keys.add("stock");
		boolean isStockEnough = Boolean.parseBoolean(redisTemplate.execute(redisScript, keys, buyAmount).toString());
		if(isStockEnough) {
			System.out.println("lua购买成功");
		}
	}
}
