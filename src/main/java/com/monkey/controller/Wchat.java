package com.monkey.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.monkey.core.HttpRequest;
import com.monkey.core.MDK;
import com.monkey.core.Query;
import com.monkey.core.R;
import com.monkey.dao.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @title: Wchat
 * @projectName monkey2
 * @description: TODO
 * @date 2019/11/810:33
 */
@RestController
@RequestMapping("/wxc")
public class Wchat {

    @Autowired
    UserInfoService userInfoService;

    @RequestMapping("/getUserInfo")
    public R getUserInfo(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        IPage<HashMap<String, Object>> page = userInfoService.getUserInfo(new Page(query.getPage(), query.getLimit()), query);
        System.out.println(page.getRecords());
        return R.ok().put("data", page.getRecords()).put("count", page.getTotal());
    }

    @RequestMapping("/setUserInfo")
    public R setUserInfo(@RequestParam Map<String, Object> params) {
        userInfoService.setUserInfo(params);
        return R.ok();
    }

    @RequestMapping("/getInfoAll")
    public R getInfoAll(@RequestParam Map<String, Object> params) {
        String mids = HttpRequest.sendPost(MDK.URL+"/info/infoAll", "key=sipaote");
        Map<String, Object> jsonMap = JSONObject.toJavaObject(JSONObject.parseObject(mids), Map.class);
        List<Map<String, Object>> res = (List<Map<String, Object>>) jsonMap.get("list");
        List<Map<String, Object>> mid = userInfoService.getInfoAll();
        int[] key = {0};
        res.stream().forEach(v -> {
            mid.stream().forEach(o -> {
                if (o.get("openId") != null && v.get("openId").toString().equals(o.get("openId"))) {
                    key[0]++;
                }
            });
            if (key[0] == 0) {
                userInfoService.insertUserInfo(v.get("openId")+"", v.get("name")+"");
            }else{
                key[0] = 0;
            }
        });
        key[0] = 0;
        mid.stream().forEach(v->{
            res.stream().forEach(o -> {
                if (o.get("openId") != null && v.get("openId").toString().equals(o.get("openId"))) {
                    key[0]++;
                }
            });
            if (key[0] == 0) {
                userInfoService.deleteUserInfo(v.get("openId")+"", v.get("name")+"");
            }else{
                key[0] = 0;
            }
        });
        return R.ok();
    }
}
