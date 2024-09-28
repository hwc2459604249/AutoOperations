package main;

import org.junit.Test;
import utils.Creat;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringJoiner;

import static utils.Creat.count;
import static utils.Creat.random;

public class MyMain {
    public static void main(String[] args) throws IOException {
        int num = 1;
        String ans = null;
        String obj = null;
        int[] tag = {0, 0};
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-n" -> num = Integer.parseInt(args[i + 1]);
                case "-r" -> {
                    int r = Integer.parseInt(args[i + 1]);
                    Creat.setR(r);
                }
                case "-e" -> {
                    obj = args[i + 1];
                    tag[0] = 1;
                }
                case "-a" -> {
                    ans = args[i + 1];
                    tag[1] = 1;
                }
            }
        }

        //生成题目及答案
        try (
                FileOutputStream eOutputStream = new FileOutputStream("src/main/resources/Exercises.txt");
                FileOutputStream aOutputStream = new FileOutputStream("src/main/resources/Answers.txt")

        ) {

            for (int i = 0; i < num; i++) {
                String[] result = get();
                eOutputStream.write((i + 1 + ". " + result[0] + "\n").getBytes());
                aOutputStream.write((i + 1 + ". " + result[1] + "\n").getBytes());
            }
        }
        //判断给定题目
        if (tag[0] == 1 && tag[1] == 1) {
            try (
                    BufferedReader objFile = new BufferedReader(new FileReader(obj));
                    BufferedReader ansFile = new BufferedReader(new FileReader(ans));
                    BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/Grade.txt"))
            ) {
                ArrayList<Integer> right = new ArrayList<>();
                ArrayList<Integer> error = new ArrayList<>();
                while (objFile.ready()) {
                    String objLine = objFile.readLine();
                    String ansLine = ansFile.readLine();
                    String[] split1 = objLine.split("\\. ");
                    String[] split2 = ansLine.split("\\. ");
                    if (pd(split1[1], split2[1])) {
                        right.add(Integer.parseInt(split2[0]));
                    } else error.add(Integer.parseInt(split2[0]));
                }

                String r = "Correct:" + right.size();
                StringJoiner joiner = new StringJoiner(",", "(", ")");
                for (Integer i : right) {
                    joiner.add(String.valueOf(i));
                }
                System.out.println(joiner);
                writer.write(r + joiner);
                writer.newLine();
                StringJoiner joiner1 = new StringJoiner(",", "(", ")");
                String l = "Wrong:" + error.size();
                for (Integer i : error) {
                    joiner1.add(String.valueOf(i));
                }
                writer.write(l + joiner1);
            }
        }

    }

    public static String[] get() {

        String ans;
        //运算符个数
        int n = random.nextInt(1, 4);
        // System.out.println(n);
        //数据个数

        int simple = random.nextInt(1, 3);
        //用于判断是否加括号
        char[] product = new char[4 * n + 1];
        int aNum = 0;

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < product.length; i++) {
            if (i % 4 == 0) {
                if (random()) {//给真分数占位
                    product[i] = 'a';
                    aNum++;
                } else {

                    int num = random.nextInt(1, Creat.r);

                    queue.add(num);
                    product[i] = 'b';


                }
            } else if ((i + 2) % 4 == 0) {
                //随机生成运算符
                int nextInt = random.nextInt(1, 5);
                switch (nextInt) {
                    case 1:
                        product[i] = '+';

                        break;
                    case 2:
                        product[i] = '-';

                        break;
                    case 3:
                        product[i] = '×';

                        break;
                    case 4:
                        product[i] = '÷';

                        break;
                }
            } else {
                product[i] = ' ';
            }
        }
        // System.out.println("a:" + aNum + ",b:" + bNum + "\nc:" + new String(product));
        String obj = new String(product);

        //随机决定在某个数据前添加左括号
        if (simple <= 1) {
            String[] split = obj.split(" [+-×÷] ");
            int t = random.nextInt(0, n);
            int index = obj.indexOf(split[t]);
            StringBuilder inBer = new StringBuilder(obj);
            inBer.insert(index, '(');
            inBer.insert(index + 6, ')');
            obj = inBer.toString();
        }
        //随机生成真分数
        obj = Creat.realSplitNum(obj, aNum);
        // System.out.println(obj);

        //替换b
        obj = Creat.creatNum(queue, obj);
        System.out.println("org: " + obj);
        try {
            ans = Creat.count(obj);
        } catch (Exception e) {
            return get();
        }
        if (ans == null) return get();
        System.out.println("count: " + ans);
        String[] split1 = ans.split("/");

        if (split1.length == 2) {
            int a = Integer.parseInt(split1[0]);
            int b = Integer.parseInt(split1[1]);
            if (a > b) {//带分数
                int d;
                try {
                    d = a / b;
                } catch (Exception e) {
                    return get();
                }
                //分子
                int y = a % b;
                if (y != 0) {
                    int gcd = Creat.gcd(y, b);
                    y /= gcd;
                    b /= gcd;
                    ans = d + "’" + y + "/" + b;

                } else ans = d + "";

            } else if (a < b) {
                if (a == 0) {
                    ans = "0";
                } else {
                    int gcd = Creat.gcd(a, b);
                    a /= gcd;
                    b /= gcd;
                    ans = a + "/" + b;
                }


            } else ans = "1";

        }

        return new String[]{obj, ans};
    }

    public static boolean pd(String obj, String ans) {
        String count = count(obj);
        String[] split = count.split("/");
        int i = Integer.parseInt(split[0]);
        int j = 1;
        if (split.length == 2) j = Integer.parseInt(split[1]);
        double temp = i * 1.0 / j;
        if (ans.contains("’")) {
            String[] split1 = ans.split("’");
            int a = Integer.parseInt(split1[0]);
            String[] split2 = split1[1].split("/");
            int b = Integer.parseInt(split2[0]);
            int c = Integer.parseInt(split2[1]);
            return (a + b * 1.0 / c) - temp < 0.01;
        } else if (ans.contains("/")) {
            String[] split1 = ans.split("/");
            int a = Integer.parseInt(split1[0]);
            int b = Integer.parseInt(split1[1]);
            return (a * 1.0 / b - temp) < 0.01;
        }
        return Integer.parseInt(ans) - temp < 0.01;
    }

    @Test
    public void test() {
        try (
                BufferedReader objFile = new BufferedReader(new FileReader("src\\main\\resources\\Exercises.txt"));
                BufferedReader ansFile = new BufferedReader(new FileReader("src\\main\\resources\\Answers.txt"))
        ) {
            String objLine = objFile.readLine();
            String ansLine = ansFile.readLine();
            String[] split = objLine.split("[0-9]\\. ");
            System.out.println(count(split[1]));
            System.out.println(split.length);

            for (String s : split) {
                System.out.println(s);
            }

            System.out.println(objLine);
            System.out.println(ansLine);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
