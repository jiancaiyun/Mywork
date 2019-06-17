package com.imooc.service;

import com.imooc.pojo.Users;
import com.imooc.pojo.UsersReport;

public interface UserService {

	//判断用户名是否存在
	public boolean queryUsernameisExist(String username);
	
	//保存用户（用户注册）
	public void saveUser(Users user);
	
	//根据用户名和密码查询用户 
	 Users queryUserForLogin(String username, String password);
	
	 //用户修改信息
	 public void updateUserInfo(Users user);
	 
	 //查询用户信息
	 public Users queryUserInfo(String userId); 
	
	 /**
		 * @Description: 查询用户是否喜欢点赞视频
		 */
		public boolean isUserLikeVideo(String userId, String videoId);
		
		/**
		 * @Description: 增加用户和粉丝的关系
		 */
		public void saveUserFanRelation(String userId, String fanId);
		
		/**
		 * @Description: 删除用户和粉丝的关系
		 */
		public void deleteUserFanRelation(String userId, String fanId);
		
		/**
		 * @Description: 查询用户是否关注
		 */
		public boolean queryIfFollow(String userId, String fanId);
		
		/**
		 * @Description: 举报用户
		 */
		public void reportUser(UsersReport userReport);
	 
}
