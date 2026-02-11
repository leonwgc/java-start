package advanced;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Java 8日期时间API学习
 * 学习目标：
 * 1. 掌握LocalDate、LocalTime、LocalDateTime的使用
 * 2. 学习日期时间的格式化和解析
 * 3. 理解时区处理
 * 4. 为Spring中的日期处理做准备
 */
public class DateTimeDemo {
    public static void main(String[] args) {
        System.out.println("=== Java 8日期时间API学习 ===\n");

        // 示例1：LocalDate - 日期操作
        demonstrateLocalDate();

        // 示例2：LocalTime - 时间操作
        demonstrateLocalTime();

        // 示例3：LocalDateTime - 日期时间操作
        demonstrateLocalDateTime();

        // 示例4：日期格式化
        demonstrateDateFormatting();

        // 示例5：日期计算
        demonstrateDateCalculation();

        // 示例6：时区处理
        demonstrateTimeZone();
    }

    // LocalDate示例
    public static void demonstrateLocalDate() {
        System.out.println("1. LocalDate - 日期操作\n");
        System.out.println("作用：表示日期（年月日），不包含时间");
        System.out.println("Spring应用：用于处理生日、订单日期等\n");

        // 获取当前日期
        LocalDate today = LocalDate.now();
        System.out.println("今天: " + today);

        // 创建指定日期
        LocalDate birthday = LocalDate.of(1990, 5, 15);
        System.out.println("生日: " + birthday);

        // 日期操作
        LocalDate tomorrow = today.plusDays(1);
        LocalDate nextWeek = today.plusWeeks(1);
        LocalDate nextMonth = today.plusMonths(1);
        LocalDate nextYear = today.plusYears(1);

        System.out.println("明天: " + tomorrow);
        System.out.println("下周: " + nextWeek);
        System.out.println("下月: " + nextMonth);
        System.out.println("明年: " + nextYear);

        // 日期比较
        System.out.println("今天在生日之后吗? " + today.isAfter(birthday));
        System.out.println("今天是闰年吗? " + today.isLeapYear());
        System.out.println();
    }

    // LocalTime示例
    public static void demonstrateLocalTime() {
        System.out.println("2. LocalTime - 时间操作\n");
        System.out.println("作用：表示时间（时分秒），不包含日期");
        System.out.println("Spring应用：用于处理营业时间、预约时间等\n");

        // 获取当前时间
        LocalTime now = LocalTime.now();
        System.out.println("现在: " + now);

        // 创建指定时间
        LocalTime lunchTime = LocalTime.of(12, 30);
        System.out.println("午餐时间: " + lunchTime);

        // 时间操作
        LocalTime oneHourLater = now.plusHours(1);
        LocalTime tenMinutesLater = now.plusMinutes(10);
        System.out.println("一小时后: " + oneHourLater);
        System.out.println("十分钟后: " + tenMinutesLater);

        // 时间比较
        System.out.println("现在已过午餐时间吗? " + now.isAfter(lunchTime));
        System.out.println();
    }

    // LocalDateTime示例
    public static void demonstrateLocalDateTime() {
        System.out.println("3. LocalDateTime - 日期时间操作\n");
        System.out.println("作用：表示日期和时间");
        System.out.println("Spring应用：用于记录创建时间、更新时间等\n");

        // 获取当前日期时间
        LocalDateTime now = LocalDateTime.now();
        System.out.println("现在: " + now);

        // 创建指定日期时间
        LocalDateTime meeting = LocalDateTime.of(2024, 3, 15, 14, 30);
        System.out.println("会议时间: " + meeting);

        // 日期时间操作
        LocalDateTime nextMeeting = meeting.plusWeeks(1);
        System.out.println("下次会议: " + nextMeeting);

        // 提取日期和时间部分
        LocalDate date = now.toLocalDate();
        LocalTime time = now.toLocalTime();
        System.out.println("日期部分: " + date);
        System.out.println("时间部分: " + time);
        System.out.println();
    }

    // 日期格式化
    public static void demonstrateDateFormatting() {
        System.out.println("4. 日期格式化\n");
        System.out.println("作用：将日期转换为字符串，或将字符串解析为日期");
        System.out.println("Spring应用：API返回日期格式、接收前端日期输入\n");

        LocalDateTime now = LocalDateTime.now();

        // 使用预定义格式
        System.out.println("ISO格式: " + now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        // 使用自定义格式
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("自定义格式1: " + now.format(formatter1));

        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm");
        System.out.println("自定义格式2: " + now.format(formatter2));

        // 解析字符串为日期
        String dateStr = "2024-03-15 14:30:00";
        LocalDateTime parsed = LocalDateTime.parse(dateStr, formatter1);
        System.out.println("解析字符串: " + parsed);
        System.out.println();
    }

    // 日期计算
    public static void demonstrateDateCalculation() {
        System.out.println("5. 日期计算\n");
        System.out.println("作用：计算日期之间的差值");
        System.out.println("Spring应用：计算会员有效期、计算账单周期等\n");

        LocalDate today = LocalDate.now();
        LocalDate birthday = LocalDate.of(1990, 5, 15);

        // 计算年龄
        long years = ChronoUnit.YEARS.between(birthday, today);
        System.out.println("年龄: " + years + "岁");

        // 计算天数差
        LocalDate vacation = LocalDate.of(2024, 12, 25);
        long daysUntilVacation = ChronoUnit.DAYS.between(today, vacation);
        System.out.println("距离假期还有: " + daysUntilVacation + "天");

        // Period - 日期间隔
        Period period = Period.between(birthday, today);
        System.out.println("出生至今: " + period.getYears() + "年"
                         + period.getMonths() + "个月"
                         + period.getDays() + "天");

        // Duration - 时间间隔
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(17, 30);
        Duration duration = Duration.between(start, end);
        System.out.println("工作时长: " + duration.toHours() + "小时"
                         + duration.toMinutesPart() + "分钟");
        System.out.println();
    }

    // 时区处理
    public static void demonstrateTimeZone() {
        System.out.println("6. 时区处理\n");
        System.out.println("作用：处理不同时区的时间");
        System.out.println("Spring应用：国际化应用、跨时区数据同步\n");

        // 当前时区的日期时间
        ZonedDateTime now = ZonedDateTime.now();
        System.out.println("当前时区: " + now);

        // 不同时区的时间
        ZonedDateTime tokyo = ZonedDateTime.now(ZoneId.of("Asia/Tokyo"));
        ZonedDateTime newYork = ZonedDateTime.now(ZoneId.of("America/New_York"));
        ZonedDateTime london = ZonedDateTime.now(ZoneId.of("Europe/London"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
        System.out.println("东京时间: " + tokyo.format(formatter));
        System.out.println("纽约时间: " + newYork.format(formatter));
        System.out.println("伦敦时间: " + london.format(formatter));

        // 时区转换
        ZonedDateTime shanghaiTime = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
        ZonedDateTime toTokyoTime = shanghaiTime.withZoneSameInstant(ZoneId.of("Asia/Tokyo"));
        System.out.println("\n上海时间: " + shanghaiTime.format(formatter));
        System.out.println("转换为东京时间: " + toTokyoTime.format(formatter));

        System.out.println("\n💡 提示：");
        System.out.println("- LocalDate/LocalTime/LocalDateTime不包含时区信息");
        System.out.println("- ZonedDateTime包含时区信息");
        System.out.println("- 在Spring中，通常使用LocalDateTime存储，ZonedDateTime用于显示");
        System.out.println("- 数据库建议统一使用UTC时区存储\n");
    }
}
