package com.ruoyi.system.domain.vo;

import com.ruoyi.system.domain.SysClientVersion;

import lombok.Data;


@Data
public class ClientVersionVO
{
    boolean haveNewVersion;
    SysClientVersion version;
}
