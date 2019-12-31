package com.monkey.core;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.monkey.dao.SysErrorService;
import com.monkey.dao.UserInfoService;
import com.monkey.entity.SysError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Administrator
 * @title: Inint
 * @projectName monkey2
 * @description: TODO
 * @date 2019/11/410:45
 */
@Component
public class Inint {

    private static Logger logger = LoggerFactory.getLogger(Inint.class);

    @Autowired
    SysErrorService sysError;

    @Autowired
    UserInfoService userInfoService;

    @Scheduled(cron = "0 0 0 0/8 * ?")
    public void one() {
        logger.debug("开始s删除日志！");
        Calendar c = new GregorianCalendar();
        Date date1 = new Date();
        c.setTime(date1);//设置参数时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        c.add(Calendar.HOUR, -192);//把日期往后增加SECOND 秒.整数往后推,负数往前移动
        Date date0 = c.getTime(); //这个时间就是日期往后推一天的结果
        QueryWrapper<SysError> s = new QueryWrapper<>();
        s.apply("date_format(create_time,'%Y-%m-%d  %H:%i:%s')<{0}", date0);
        sysError.delete(s);
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    public void two() {
        List<Map<String, Object>> mp = sysError.findInfo("0");
        List<Map<String, Object>> mid = userInfoService.getInfoAll();
        mid.stream().forEach(o -> {
            List<String> ss = Arrays.asList(o.get("content").toString().split(";"));
            o.put("list", ss);
        });
        mp.stream().forEach(v -> {
            mid.stream().forEach(o -> {
                ((List<String>) o.get("list")).stream().forEach(i -> {
                    if (v.get("content").toString().indexOf(i)>-1) {
                        HttpRequest.sendPost(MDK.URL + "/init/sendMsg", "openId=" + o.get("openId") + "&content=" + v.get("name") + " uuid=" + v.get("uuid") + " 错误：" + v.get("content"));
                    }
                });



            });
            sysError.updateSysError(v.get("id")+"");
        });
    }


    @Scheduled(cron = "0/10 * * * * ?")
    public void third() {
        Date d = new Date();
        String s =new SimpleDateFormat("HH:mm:ss").format(d);
        logger.info("开始测试！"+s);
        HttpRequest.sendPost("http://gepan8.cn/mk/init/test","");
        logger.info("结束测试！"+s);
    }
}
