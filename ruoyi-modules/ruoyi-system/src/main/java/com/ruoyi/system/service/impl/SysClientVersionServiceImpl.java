package com.ruoyi.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.system.domain.SysClientVersion;
import com.ruoyi.system.mapper.SysClientVersionMapper;
import com.ruoyi.system.service.ISysClientVersionService;

/**
 * <p>
 * 系统版本表 服务实现类
 * </p>
 *
 * @author Robinxiao
 * @since 2020-07-20
 */
@Service
public class SysClientVersionServiceImpl extends ServiceImpl<SysClientVersionMapper, SysClientVersion> implements ISysClientVersionService {

	@Autowired
	SysClientVersionMapper clientVersionMapper;
	@Override
	public SysClientVersion getLatestVersion() {
		return clientVersionMapper.getLatestVersion();
	}

}
