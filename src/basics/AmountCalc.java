package basics;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AmountCalc {
    public static void main(String[] args) {
        // 浮点数计算示例 , 浮点数计算存在精度问题，建议使用BigDecimal进行高精度计算
        BigDecimal a = new BigDecimal("0.111");
        BigDecimal b = new BigDecimal("0.222");

        System.out.println("a + b = " + a.add(b)); // 0.333

        var c = a.add(b);
        System.out.println(c.setScale(2, RoundingMode.HALF_UP)); // 0.33 四舍五入保留两位小数
    }
}
