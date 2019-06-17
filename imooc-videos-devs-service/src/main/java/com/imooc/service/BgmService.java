package com.imooc.service;

import java.util.List;

import com.imooc.pojo.Bgm;

public interface BgmService {
	
	//查询背景音乐
	public List<Bgm> queryBgmList();
	
	public Bgm queryBgmById(String bgmId);
}
