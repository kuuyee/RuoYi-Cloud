package com.ruoyi.system.config;

import java.util.Date;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ruoyi.common.security.domain.LoginUser;
import com.ruoyi.common.security.utils.SecurityUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 字段填充审计
 * 
 * @author Exrick
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

	/*
	 * 创建时间
	 */
	private final String createTime = "createTime";
	/**
	 * 修改时间
	 */
	private final String updateTime = "updateTime";
	/**
	 * 创建者ID
	 */
	private final String createUid = "createUid";
	/**
	 * 创建者ID
	 */
	private final String createBy = "createBy";
	/**
	 * 创建者
	 */
	private final String creator = "creator";
	/**
	 * 修改者
	 */
	private final String modifier = "updated";
	/**
	 * 修改者
	 */
	private final String updateBy = "updateBy";
	/**
	 * 登录IP
	 */
	private final String ip = "ip";

	@Override
	public void insertFill(MetaObject metaObject) {
		LoginUser user = SecurityUtils.getLoginUser();
		if (user != null) {
			String userName = user.getUsername();
			this.setFieldValByName(createUid, user.getUserId() + "", metaObject);
			this.setFieldValByName(createBy, userName, metaObject);
			this.setFieldValByName(creator, userName, metaObject);
			this.setFieldValByName(updateBy, userName, metaObject);
			this.setFieldValByName(modifier, userName, metaObject);
		} else {
			try {
				throw new Exception("登录过期，请重新登录");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.setFieldValByName(updateTime, new Date(), metaObject);
		this.setFieldValByName(createTime, new Date(), metaObject);
	}


	@Override
	public void updateFill(MetaObject metaObject) {
		LoginUser user = SecurityUtils.getLoginUser();
		String userName = user.getUsername();
		if (user != null) {
			this.setFieldValByName(updateBy, userName, metaObject);
			this.setFieldValByName(modifier, userName, metaObject);
		}
		this.setFieldValByName(updateTime, new Date(), metaObject);
	}

}
