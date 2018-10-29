package com.inno72.service.impl;

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
import com.inno72.util.CommonBean;
import com.inno72.model.MachineGoodsCount;
import com.inno72.model.PointLog;
import com.inno72.mapper.CommonMapper;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.MachineDataCountService;

@Service
public class MachineDataCountServiceImpl implements MachineDataCountService {

	@Resource
	private MongoOperations mongoTpl;

	@Resource
	private IRedisUtil redisUtil;

	@Resource
	private CommonMapper commonMapper;

	@Override
	public void countLog(PointLog pointLog) {

		String date = LocalDateUtil.transfer(LocalDate.now());

		String machineCode = pointLog.getMachineCode();
		String tag = pointLog.getTag();

		Query query = new Query();
		query.addCriteria(Criteria.where("date").is(date));
		query.addCriteria(Criteria.where("machineCode").is(machineCode));

		String activityId = Optional.of(tag).map((v)->{
			if (!v.contains("|")){
				return v;
			}
			return v.split("\\|")[0];
		}).orElse("");


		String type = pointLog.getType();
		if (type.equals(PointLog.POINT_TYPE_WARNING)){
			activityId = getActivityId(machineCode);
		}
		query.addCriteria(Criteria.where("activityId").is(activityId));

		Update update = new Update();

		switch (type){
			case PointLog.POINT_TYPE_LOGIN:
				String userId = Optional.of(pointLog.getTag()).map((v)->{
					if (!v.contains("|")){
						return v;
					}
					return v.split("\\|")[1];
				}).orElse("");

				int newUv = addUv(machineCode, activityId, userId, date);
				update.inc("pv", 1);
				update.set("uv", newUv);
				break;
			case PointLog.POINT_TYPE_ORDER:
				update.inc("order", 1);
				break;
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
				this.addShipment(machineCode, activityId, shipmentId, date, goodsName);
				update.inc("shipment", 1);
				break;
			case PointLog.POINT_TYPE_FANS:
				update.inc("fans", 1);
				break;
			case PointLog.POINT_TYPE_WARNING:
				String count = Optional.ofNullable(JSON.parseObject(tag).get("count")).map(Object::toString).orElse("");
				update.inc("visitor", Integer.parseInt(count));
				break;
		}

		mongoTpl.findAndModify(query, update, FindAndModifyOptions.options().upsert(true),MachineGoodsCount.class,"MachineDataCount");
	}

	//增加用户量
	private int addUv(String machineCode, String activityId, String userId, String date){
		String  UV_REDIS_KEY = CommonBean.REDIS_MACHINE_ACTIVITY_ID +
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

	/**
	 * 通过machineCode查询活动ID， 机器可能没有活动 ！！！
	 * 没有活动的情况下返回 「-1」 表示没有活动
	 * 该活动ID缓存 10分钟失效，失效后重新刷新活动ID
	 * @param machineCode 机器code
	 * @return activityId
	 */
	private String getActivityId(String machineCode){
		String machine_activity_key = CommonBean.REDIS_MACHINE_ACTIVITY_ID
				+
				machineCode
				+ "activity_id";
		String activityId = redisUtil.get(machine_activity_key);
		if (StringUtil.isEmpty(activityId)){
			activityId = commonMapper.findActivityIdByMachineCode(machineCode);
			if ( StringUtil.isEmpty(activityId) ){
				activityId = "-1";
			}
			redisUtil.setex(machine_activity_key, 60 * 10, activityId);
		}
		return activityId;
	}
}
