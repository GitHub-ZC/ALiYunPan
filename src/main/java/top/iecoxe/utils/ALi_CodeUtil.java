package top.iecoxe.utils;


import java.io.*;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * 生成 proofCode hash
 * @author IMAX-拾荒者
 * @date 2023-01-03
 */
public class ALi_CodeUtil {

    protected static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    // 理论上支持62进制的转换, 当然可以自己添加一些其他符号来增加进制数
    private static final String TARGET_STR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";


    public static String MD5(String input) {
        if (input == null || input.length() == 0) {
            return null;
        }
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(input.getBytes());
            byte[] byteArray = md5.digest();
            BigInteger bigInt = new BigInteger(1, byteArray);
            // 参数16表示16进制
            String result = bigInt.toString(16);
            // 不足32位高位补零
            while (result.length() < 32) {
                result = "0" + result;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String proofCode(String accountToken, File file) {
        String md5 = radixToNum(MD5(accountToken).substring(0, 16), 16);

        BigInteger bigInteger = new BigInteger(md5);
        long offset = 0L;
        Long filesize = file.length();

        if (filesize > 0) {
            offset = bigInteger.mod(BigInteger.valueOf(filesize)).longValue();
        }

        byte[] bytes = new byte[0];
        //文件指向前一字节
        try (FileInputStream inputStream = new FileInputStream(file);) {
            inputStream.skip(offset);
            int min = Integer.valueOf(String.valueOf(Math.min(8L, filesize - offset)));
            bytes = new byte[min];
            inputStream.read(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(Base64.getEncoder().encode(bytes));
    }

    public static String contentCode(File file) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            FileInputStream in = new FileInputStream(file);
            FileChannel ch = in.getChannel();
            MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            messageDigest.update(byteBuffer);
            return bufferToHex(messageDigest.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringBuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringBuffer);
        }
        return stringBuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringBuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];
        char c1 = hexDigits[bt & 0xf];
        stringBuffer.append(c0);
        stringBuffer.append(c1);
    }


    /**
     * 任意进制转10进制
     */
    public static String radixToNum(String number, int radix) {
        if (radix < 0 || radix > TARGET_STR.length()) {
            radix = TARGET_STR.length();
        }
        if (radix == 10) {
            return number;
        }

        char ch[] = number.toCharArray();
        int len = ch.length;

        BigInteger bigRadix = new BigInteger(radix + "");
        BigInteger result = new BigInteger("0");
        BigInteger base = new BigInteger("1");


        for (int i = len - 1; i >= 0; i--) {
            BigInteger index = new BigInteger(TARGET_STR.indexOf(ch[i]) + "");
            result = result.add(index.multiply(base));
            base = base.multiply(bigRadix);
        }

        return result.toString();
    }
}