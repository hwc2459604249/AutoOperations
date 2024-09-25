package utils;

import java.util.Random;

public class creat {
    public static void main(String[] args) {
        Random random = new Random();
        int n = random.nextInt(1, 4);
        System.out.println(n);
        int m = n + 1;
        //3 4 length:4n+1没有（）
        //1 + 2
        //012345678
        //1 + 2 + c
        // m=n+1
        //" + "
        //012345678
        // 3 4 7 9
        //ab
        //有括号 +2
        //随机决定需不需要括号
        int r = 90;
        int simple = random.nextInt(1, 3);
        if (simple <= 2) {//无括号
            char[] product = new char[4 * n + 1];
            int aNum = 0,bNum = 0;
            for (int i = 0; i < product.length; i++) {
                if (i % 4 == 0) {
                    if (random()) {//给真分数占位
                        product[i] = 'a';
                        aNum++;
                    } else {
                        //小于10的数
                        int num = random.nextInt(1, r);
                        if(num<10){
                            product[i] = (char) ('0'+num);
                        }else{
                            //一个字符无法表示多位数
                            product[i] = 'b';
                            bNum++;
                        }
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
            System.out.println("a:"+aNum+",b:"+bNum+"\nc:"+new String(product));
            String s = new String(product);
            String replace = s.replaceFirst("a", "1/2");
            for (int i=0;i<aNum;i++){

            }
            for (int i=0;i<bNum;i++){

            }
            System.out.println(replace);
        } else {

        }
        /*
         * 随机生成n个运算符
         * */
        for (int i = 1; i <= n; i++) {

        }
        //随机生成m个数据
    }
    public static boolean random() {
        int i = new Random().nextInt(1, 3);
        if (i == 1) return true;
        else return false;
    }

}
