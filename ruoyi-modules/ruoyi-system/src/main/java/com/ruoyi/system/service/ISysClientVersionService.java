package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.SysClientVersion;

/**
 * <p>
 * 系统版本表 服务类
 * </p>
 *
 * @author Robinxiao
 * @since 2020-07-20
 */
public interface ISysClientVersionService extends IService<SysClientVersion> {

	SysClientVersion getLatestVersion();

}
