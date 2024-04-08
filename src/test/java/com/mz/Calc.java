package com.mz;

import org.apache.commons.lang3.StringUtils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Calc {
    public static Integer calc(Integer para) {
        try {
            //模拟一个长时间的执行
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return para * para;
    }

    public static void main(String[] args) throws Exception {
//        final CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> calc(50)).thenApply((i) -> Integer.toString(i)).thenApply((str) -> "\"" + str + "\"").thenAccept(System.out::println);
//        future.get();

        String fileName = "C:/Users/yangh/Desktop/yh1.txt";
        Stream<String> lines = Files.lines(Paths.get(fileName));

        // 随机行顺序进行数据处理
//        lines.forEach(ele -> {
//            if(ele.indexOf("镇") > -1){
//                String[] split = ele.split("镇");
//                System.out.println(split[1]);
//            } else if(ele.indexOf("街道") > -1){
//                String[] split = ele.split("街道");
//                System.out.println(split[1]);
//            }else if(ele.indexOf("乡") > -1){
//                String[] split = ele.split("乡");
//                System.out.println(split[1]);
//            } else if(ele.indexOf("管委会") > -1){
//                String[] split = ele.split("管委会");
//                System.out.println(split[1]);
//            }else {
//                System.out.println(ele);
//            }
//        });


        String hy = "";

        Object[] objects = lines.toArray();
        for (Object obj : objects) {
            if(StringUtils.isNoneBlank(hy)){
                hy += ",";
            }
            hy += "'" + obj + "'" ;
        }
        System.out.println(hy);
    }
}