package com.doublewinter0.application.controller;

import com.doublewinter0.application.service.ArrayTransService;
import com.doublewinter0.application.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigInteger;
import java.util.Arrays;

@RestController
public class Controller {

    private final ArrayTransService atService;

    @Autowired
    public Controller(ArrayTransService atService) {
        this.atService = atService;
    }

    @RequestMapping("/result")
    public ModelAndView result(Model model, @RequestParam(value = "element") String element) {


        // 前台传过来的矩阵字符串
        String[] oriStrs = element.split(";");
        String[] numeratorStrs = oriStrs[0].split(",");
        String[] denominatorStrs = oriStrs[1].split(",");
        BigInteger[][] numerator2s = StringUtil.one2twoDimen(StringUtil.strsToNums(numeratorStrs));
        BigInteger[][] denominator2s = StringUtil.one2twoDimen(StringUtil.strsToNums(denominatorStrs));

        long begin = System.currentTimeMillis();
        String result = compute(numerator2s, denominator2s);
        long interval = System.currentTimeMillis() - begin;
        System.out.println("耗时:" + interval + "毫秒");
        model.addAttribute("result", result);
        model.addAttribute("interval", interval);
        ModelAndView mv = new ModelAndView();
        mv.setViewName("./WEB-INF/jsp/result.jsp");
        return null;
    }

    // @RequestMapping("/compute")
    private String compute(BigInteger[][] numerator2s, BigInteger[][] denominator2s) {

        atService.arrayTrans(numerator2s, denominator2s);
        int plusMinusDivisor = (atService.getInvertedSequence() % 2 == 0) ? 1 : -1;
        BigInteger[] numeratorDiagonalEntries = atService.getNumeratorDiagonalEntries();
        BigInteger[] denominatorDiagonalEntries = atService.getDenominatorDiagonalEntries();
        System.out.println(Arrays.toString(numeratorDiagonalEntries));
        System.out.println(Arrays.toString(denominatorDiagonalEntries));
        BigInteger numerator = BigInteger.ONE;
        BigInteger denominator = BigInteger.ONE;

        int order = numerator2s.length;
        for (int i = 0; i < order; i++) {
            System.out.println("艹腻骂...");
            numerator = numerator.multiply(numeratorDiagonalEntries[i]);
            denominator = denominator.multiply(denominatorDiagonalEntries[i]);
            System.out.println("denominator = " + denominator);
        }
        if (numerator.equals(BigInteger.ZERO)) {
            return "0";
        }

        BigInteger gcd = numerator.gcd(denominator);
        numerator = numerator.divide(gcd);
        denominator = denominator.divide(gcd);

        if (numerator.multiply(denominator).multiply(BigInteger.valueOf(plusMinusDivisor)).compareTo(BigInteger.ZERO) > 0) {
            if (denominator.abs().equals(BigInteger.ONE)) {
                System.out.println("所求行列式值为:" + numerator.abs());
                return numerator.abs().toString();
            }
            else {
                System.out.println("所求行列式值为:" + numerator.abs() + " / " + denominator.abs());
                return numerator.abs() + " / " + denominator.abs();
            }
        } else {
            if (denominator.abs().equals(BigInteger.ONE)) {
                System.out.println("所求行列式值为:" + "-" + numerator.abs());
                return "-" + numerator.abs();
            }
            else {
                System.out.println("所求行列式值为:" + "-" + numerator.abs() + " / " + denominator.abs());
                return "-" + numerator.abs() + " / " + denominator.abs();
            }
        }
    }

}
