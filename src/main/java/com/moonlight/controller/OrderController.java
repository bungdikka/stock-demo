package com.moonlight.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moonlight.util.RedisUtil;

@RestController
public class OrderController {
	
	@Autowired
	RedisUtil redisUtil;
	
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
}
