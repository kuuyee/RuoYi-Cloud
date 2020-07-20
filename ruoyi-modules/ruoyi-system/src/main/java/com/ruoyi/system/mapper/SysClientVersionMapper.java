package com.ruoyi.system.mapper;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.SysClientVersion;

/**
 * <p>
 * 系统版本表 Mapper 接口
 * </p>
 *
 * @author Robinxiao
 * @since 2020-07-20
 */
public interface SysClientVersionMapper extends BaseMapper<SysClientVersion> {

	@Select("select * from sys_client_version order by id desc limit 1")
	SysClientVersion getLatestVersion();

}
