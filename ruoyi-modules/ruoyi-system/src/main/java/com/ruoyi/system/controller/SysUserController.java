package com.ruoyi.system.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ruoyi.common.core.constant.UserConstants;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.system.api.domain.SysDept;
import com.ruoyi.system.api.domain.SysRole;
import com.ruoyi.system.api.domain.SysUser;
import com.ruoyi.system.api.model.UserInfo;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysPermissionService;
import com.ruoyi.system.service.ISysPostService;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.utils.tree.TreeDto;


/**
 * 用户信息
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/user")
public class SysUserController extends BaseController
{
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysPostService postService;

    @Autowired
    private ISysPermissionService permissionService;
    
    @Autowired
    private ISysDeptService deptService;

    /**
     * 获取用户列表
     */
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user)
    {
        startPage();
        List<SysUser> list = userService.selectUserList(user);
        return getDataTable(list);
    }
//    
//    /**
//     * 获取用户列表
//     */
//    @GetMapping("/all/list")
//    public TableDataInfo alllist(SysUser user)
//    {
//        startPage();
//        List<SysUser> list = userService.selectALLUserList(user);
//        return getDataTable(list);
//    }
    

    @Log(title = "用户管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:user:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysUser user) throws IOException
    {
        List<SysUser> list = userService.selectUserList(user);
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        util.exportExcel(response, list, "用户数据");
    }

    @Log(title = "用户管理", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('system:user:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        List<SysUser> userList = util.importExcel(file.getInputStream());
        String operName = SecurityUtils.getUsername();
        String message = userService.importUser(userList, updateSupport, operName);
        return AjaxResult.success(message);
    }

    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) throws IOException
    {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        util.importTemplateExcel(response, "用户数据");
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/info/{username}")
    public R<UserInfo> info(@PathVariable("username") String username)
    {
        SysUser sysUser = userService.selectUserByUserName(username);
        if (StringUtils.isNull(sysUser))
        {
            return R.failed("用户名或密码错误");
        }
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(sysUser.getUserId());
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(sysUser.getUserId());
        UserInfo sysUserVo = new UserInfo();
        sysUserVo.setSysUser(sysUser);
        sysUserVo.setRoles(roles);
        sysUserVo.setPermissions(permissions);
        return R.ok(sysUserVo);
    }

    /**
     * 获取用户信息
     * 
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo()
    {
        Long userId = SecurityUtils.getLoginUser().getUserId();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(userId);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(userId);
        Set<String> postNames = postService.selectPostNameByUserId(userId);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", userService.selectUserById(userId));
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        ajax.put("postNames", postNames);
        return ajax;
    }
    
    /**
     * 获取当前用户信息
     * 
     * @return 用户信息
     */
    @GetMapping("/active/info")
    public AjaxResult getUserInfo()
    {
        Long userId = SecurityUtils.getLoginUser().getUserId();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(userId);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(userId);
        Set<String> postNames = postService.selectPostNameByUserId(userId);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", userService.selectUserById(userId));
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        ajax.put("postNames", postNames);
        return ajax;
    }
    

    /**
     * 部门人员树
     * 
     * @return 用户信息
     */
    @GetMapping("/tree")
    public AjaxResult getUserTree()
    {
    	AjaxResult ajax = AjaxResult.success();
    	List<TreeDto> tree = deptService.selectDeptTree();
        return ajax.success(tree);
    }
    
    /**
     * 自定义:获取用户所处部门信息
     * 
     * @return 用户信息
     */
    @GetMapping("/custom/dept/{userId}")
    public AjaxResult getDept(@PathVariable(value = "userId", required = false) Long userId)
    {
        AjaxResult ajax = AjaxResult.success();
        SysUser user = userService.selectUserById(userId);
        Long deptId = user.getDeptId();
        SysDept dept = deptService.selectDeptById(deptId);
        if (dept == null || StringUtils.isBlank(dept.getAncestors())) {
        	 ajax.put("dept", deptId);
        } else {
        	ajax.put("dept", dept.getAncestors() + "," +  deptId);
        }
        return ajax;
    }
    
    /**
     * 自定义:获取多部门信息
     */
    @GetMapping("/custom/depts/{deptIds}")
    public AjaxResult getDepts(@PathVariable(value = "deptIds", required = false) String deptIds)
    {
        AjaxResult ajax = AjaxResult.success();
        List<SysDept> depts = deptService.selectDeptsByIds( Arrays.asList(deptIds.split(",")));
        List<Map<String, String>> list = new ArrayList<>();
        for (SysDept dept : depts) {
        	Map<String, String> map = new HashMap<>();
        	map.put("id", dept.getDeptId() + "");
        	map.put("title", dept.getDeptName());
        	list.add(map);
        }
        ajax.put("depts", list);
        return ajax;
    }
    
    /**
     * 自定义:获取多用户信息
     */
    @GetMapping("/custom/users/{userIds}")
    public AjaxResult getUsersInfo(@PathVariable(value = "userIds", required = false) String userIds)
    {
    	AjaxResult ajax = AjaxResult.success();
    	List<SysUser> users = userService.selectUsersByIds(Arrays.asList(userIds.split(",")));
        List<Map<String, String>> list = new ArrayList<>();
        for (SysUser user : users) {
        	Map<String, String> map = new HashMap<>();
        	map.put("id", user.getUserId() + "");
        	map.put("trueName", user.getNickName());
        	list.add(map);
        }
        ajax.put("users", list);
        return ajax;
    }
    
    /**
     * 自定义:获取多用户信息
     */
    @GetMapping("/activiti/users/{userIds}")
    public List<Map<String, String>> getActivitiUsersInfo(@PathVariable(value = "userIds", required = false) String userIds)
    {
    	List<Map<String, String>> list = new ArrayList<>();
    	List<SysUser> users = userService.selectUsersByIds(Arrays.asList(userIds.split(",")));
        for (SysUser user : users) {
        	Map<String, String> map = new HashMap<>();
        	map.put("id", user.getUserId() + "");
        	map.put("userName", user.getUserName());
        	map.put("trueName", user.getNickName());
        	list.add(map);
        }
        return list;
    }
    
    /**
     * 自定义:获取多部门
     */
    @GetMapping("/activiti/depts/{userIds}")
	public List<Map<String, String>> findDeptById(String userIds) {
    	List<Map<String, String>> list = new ArrayList<>();
    	List<SysDept> entitys = deptService.selectDeptsByIds(Arrays.asList(userIds.split(",")));
    	entitys.forEach(d -> {
    		Map<String, String> map = new HashMap<>();
        	map.put("id", d.getDeptId() + "");
        	map.put("name", d.getDeptName());
        	list.add(map);
    	});
    	return list;
    }

    /**
     * 自定义:获取多角色
     */
    @GetMapping("/activiti/roles/{roleids}")
	public List<Map<String, String>> findRoleById(String roleids) {
    	List<Map<String, String>> list = new ArrayList<>();
    	List<SysRole> entitys = roleService.selectRolesByIds(Arrays.asList(roleids.split(",")));
    	entitys.forEach(d -> {
    		Map<String, String> map = new HashMap<>();
        	map.put("id", d.getRoleId() + "");
        	map.put("name", d.getRoleName());
        	list.add(map);
    	});
    	return list;
    }
    
    /**
     * 自定义:获取多用户根据角色
     */
    @GetMapping("/activiti/role/users/{roleids}")
	public List<Map<String, String>> getUsersByRoleIds(String roleids) {
    	List<Map<String, String>> list = new ArrayList<>();
    	List<SysUser> users = userService.selectUsersByRoleIds(Arrays.asList(roleids.split(",")));
        for (SysUser user : users) {
        	Map<String, String> map = new HashMap<>();
        	map.put("id", user.getUserId() + "");
        	map.put("userName", user.getUserName());
        	map.put("trueName", user.getNickName());
        	list.add(map);
        }
        return list;
    }
    
    
    
    
    /**
     * 自定义:获取用户信息
     * 
     * @return 用户信息
     */
    @GetMapping("/activiti/userName/{userId}")
    public String getUserName(@PathVariable(value = "userId", required = true) String userId)
    {
        SysUser user = userService.selectUserById(Long.parseLong(userId));
        return user == null ? "" : user.getUserName();
    }

    
    /**
     * 自定义:是否管理员
     * 
     * @return 用户信息
     */
    @GetMapping("/custom/isAdmin/{userId}")
    public AjaxResult isAdmin(@PathVariable(value = "userId", required = false) Long userId)
    {
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(userId);
        boolean bl = false;
        for (String role : roles) {
        	if ("admin".equals(role)) {
        		bl = true;
        		break;
        	}
        }
       
        AjaxResult ajax = AjaxResult.success();
        ajax.put("isAdmin", bl);
        return ajax;
    }
    
  
    /**
     * 根据用户编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping(value = { "/", "/{userId}" })
    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) Long userId)
    {
        AjaxResult ajax = AjaxResult.success();
        ajax.put("roles", roleService.selectRoleAll());
        ajax.put("posts", postService.selectPostAll());
        if (StringUtils.isNotNull(userId))
        {
            ajax.put(AjaxResult.DATA_TAG, userService.selectUserById(userId));
            ajax.put("postIds", postService.selectPostListByUserId(userId));
            ajax.put("roleIds", roleService.selectRoleListByUserId(userId));
        }
        return ajax;
    }

    /**
     * 新增用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:add')")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysUser user)
    {
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user.getUserName())))
        {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        }
        else if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user)))
        {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        else if (UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user)))
        {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setCreateBy(SecurityUtils.getUsername());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        return toAjax(userService.insertUser(user));
    }

    /**
     * 修改用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user)))
        {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        else if (UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user)))
        {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userService.updateUser(user));
    }

    /**
     * 删除用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:remove')")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds)
    {
        return toAjax(userService.deleteUserByIds(userIds));
    }

    /**
     * 重置密码
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userService.resetPwd(user));
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        user.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userService.updateUserStatus(user));
    }
}
