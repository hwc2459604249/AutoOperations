package utils;

import org.junit.Test;

import java.util.*;

public class Creat {
    public static int r = 70;
    public static Random random = new Random();
    public  static void setR(int r){
        Creat.r = r;
    }
    /*
     * 求最大公约数
     * */
    public static int gcd(int a, int b) {
        if (a < b) {
            a ^= b;
            b ^= a;
            a ^= b;
        }
        while (a % b != 0) {
            int t = a % b;
            a = b;
            b = t;
        }
        return b;

    }

    /*
     * 创建真分数,替换a
     * */
    public static String realSplitNum(String org, int aNum) {
        String temp;

        for (int i = 0; i < aNum; i++) {
            int t1 = random.nextInt(1, r);
            int t2 = random.nextInt(1, r);
            if (t1 < t2) {
                temp = t1 + "/" + t2;
            } else if (t1 > t2) {
                temp = t2 + "/" + t1;
            }else {
                return realSplitNum(org,aNum);
            }

            org = org.replaceFirst("a", temp);
        }
        return org;
    }

    /*
     * 创建自然数并替换b，返回用于显示的数据和便于计算的
     * */
    public static String creatNum(Queue<Integer> queue, String org) {

        while (!queue.isEmpty()) {
            Integer i = queue.poll();
            String value = String.valueOf(i);
            org = org.replaceFirst("b", value);
        }
        return org;
    }

    /*
     * 对一个小的二元运算表达式计算结果
     * 返回空表示存在负数
     * */
    public static String compute(String t) {

        String ans;
        String[] split = t.split(" ");
        String[] aSplit = split[0].split("/");
        String[] bSplit = split[2].split("/");
        int ma = 1, mb = 1, za, zb;
        za = Integer.parseInt(aSplit[0]);
        zb = Integer.parseInt(bSplit[0]);
        if (aSplit.length == 2) ma = Integer.parseInt(aSplit[1]);
        if (bSplit.length == 2) mb = Integer.parseInt(bSplit[1]);
        ans = switch (split[1]) {
            case "+" -> add(za, ma, zb, mb);
            case "-" -> sub(za, ma, zb, mb);
            case "×" -> mul(za, ma, zb, mb);
            case "÷" -> div(za, ma, zb, mb);
            default -> null;
        };
        return ans;
    }

    /*
     * 真分数加法
     * */
    public static String add(int za, int ma, int zb, int mb) {

        String z = String.valueOf(za * mb + zb * ma);
        String m = String.valueOf(ma * mb);
        if (m.equals("1")) return z;
        return z + "/" + m;
    }

    /*
     * 真分数减法
     * */
    public static String sub(int za, int ma, int zb, int mb) {
        int i = za * mb - zb * ma;
        if (i < 0) return null;
        String z = String.valueOf(i);
        String m = String.valueOf(ma * mb);
        if (m.equals("1")) return z;
        return z + "/" + m;
    }

    /*
     * 真分数乘法
     * */
    public static String mul(int za, int ma, int zb, int mb) {

        String z = String.valueOf(za * zb);
        String m = String.valueOf(ma * mb);
        if (m.equals("1")) return z;
        return z + "/" + m;
    }

    /*
     * 真分数除法
     * */
    public static String div(int za, int ma, int zb, int mb) {
        return mul(za, ma, mb, zb);
    }

    /*
     *   计算答案
     * */
    public static String count(String org) {
        String ans = org;
        int it = org.indexOf('(');

        if (it != -1) {//去括号化
            int j = org.indexOf(')');
            String substring = org.substring(it + 1, j);

            String compute = compute(substring);
            //存在负数
            if (compute == null) return null;
            ans = org.replace("(" + substring + ")", compute);

        }
        Stack<String> numStack = new Stack<>();
        Stack<Character> opeStack = new Stack<>();
        char[] charArray = ans.toCharArray();
        //获取数据
        String[] split = ans.split(" . ");
        int len = 0;
        numStack.push(split[len++]);
        for (int i = 0; i < charArray.length; i++) {
            if (charArray[i] == '+' || charArray[i] == '-' || charArray[i] == '÷' || charArray[i] == '×') {
                if (opeStack.isEmpty()) {//运算符为空
                    numStack.push(split[len++]);
                    opeStack.push(charArray[i]);
                } else {
                    char peek = opeStack.peek();
                    if ((peek == '+' || peek == '-') && (charArray[i] == '÷' || charArray[i] == '×')) {
                        //优先级更高，加入
                        opeStack.push(charArray[i]);
                        numStack.push(split[len++]);
                    } else {//当前运算符优先级小于栈首
                        String s = numStack.get(numStack.size() - 2) + " " + opeStack.pop() + " " + numStack.pop();
                        numStack.pop();
                        String compute = compute(s);
                        if (compute == null) return null;
                        numStack.push(compute);

                        i--;
                    }
                }
            }
        }
        while (!opeStack.isEmpty()) {
            String beCount = numStack.pop();
            String mainCount = numStack.pop();
            ans = compute(mainCount + " " + opeStack.pop() + " " + beCount);
            if(ans == null) return null;
            numStack.push(ans);
           // System.out.println("ans:    " + ans);
        }

        return ans;
    }

    @Test
    public void text() {

        System.out.println(count("2/5 ÷ 4 - 6/9 ÷ 4"));

    }



    public static boolean random() {
        int i = random.nextInt(1, 3);
        return i == 1;
    }

}
