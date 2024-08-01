package org.example;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.lang.UUID;

import java.io.File;
import java.util.Vector;

/**
 * @author eliauk
 * @since 2023-09-02 12:48
 */
public class Main {

    public static void main(String[] args) {
        try {
            new MemConsume(4).start();
            new CpuConsume().start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 占比 80%
        new SsdConsume(0.8).start();
    }
}


/**
 * 内存占用
 *
 * @author eliauk
 * @since 2023/9/2 13:19
 */
class MemConsume extends Thread {
    private final int memory;

    public MemConsume(int m) {
        memory = m;
    }

    @Override
    public void run() {
        int num = 1;
        for (int i = 0; i < num * 10 * memory; i++) {
            Vector v = new Vector();
            byte[] b1 = new byte[104857600]; // 100M
            v.add(b1);
//            Runtime rt = Runtime.getRuntime();
//            System.out.println("free memory" + rt.freeMemory());
            System.out.println(i);
        }
        while (true) {
            try {
                Thread.sleep(36000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

/**
 * cpu占用
 *
 * @author eliauk
 * @since 2023/9/2 13:19
 */
class CpuConsume extends Thread {
    //cpu消耗方法
    @Override
    public void run() {
        // 角度的分割
        final double split = 0.01;
        //
        // 2PI分割的次数，也就是2/0.01个，正好是一周
        final int count = (int) (2 / split);
        final double pi = Math.PI;
        // 时间间隔
        final int INTERVAL = 200;
        long[] busySpan = new long[count];
        long[] idleSpan = new long[count];
        int half = INTERVAL / 2;
        double radian = 0.0;
        for (int i = 0; i < count; i++) {
            busySpan[i] = (long) (half + (Math.sin(pi * radian) * half));
            idleSpan[i] = INTERVAL - busySpan[i];
            radian += split;
        }
        long startTime;
        int j = 0;
        while (true) {
            j = j % count;
            startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < busySpan[j]) {

            }
            try {
//                System.out.println(idleSpan[j]);
                //这里的if控制可以注解掉，让Thread.sleep(idleSpan[j])一直执行。
                //我这里加了if控制是因为希望Cpu一直保存在70%以上工作的效果(小于70不sleep)，If注解掉将以正弦曲线的趋势使用Cpu
                if (idleSpan[j] < 70) {
                    Thread.sleep(idleSpan[j]);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            j++;
        }
    }
}

/**
 * 从盘占用
 *
 * @author eliauk
 * @since 2023/9/2 13:19
 */
class SsdConsume extends Thread {
    private final double rate;

    public SsdConsume(double r) {
        rate = r;
    }
    @Override
    public void run() {
        File file;
        Long total;
        Long free;
        int i = 0;
        String str = "";
        while (i < 15000) {
            str += "How do you know what is the right direction\n\nUltimately it comes down to t" +
                    "aste\n\nIt comes down to taste\n\nIt is a matter of trying to expose yourself to t" +
                    "he best things that humans have done\n\nAnd then try to bring those things into wh" +
                    "at you are doing .\n\nPicasso had a saying : \" Good artists copy ,great artists s" +
                    "teal\"\n\nWe have always been shameless about stealing great ideas.\r\nAs a magicia" +
                    "n , I try to create images that make people stop and think\n\nI also try to challe" +
                    "nge myself to do things that doctors say are not possible\n\nAs a magician ,I try t" +
                    "o show things to people that seem impossible\n\nAnd I think magic ,whether I am hol" +
                    "ding my breath or shuffling a deck of cards, is pretty simple\n\nIt's practice, it'" +
                    "s training.\n\nAnd it' s practice, it' s training and experimenting ,while pushin" +
                    "g through the pain to be the best that I can be\n\nAnd that's what magic is to m" +
                    "e, so ,thank you. (Applause)\r\n";
            i++;
            System.out.println(i);
        }
        boolean flg = true;
        while (flg) {
            String uuid = UUID.randomUUID().toString();
            String path = "/data/temp/" + uuid + ".txt";
            if (!FileUtil.exist(path)) {
                FileWriter writer = FileWriter.create(FileUtil.file(path));
                writer.write(str);
            }
            try {
                file = new File("/data");
                total = file.getTotalSpace();
                free = file.getFreeSpace();
                if (total * rate <= (total - free)) {
                    flg = false;
                }
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}


