package ren.erdong.det.util;

// 该工具类没有用 因为使用了 BigInteger 这个类
public class GreatestCommonDivisor {

    /**
     * 求最大公约数是小学数学的内容, 说实话我也没有过多的研究过这个东西.
     * 直到我发现我之前所写的行列式代码中所出现的问题: 求两个数的最大公约数的算法太过垃圾, 导致程序卡住了...
     * 接下来的事情便是寻找好的求最大公约数的算法. 不过遗憾的是, 本人过于愚笨, 思考几天无果, 只能向大师们请教.
     * 这一请教, 对我的打击很大, 因为算法来源于伟大的数学家 Euclid 的作品. 一个看起来很简单的原理, 但我就是没有想到.
     * 当然我也知道, 数学上有些看起来简单而一般人难以想到的原理往往蕴含着深刻的思想.
     * 这个 Euclid 算法所蕴含的思想应用到了数论中的一些定理.
     * 作为后辈, 我想多读读这些大师们的作品, 会让我们受益无穷! 最后, 向大师们致敬! 感谢你们, 让我们看到了更大的世界!
     *
     * @param x para 1
     * @param y para 2
     * @return GCD
     * @throws ArithmeticException 算术异常
     */
    public static long getGCD(long x, long y) throws ArithmeticException {
        long xx = Math.abs(x);
        long yy = Math.abs(y);

        if (xx + yy == 0) {
            throw new ArithmeticException("两个数都为0, 求最大公约数似乎没有什么意义, 你说呢?");
        }

        long minor = (xx <= yy) ? xx : yy;
        long major = xx + yy - minor;
        if (minor == 0) {
            return xx + yy;
        }

        // 利用辗转相除法求最大公约数
        // 向 Euclid 大师致敬!
        long remainder;
        while ((remainder = major % minor) != 0) {
            major = minor;
            minor = remainder;
            // remainder = major % minor;
        }
        return minor;
    }
}
