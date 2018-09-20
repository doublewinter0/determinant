package com.trasepi.det.service;

import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class ArrayTransService {

    private int invertedSequence = 0;
    private BigInteger[] numeratorDiagonalEntries;
    private BigInteger[] denominatorDiagonalEntries;

    public int getInvertedSequence() {
        return invertedSequence;
    }

    public BigInteger[] getNumeratorDiagonalEntries() {
        return numeratorDiagonalEntries;
    }

    public BigInteger[] getDenominatorDiagonalEntries() {
        return denominatorDiagonalEntries;
    }

    public void arrayTrans(BigInteger[][] numeratorMatrix2s, BigInteger[][] denominatorMatrix2s) {
        int order = numeratorMatrix2s.length;
        int[] rowIndex = new int[order];
        numeratorDiagonalEntries = new BigInteger[order];
        denominatorDiagonalEntries = new BigInteger[order];

        // 接下来的 4 阶 for 嵌套我没有注释, 因为注释起来很麻烦!
        // ******核心代码开始******//
        for (int t = 0; t < order; t++) {
            for (int s = 0; s < order; s++) {
                if (numeratorMatrix2s[s][t].equals(BigInteger.ZERO))
                    continue;
                rowIndex[t] = s + 1;
                numeratorDiagonalEntries[t] = numeratorMatrix2s[s][t];
                denominatorDiagonalEntries[t] = denominatorMatrix2s[s][t];
                if (t == order - 1) {
                    break;
                }
                for (int i = 0; i < order; i++) {
                    if (i == s) {
                        continue;
                    }
                    BigInteger tempData1 = numeratorMatrix2s[i][t];
                    BigInteger tempData2 = denominatorMatrix2s[i][t];
                    for (int j = 0; j < order; j++) {
                        numeratorMatrix2s[i][j] = (numeratorMatrix2s[i][j].multiply(numeratorMatrix2s[s][t]).multiply(denominatorMatrix2s[s][j]).multiply(tempData2))
                                .subtract(tempData1.multiply(numeratorMatrix2s[s][j]).multiply(denominatorMatrix2s[i][j]).multiply(denominatorMatrix2s[s][t]));
                        denominatorMatrix2s[i][j] = numeratorMatrix2s[s][t].multiply(denominatorMatrix2s[i][j]).multiply(denominatorMatrix2s[s][j]).multiply(tempData2);

                        // System.out.println(numeratorMatrix2s[i][j]); 测试代码, 不要删除
                        // System.out.println(denominatorMatrix2s[i][j]); 测试代码, 不要删除

                        // 获取 numeratorMatrix2s[i][j] denominatorMatrix2s[i][j] 的最大公约数
                        BigInteger gcd = numeratorMatrix2s[i][j].gcd(denominatorMatrix2s[i][j]);
                        numeratorMatrix2s[i][j] = numeratorMatrix2s[i][j].divide(gcd);
                        denominatorMatrix2s[i][j] = denominatorMatrix2s[i][j].divide(gcd);
                    }
                }
                for (int r = 0; r < order; r++) {
                    numeratorMatrix2s[s][r] = BigInteger.ZERO;
                }
                break;
            }
        }
        // ****** 核心代码结束 ******//

        for (int i = 1; i < rowIndex.length; i++) {
            for (int j = 0; j < i; j++) {
                if (rowIndex[j] > rowIndex[i])
                    invertedSequence = invertedSequence + 1;
            }
        }
    }
}
