//package com.xiabuxiabu.storemanage.Thread;
//
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.concurrent.Future;
//
//@Component
//@EnableScheduling
//public class MailTask {
//    @Scheduled(cron = "0 0/2 * * * ? ")
//    public void run() throws Exception{
//        Task task = new Task();
//        long start = System.currentTimeMillis();
//        Future<String> task1 = task.doTaskOne();
//        Future<String> task2 = task.doTaskTwo();
//
//        while(true) {
//            if(task1.isDone() && task2.isDone()) {
//                // 三个任务都调用完成，退出循环等待
//                break;
//            }
//            Thread.sleep(10000);
//        }
//        long end = System.currentTimeMillis();
//        System.out.println("任务全部完成，总耗时：" + (end - start) + "毫秒");
//    }
//
//}
