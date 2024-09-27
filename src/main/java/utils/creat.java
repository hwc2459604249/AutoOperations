package utils;

import org.junit.Test;

import java.util.*;

public class creat {
    private static int r = 70;
    static Random random = new Random();

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
        String temp = null;

        for (int i = 0; i < aNum; i++) {
            int t1 = random.nextInt(1, r);
            int t2 = random.nextInt(1, r);
            if (t1 <= t2) {
                temp = t1 + "/" + t2;
            } else {
                temp = t2 + "/" + t1;
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

        String ans = null;
        String[] split = t.split(" ");
        String[] aSplit = split[0].split("/");
        String[] bSplit = split[2].split("/");
        int ma = 1, mb = 1, za, zb;
        za = Integer.parseInt(aSplit[0]);
        zb = Integer.parseInt(bSplit[0]);
        if (aSplit.length == 2) ma = Integer.parseInt(aSplit[1]);
        if (bSplit.length == 2) mb = Integer.parseInt(bSplit[1]);
        switch (split[1]) {
            case "+":
                ans = add(za, ma, zb, mb);
                break;
            case "-":
                ans = sub(za, ma, zb, mb);
                break;
            case "×":
                ans = mul(za, ma, zb, mb);
                break;
            case "÷":
                ans = div(za, ma, zb, mb);
                break;
        }
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
                        numStack.push(split[numStack.size()]);
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
        if (!opeStack.isEmpty()) {
            String beCount = numStack.pop();
            String mainCount = numStack.pop();
            ans = compute(mainCount + " " + opeStack.pop() + " " + beCount);
            if(ans == null) return null;
            System.out.println("ans:    " + ans);
        }

        return ans;
    }

    @Test
    public void text() {

        count("29 - 26/31 - 67 - 45");
        // System.out.println("a+b".indexOf('('));
        // System.out.println("56/78".split("/")[1]);
    }

    public static void main(String[] args) {
        //运算符个数
        int n = random.nextInt(1, 4);
        System.out.println(n);
        //数据个数
        int m = n + 1;
        int simple = random.nextInt(1, 3);
        //用于判断是否加括号
        char[] product = new char[4 * n + 1];
        int aNum = 0, bNum = 0;
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < product.length; i++) {
            if (i % 4 == 0) {
                if (random()) {//给真分数占位
                    product[i] = 'a';
                    aNum++;
                } else {

                    int num = random.nextInt(1, r);

                    queue.add(num);
                    product[i] = 'b';
                    bNum++;

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
        System.out.println("a:" + aNum + ",b:" + bNum + "\nc:" + new String(product));
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
        obj = realSplitNum(obj, aNum);
        System.out.println(obj);

        //替换b
        obj = creatNum(queue, obj);
        System.out.println("org:    " + obj);
        String count = count(obj);
        if(count == null)return;
        count="2352520/9568";
        String[] split1 = count.split("/");
        if (split1.length == 2) {
            int a = Integer.parseInt(split1[0]);
            int b = Integer.parseInt(split1[1]);
            if (a > b) {//带分数
                int d = a / b;
                //分子
                int y = a % b;
                if(y!=0){
                    int gcd = gcd(y, b);
                    y /= gcd;
                    b /= gcd;
                    String ans = d + "\\'" + y + "/" + b;
                    System.out.println("ans1:"+ans);
                }

            }else{
                int gcd = gcd(a, b);
                a /= gcd;
                b /= gcd;
                String ans =  a + "/" + b;
                System.out.println("ans2"+ans);
            }
        }

    }

    public static boolean random() {
        int i = random.nextInt(1, 3);
        if (i == 1) return true;
        else return false;
    }

}
