package com.ruoyi.system.controller;
 
 
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.system.domain.SysClientVersion;
import com.ruoyi.system.domain.vo.ClientVersionVO;
import com.ruoyi.system.service.ISysClientVersionService;
import com.ruoyi.system.utils.PageUtil;
import com.ruoyi.system.utils.PageVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
 
/**
* @author Robinxiao
* @since 2020-07-20
*/
@Api(description = "系统版本表" )
@RestController
@RequestMapping("/client-version" )
public class SysClientVersionRestController  {
 
    @Autowired
    private ISysClientVersionService  sysClientVersionService;
 
    @ApiOperation("检测是否有新版本" )
    @GetMapping("/check" )
    public AjaxResult check(String versionNumber) {
    	SysClientVersion version = sysClientVersionService.getLatestVersion();
    	ClientVersionVO vo = new ClientVersionVO();
    	vo.setHaveNewVersion(true);
    	vo.setVersion(version);
        if (version == null || version.getVersionNumber().equals(versionNumber)) {
        	vo.setHaveNewVersion(false);
        }

   		return AjaxResult.success(vo);
    }
    
    
    @ApiOperation("分页" )
    @GetMapping("/page" )
    public AjaxResult page(PageVo page, SysClientVersion req) {
        IPage<SysClientVersion> data = sysClientVersionService.page(PageUtil.initMpPage(page));
   		return AjaxResult.success(data);
    }
 
    @ApiOperation("添加" )
    @PostMapping("" )
    public AjaxResult add(@RequestBody SysClientVersion req) {
        sysClientVersionService.save(req);
        return AjaxResult.success();
    }
 
    @ApiOperation("修改" )
    @PutMapping("" )
     public AjaxResult update(@RequestBody SysClientVersion req) {
        sysClientVersionService.updateById(req);
        return AjaxResult.success();
    }
 
    @ApiOperation("删除" )
    @DeleteMapping("" )
    public AjaxResult delete(String[] ids) {
        if (ids == null || ids.length == 0) {
			return AjaxResult.error("ids is null!");
        }
        List<String> idList = Arrays.asList(ids);
		sysClientVersionService.removeByIds(idList);
		return AjaxResult.success();
     }
 }