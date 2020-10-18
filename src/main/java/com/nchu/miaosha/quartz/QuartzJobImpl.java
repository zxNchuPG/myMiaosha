package com.nchu.miaosha.quartz;

import com.nchu.miaosha.materials.service.GoodsService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ClassName: QuartzJobImpl
 * @Author: zxnchu
 * @Description: job实现类
 * @Date: 2020/9/19 10:56
 * @Version: 1.0
 */
public class QuartzJobImpl implements Job {
    private String message;
    private Integer number;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // 业务逻辑代码
        System.out.println(message);
        System.out.println(number);
    }
}
