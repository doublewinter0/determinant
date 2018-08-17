package ren.erdong.det.controller;

import org.springframework.web.bind.annotation.*;
import ren.erdong.det.service.ArrayTransService;
import ren.erdong.det.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

// @RestController 这两个有什么不同?
@Controller
public class IController {

    private final ArrayTransService atService;

    @Autowired
    public IController(ArrayTransService atService) {
        this.atService = atService;
    }

    @GetMapping(value = "/")
    public String index() throws IOException {
        // TODO 关于 Java 路径的困惑
        File file = new File(".");
        System.out.println("Path = " + file.getPath());
        System.out.println("absPath = " + file.getAbsolutePath());
        System.out.println("canonicalPath = " + file.getCanonicalPath());
        System.out.println(IController.class.getResource("."));
        System.out.println(IController.class.getResource("/"));
        return "../static/index";
    }

    // @RequestMapping(value = "/result", method = RequestMethod.POST)
    @PostMapping(value = "/result")
    public String result(Model model, @RequestParam(value = "element") String element) {

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
        return "result";
    }

    private String compute(BigInteger[][] numerator2s, BigInteger[][] denominator2s) {

        atService.arrayTrans(numerator2s, denominator2s);
        int plusMinusDivisor = (atService.getInvertedSequence() % 2 == 0) ? 1 : -1;
        BigInteger[] numeratorDiagonalEntries = atService.getNumeratorDiagonalEntries();
        BigInteger[] denominatorDiagonalEntries = atService.getDenominatorDiagonalEntries();
        BigInteger numerator = BigInteger.ONE;
        BigInteger denominator = BigInteger.ONE;

        int order = numerator2s.length;
        for (int i = 0; i < order; i++) {
            numerator = numerator.multiply(numeratorDiagonalEntries[i]);
            denominator = denominator.multiply(denominatorDiagonalEntries[i]);
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
            } else {
                System.out.println("所求行列式值为:" + numerator.abs() + " / " + denominator.abs());
                return numerator.abs() + " / " + denominator.abs();
            }
        } else {
            if (denominator.abs().equals(BigInteger.ONE)) {
                System.out.println("所求行列式值为:" + "-" + numerator.abs());
                return "-" + numerator.abs();
            } else {
                System.out.println("所求行列式值为:" + "-" + numerator.abs() + " / " + denominator.abs());
                return "-" + numerator.abs() + " / " + denominator.abs();
            }
        }
    }
}
