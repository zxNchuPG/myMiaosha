package com.nchu.miaosha.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @ClassName: QuartzScheduler
 * @Author: zxnchu
 * @Description:
 * @Date: 2020/9/19 14:55
 * @Version: 1.0
 */
public class QuartzScheduler {
    public static void main(String[] args) throws SchedulerException {
        // 创建一个JobDetail实例
        JobDetail jobDetail = JobBuilder.newJob(QuartzJobImpl.class)
                // 指定JobDetail的名称和组名称
                .withIdentity("job1", "group1")
                // 使用JobDataMap存储用户数据
                .usingJobData("message", "JobDetail传递的文本数据").build();

        // 创建一个SimpleTrigger，规定该Job立即执行，且两秒钟重复执行一次
        SimpleTrigger trigger = TriggerBuilder.newTrigger()
                // 设置立即执行，并指定Trigger名称和组名称
                .startNow().withIdentity("trigger1", "group1")
                // 使用JobDataMap存储用户数据
                .usingJobData("number", 128)
                // 设置运行规则，每隔两秒执行一次，一直重复下去
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(2).repeatForever()).build();

        // 得到Scheduler调度器实例
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.scheduleJob(jobDetail, trigger); // 绑定JobDetail和Trigger
        scheduler.start();                         // 开始任务调度
    }
}
