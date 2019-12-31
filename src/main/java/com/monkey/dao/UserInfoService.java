package com.monkey.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.monkey.core.Query;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @title: UserInfoService
 * @projectName monkey2
 * @description: TODO
 * @date 2019/11/811:28
 */

public interface UserInfoService {

    IPage<HashMap<String,Object>> getUserInfo(Page page,@Param("q") Query query);

    void setUserInfo(@Param("q") Map<String,Object> params);

    List<Map<String,Object>> getInfoAll();

    void insertUserInfo(@Param("p")String openId,@Param("q") String name);

    void deleteUserInfo(@Param("p")String openId,@Param("q") String name);

}
