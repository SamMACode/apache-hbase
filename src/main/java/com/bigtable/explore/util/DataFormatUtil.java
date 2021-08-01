package com.bigtable.explore.util;

/**
 * @author Sam Ma
 * @date 2020-4-15
 * 定义数据格式化的工具类 DataFormatUtil
 */
public class DataFormatUtil {

    public static String makeCreditCardQualifier(String[] values) {
        // <reformatted-expirationdate>-<cardnumber>

        // (m|mm)/yyyy --> yyyymm
        String[] expirationDate = values[17].split("/");
        return expirationDate[1] + StringUtils.leftPad(expirationDate[0], 2, '0') + "-" + values[15];
    }

    public static String reformatBirthdate(String original) {
        // (m|mm)/(d|dd)/yyyy -> yyyymmdd
        String[] values = original.split("/");
        return values[2] + StringUtils.leftPad(values[0], 2, '0') + StringUtils.leftPad(values[1], 2, '0');
    }

    public static String makeCreditCardInfo(String[] values) {
        // cardtype,cardnumber,expiration,cvv2
        return String.format("%s,%s,%s,%s", values[14], values[15], values[17], values[16]);
    }

    private DataFormatUtil() {
    }

}
