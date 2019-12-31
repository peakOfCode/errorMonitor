package com.monkey.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.monkey.core.Query;
import com.monkey.entity.SysError;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @title: SysError
 * @projectName monkey2
 * @description: TODO
 * @date 2019/11/114:26
 */
public interface SysErrorService extends BaseMapper<SysError> {

    List<Map<String,Object>> getSysError(@Param("query") Map<String,Object> query);


    List<Map<String,Object>> getType();

    List<Map<String,Object>> getSysErrorNum(@Param("query") Map<String,Object> mp);

    List<Map<String,Object>> setStaticInfo(@Param("query") Map<String,Object> mp);

    IPage<HashMap<String,Object>> getErrorInfo(IPage<HashMap<String,Object>> pageUtil, @Param("query") Query query);

    List<Map<String,Object>> findInfo(@Param("p") String s);

    void updateSysError(@Param("p") String id);

}
