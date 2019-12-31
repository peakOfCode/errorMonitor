package com.monkey.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.monkey.core.Query;
import com.monkey.core.R;
import com.monkey.dao.SysErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Administrator
 * @title: GetErrorInfo
 * @projectName monkey2
 * @description: TODO
 * @date 2019/10/3110:58
 */
@RestController
@RequestMapping("error")

public class GetErrorInfo {

    @Autowired
    SysErrorService sysError;

    @RequestMapping("getErrorInfo")
    public R getErrorinfo(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        IPage<HashMap<String, Object>> page = sysError.getErrorInfo(new Page(query.getPage(), query.getLimit()), query);
        System.out.println(page.getRecords());
        return R.ok().put("data", page.getRecords()).put("count", page.getTotal());
    }

    @RequestMapping("/getMachInfo")
    public R getMachInfo() {
        List<Map<String, Object>> ls = sysError.getType();
        Integer[] i = {0};
        ls.stream().forEach(v -> v.put("id", i[0]++));
        return R.ok().put("map", ls);
    }


    @RequestMapping("/setStaticInfo")
    public R setStaticInfo(@RequestParam Map<String, Object> mp) throws ParseException {
        Calendar c = new GregorianCalendar();
        Date date1 = new Date();
        c.setTime(date1);//设置参数时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        switch (mp.get("date") + "") {
            case "1":
                mp.put("hour", 1);
                df = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
                c.add(Calendar.HOUR, -24);//把日期往后增加SECOND 秒.整数往后推,负数往前移动
                break;
            case "2":
                c.add(Calendar.HOUR, -168);//把日期往后增加SECOND 秒.整数往后推,负数往前移动
                break;
            case "3":
                c.add(Calendar.MONTH, -1);//把日期往后增加SECOND 秒.整数往后推,负数往前移动
                break;
            default:
                mp.put("hour", 1);
                df = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
                c.add(Calendar.HOUR, -24);//把日期往后增加SECOND 秒.整数往后推,负数往前移动
                break;
        }
        Date date0 = c.getTime(); //这个时间就是日期往后推一天的结果
        mp.put("start", date0);
        mp.put("end", date1);
        List<Map<String, Object>> ls = sysError.setStaticInfo(mp);
        List<Map<String, Object>> ss = new ArrayList<>();
        while (date0.before(date1)) {
            Map<String, Object> m = new HashMap<>();
            m.put("createTime", df.format(date1));
            ss.add(m);
            c.setTime(date1);
            if ("1".equals(mp.get("date") + "")) {
                c.add(Calendar.HOUR, -1);
            } else {
                c.add(Calendar.HOUR, -24);
            }
            date1 = c.getTime();
        }
        //System.out.println(ls);
        String key="";
        for (int i = 0; i < ls.size(); i++) {
            if(key.equals(ls.get(i).get("name").toString())){
                continue;
            }else {
                key = ls.get(i).get("name").toString();
            }
            for (int j = ss.size() - 1; j > -1; j--) {
                //System.out.println(ss.get(j).get("createTime") + ">>>><<<<<" + df.parse(ls.get(i).get("createTime").toString()) + "==" + i);
                if (df.parse(ss.get(j).get("createTime").toString()).before(df.parse(ls.get(i).get("createTime").toString()))) {
                    Map<String, Object> m = new HashMap<>();
                    m.put("num", 0);
                    m.put("createTime", ss.get(j).get("createTime"));
                    m.put("name", ls.get(i).get("name"));
                    if (!getIsExist(ls,m)) {
                        ls.add(i, m);
                    }
                    i++;
                    //System.out.println(i);
                    continue;
                }
                if (df.parse(ss.get(j).get("createTime").toString()).after(df.parse(ls.get(i).get("createTime").toString()))) {
                    Map<String, Object> m = new HashMap<>();
                    m.put("num", 0);
                    m.put("name", ls.get(i).get("name"));
                    m.put("createTime", ss.get(j).get("createTime"));
                    if (!getIsExist(ls,m)) {
                        i++;
                        ls.add(i, m);
                    }else{
                        i++;
                    }
                    continue;
                }
                if ((ss.get(j).get("createTime")).equals(ls.get(i).get("createTime"))) {
                    continue;
                }
            }
        }
        //System.out.println(ls);
        return R.ok().put("map", ls);
    }

    private Boolean getIsExist(List<Map<String,Object>> ls,Map<String,Object> mp){
        for(int i = 0;i<ls.size();i++){
            if(ls.get(i).get("createTime").equals(mp.get("createTime")) && ls.get(i).get("name").equals(mp.get("name"))){
                return true;
            }
        }
        return false;
    }

    @RequestMapping("/getChangeInfo")
    public R getChangeInfo(@RequestParam Map<String, Object> mp) {
        Calendar c = new GregorianCalendar();
        Date date1 = new Date();
        c.setTime(date1);//设置参数时间
        switch (mp.get("time") + "") {
            case "1":
                c.add(Calendar.SECOND, -30);//把日期往后增加SECOND 秒.整数往后推,负数往前移动
                break;
            case "2":
                c.add(Calendar.MINUTE, -1);//把日期往后增加SECOND 秒.整数往后推,负数往前移动
                break;
            case "3":
                c.add(Calendar.MINUTE, -2);//把日期往后增加SECOND 秒.整数往后推,负数往前移动
                break;
            default:
                c.add(Calendar.MINUTE, -1);//把日期往后增加SECOND 秒.整数往后推,负数往前移动
                break;
        }
        Date date0 = c.getTime(); //这个时间就是日期往后推一天的结果
        mp.put("start", date0);
        mp.put("end", date1);
        List<Map<String, Object>> ls = sysError.getSysErrorNum(mp);
        List<Map<String, Object>> ss = new ArrayList<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        while (date0.before(date1)) {
            Map<String, Object> m = new HashMap<>();
            m.put("createTime", df.format(date1));
            ss.add(m);
            c.setTime(date1);
            c.add(Calendar.SECOND, -1);
            date1 = c.getTime();
        }
        ss.stream().forEach(o -> {
            o.put("num", 0);
            for (int i = 0; i < ls.size(); i++) {
                if ((o.get("createTime")).equals(ls.get(i).get("createTime"))) {
                    o.put("num", ls.get(i).get("num"));
                    break;
                }
            }
        });
        return R.ok().put("map", ss);
    }

    @RequestMapping("/getChangeOneInfo")
    public R getChangeOneInfo(@RequestParam Map<String, Object> mp) {
        Date date1 = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mp.put("start", date1);
        mp.put("end", date1);
        List<Map<String, Object>> ls = sysError.getSysErrorNum(mp);
        if (ls.size() == 0) {
            Map<String, Object> m = new HashMap<>();
            m.put("num", 0);
            m.put("createTime", df.format(date1));
            ls.add(m);
        }
        return R.ok().put("res", ls);
    }

}
