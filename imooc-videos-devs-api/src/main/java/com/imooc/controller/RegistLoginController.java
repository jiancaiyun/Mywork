package com.imooc.controller;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.imooc.pojo.Users;
import com.imooc.pojo.vo.UsersVO;
import com.imooc.service.UserService;
import com.imooc.utils.IMoocJSONResult;
import com.imooc.utils.MD5Utils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;



@RestController
@Api(value="用户注册登录的接口",tags= {"用户注册的接口"})
public class RegistLoginController extends BasicController {

	@Autowired
	private UserService userService;
	@ApiOperation(value="用户注册",notes="用户注册的接口")
	@PostMapping("/regist")
	public IMoocJSONResult regist( @RequestBody Users user)throws Exception {
		//判断用户名和密码必须不为空
		if(StringUtils.isBlank(user.getUsername())||StringUtils.isBlank(user.getPassword())) {
			return IMoocJSONResult.errorMsg("用户名和密码不能为空");
		}
		
		//判断用户名是否存在
		boolean usernameIsExist = userService.queryUsernameisExist(user.getUsername());
		
		//保存用户，注册信息
		if(!usernameIsExist) {
			user.setNickname(user.getUsername());
			user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
			user.setFansCounts(0);
			user.setReceiveLikeCounts(0);
			user.setFollowCounts(0);
			userService.saveUser(user);
		}
		else {
			return IMoocJSONResult.errorMsg("用户名已经存在，请换一个试试");
		}
		user.setPassword("");
		UsersVO userVO = setUserRedisSessionToken(user);
		
	return IMoocJSONResult.ok(userVO);
	}
	
	public UsersVO setUserRedisSessionToken(Users userModel) {
		String uniqueToken = UUID.randomUUID().toString();
		redis.set(USER_REDIS_SESSION + ":" + userModel.getId(), uniqueToken, 1000 * 60 * 30);
		
		UsersVO userVO = new UsersVO();
		BeanUtils.copyProperties(userModel, userVO);
		userVO.setUserToken(uniqueToken);
		return userVO;
	}
	
	@ApiOperation(value="用户登录",notes="用户登录的接口")
	@PostMapping("/login")
	public IMoocJSONResult login(@RequestBody Users user)throws Exception{
		String username=user.getUsername();
		String passowrd = user.getPassword();
		//判断用户名和密码必须不为空
		if(StringUtils.isBlank(user.getUsername())||StringUtils.isBlank(user.getPassword())) {
			return IMoocJSONResult.errorMsg("用户名和密码不能为空");
		}
		
		//判断用户是否存在
		Users userResult = userService.queryUserForLogin(username, MD5Utils.getMD5Str(user.getPassword()));
		
		//返回
		if(userResult !=null) {
			userResult.setPassword("");
			UsersVO userVO = setUserRedisSessionToken(userResult);
			return IMoocJSONResult.ok(userVO);
		} else {
			return IMoocJSONResult.errorMsg("用户名或密码不正确，请重试");
		}
	}
	
	@ApiOperation(value="用户注销",notes="用户注销接口")
	@ApiImplicitParam(name="userId",value="用户Id",required=true,dataType="String",paramType="query")
	@PostMapping("/logout")
	public IMoocJSONResult logout(String userId)throws Exception{
		
		redis.del(USER_REDIS_SESSION + ":" + userId);
		return IMoocJSONResult.ok();
	}
}
