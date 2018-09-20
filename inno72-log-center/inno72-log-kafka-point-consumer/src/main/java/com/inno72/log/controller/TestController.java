package com.inno72.log.controller;

import java.time.LocalDate;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.common.datetime.LocalDateUtil;
import com.inno72.log.vo.MachineGoodsCount;

@RestController
public class TestController {

	@Resource
	private MongoOperations mongoTpl;

	@RequestMapping("test")
	public void test(){
		String date = LocalDateUtil.transfer(LocalDate.now());
		Query query = new Query();
		query.addCriteria(Criteria.where("time").is(date));
		query.addCriteria(Criteria.where("machineCode").is("234"));
		query.addCriteria(Criteria.where("goodsCode").is("345"));

		Update update = new Update();
		update.set("time", date);
		update.set("activityId", "123");
		update.set("machineCode", "234");
		update.set("goodsCode", "345");

		FindAndModifyOptions options= new FindAndModifyOptions();
		options.upsert(true);
		options.isUpsert();
		mongoTpl.findAndModify(query, update, options, MachineGoodsCount.class, "MachineGoodsCount");
	}

	@RequestMapping("test1")
	public void test1(){
		String date = LocalDateUtil.transfer(LocalDate.now());
		Query query = new Query();
		query.addCriteria(Criteria.where("time").is(date));
		query.addCriteria(Criteria.where("machineCode").is("234"));
		query.addCriteria(Criteria.where("goodsCode").is("346"));

		Update update = new Update();
		update.inc("goods", 1);

		mongoTpl.findAndModify(query, update, FindAndModifyOptions.options().upsert(true), MachineGoodsCount.class, "MachineGoodsCount");
	}

	@RequestMapping("test2")
	public void test2(){
		String date = LocalDateUtil.transfer(LocalDate.now());

		MachineGoodsCount count = new MachineGoodsCount();
		count.setTime(date);
		count.setMachineCode("123");
		mongoTpl.insert(count, "MachineGoodsCount");
	}
}
