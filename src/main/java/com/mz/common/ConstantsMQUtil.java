/*
 * @(#)ConstantsUtil.java 2013-11-12下午03:25:14
 * Copyright 2013 sinovatech, Inc. All rights reserved.
 */
package com.mz.common;

import org.springframework.stereotype.Component;

/**
 * MQ常量工具类
 *
 * @author yangh
 */
@Component("ConstantsMQUtil")
public class ConstantsMQUtil {

	/**
	 * 用户离线或上线交换器
	 */
	public static final String USER_OFFORONLINE_EXCHANGE = "forest.userOffOrOnLineExchange";
	public static final String USER_OFFORONLINE_EXCHANGE_DEV = "forest.userOffOrOnLineExchange.dev";

	/**
	 * 用户离线或上线队列
	 */
	public static final String USER_OFFORONLINE_QUEUE = "forest.userOffOrOnLineQueue";
	public static final String USER_OFFORONLINE_QUEUE_DEV = "forest.userOffOrOnLineQueue.dev";

	/**
	 * 用户离线或上线路由键 routingKey
	 */
	public static final String USER_OFFORONLINE_ROUTINGKEY = "forest.userOffOrOnLine";
	public static final String USER_OFFORONLINE_ROUTINGKEY_DEV = "forest.userOffOrOnLine.dev";

	/**
	 * 微信公众号通知交换器
	 */
	public static final String WXMP_PUBLIC_EXCHANGE = "forest.wxmp.public.exchange";
	public static final String WXMP_PUBLIC_EXCHANGE_DEV = "forest.wxmp.public.exchange.dev";

	/**
	 * 微信公众号通知队列
	 */
	public static final String WXMP_PUBLIC_QUEUE = "forest.wxmp.public.queue";
	public static final String WXMP_PUBLIC_QUEUE_DEV = "forest.wxmp.public.queue.dev";

	/**
	 * 微信公众号通知路由键 routingKey
	 */
	public static final String WXMP_PUBLIC_ROUTINGKEY = "forest.wxmp.public.routingKey";
	public static final String WXMP_PUBLIC_ROUTINGKEY_DEV = "forest.wxmp.public.routingKey.dev";


	/**
	 * 日志交换器
	 */
	public static final String LOG_EXCHANGE = "forest.log.exchange";
	public static final String LOG_EXCHANGE_DEV = "forest.log.exchange.dev";

	/**
	 * 日志队列
	 */
	public static final String LOG_QUEUE = "forest.log.queue";
	public static final String LOG_QUEUE_DEV = "forest.log.queue.dev";

	/**
	 * 日志路由键 routingKey
	 */
	public static final String LOG_ROUTINGKEY = "forest.log.routingKey";
	public static final String LOG_ROUTINGKEY_DEV = "forest.log.routingKey.dev";
	/**
	 * 摄像头 事件通知类型
	 */
	public static final String TYPE_RESOURCE = "resource";
	/**
	 * 摄像头 告警通知类型
	 */
	public static final String TYPE_ALARM = "Alarm";
	/**
	 * 经纬度上报设备(imei) 离线通知
	 */
	public static final String TYPE_IMEI_LINE = "IMEI_LINE";
	/**
	 * 微信通知 任务分派提醒
	 */
	public static final String TYPE_SEND_TASK = "sendTask";
	/**
	 * 微信通知 验收结果通知
	 */
	public static final String TYPE_CHECK_NOTICE = "checkNotice";
	/**
	 * 通知类型key
	 */
	public static final String TYPE_KEY = "noticeTypeKey";

	/**
	 * 消息key
	 */
	public static final String MESSAGE_KEY = "message";
}
