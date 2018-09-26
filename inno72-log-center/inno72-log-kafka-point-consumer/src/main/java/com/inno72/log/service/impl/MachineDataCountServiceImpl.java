package com.inno72.log.service.impl;

import java.time.LocalDate;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.inno72.common.datetime.LocalDateUtil;
import com.inno72.common.utils.StringUtil;
import com.inno72.log.service.MachineDataCountService;
import com.inno72.log.vo.MachineGoodsCount;
import com.inno72.log.vo.PointLog;
import com.inno72.redis.IRedisUtil;

@Service
public class MachineDataCountServiceImpl implements MachineDataCountService {

	@Resource
	private MongoOperations mongoTpl;

	@Resource
	private IRedisUtil redisUtil;

	@Override
	public void countLog(PointLog pointLog) {

		String date = LocalDateUtil.transfer(LocalDate.now());

		String machineCode = pointLog.getMachineCode();
		String tag = Optional.of(pointLog.getTag()).map((v)->{
			if (!v.contains("|")){
				return v;
			}
			return v.split("\\|")[0];
		}).orElse("");

		Query query = new Query();
		query.addCriteria(Criteria.where("date").is(date));
		query.addCriteria(Criteria.where("machineCode").is(machineCode));

		String type = pointLog.getType();
		Update update = new Update();

		switch (type){
			case PointLog.POINT_TYPE_LOGIN:
				String userId = Optional.of(pointLog.getTag()).map((v)->{
					if (!v.contains("|")){
						return v;
					}
					return v.split("\\|")[1];
				}).orElse("");
				int newUv = addUv(machineCode, tag, userId, date);
				query.addCriteria(Criteria.where("activityId").is(tag));
				update.inc("pv", 1);
				update.set("uv", newUv);

			case PointLog.POINT_TYPE_ORDER:
				query.addCriteria(Criteria.where("activityId").is(tag));
				update.inc("order", 1);

			case PointLog.POINT_TYPE_FINISH:
				String shipmentId = Optional.of(pointLog.getTag()).map((v)->{
					if (!v.contains("|")){
						return v;
					}
					return v.split("\\|")[1];
				}).orElse("");
				String goodsName = Optional.of(pointLog.getTag()).map((v)->{
					if (!v.contains("|")){
						return v;
					}
					return v.split("\\|")[2];
				}).orElse("");
				addShipment(machineCode, tag, shipmentId, date, goodsName);
				query.addCriteria(Criteria.where("activityId").is(tag));
				update.inc("shipment", 1);

			case PointLog.POINT_TYPE_FANS:
				query.addCriteria(Criteria.where("activityId").is(tag));
				update.inc("fans", 1);

			case PointLog.POINT_TYPE_WARNING:
				Optional.ofNullable(pointLog.getDetail()).map(Object::toString).orElse("");
				String detail = pointLog.getDetail();
				if (StringUtil.notEmpty(detail)){
					String count = Optional.ofNullable(JSON.parseObject(detail).get("count")).map(Object::toString).orElse("");
					update.inc("fans", Integer.parseInt(count));
				}
		}

		mongoTpl.findAndModify(query, update, FindAndModifyOptions.options().upsert(true),MachineGoodsCount.class,"MachineDataCount");
	}

	//增加用户量
	private int addUv(String machineCode, String activityId, String userId, String date){
		String  UV_REDIS_KEY = "machine_data_count:"+
				machineCode+":"+
				date+":"+
				activityId+":"
				+ "uv_login_user_key";
		redisUtil.sadd(UV_REDIS_KEY, userId);

		Long scard = redisUtil.scard(UV_REDIS_KEY);

		return scard.intValue();
	}

	//增加出货
	private void addShipment(String machineCode, String activityId, String shipmentId, String date, String goodsName){
		Query query = new Query();
		query.addCriteria(Criteria.where("time").is(date));
		query.addCriteria(Criteria.where("activityId").is(activityId));
		query.addCriteria(Criteria.where("machineCode").is(machineCode));
		query.addCriteria(Criteria.where("goodsCode").is(shipmentId));

		Update update = new Update();
		update.inc("goods", 1);
		update.set("goodsName", goodsName);
		mongoTpl.findAndModify(query, update, FindAndModifyOptions.options().upsert(true), MachineGoodsCount.class, "MachineGoodsCount");

	}
}
