package ren.erdong.det.util;

import java.math.BigInteger;

public class StringUtil {

    // 字符串数组转化为整型数组
    public static int[] strsToNums(String[] strs) {
        int order = strs.length;
        int[] nums = new int[order];
        for (int i = 0; i < order; i++) {
            nums[i] = Integer.parseInt(strs[i]);
        }
        return nums;
    }

    // 一维数组转为二维数组
    public static BigInteger[][] one2twoDimen(int[] oneDimens) {
        int order = (int) Math.sqrt(oneDimens.length);

        // 判断下传入数组的长度是否为平方数
        if (order * order != oneDimens.length) {
            throw new IllegalArgumentException("请输入正确的数组!");
        }

        // 开始转化
        BigInteger[][] twoDimen = new BigInteger[order][order];
        for (int i = 0; i < order; i++) {
            for (int j = 0; j < order; j++) {
                twoDimen[i][j] = BigInteger.valueOf(oneDimens[order * i + j]);
            }
        }
        return twoDimen;
    }
}
