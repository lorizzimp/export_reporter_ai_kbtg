package com.evobox.reportexporter.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public final class BahtText {
    //~ Static fields/initializers ·············································

    private static final String[] NUMBER_SCALES = {
            "ล้าน", "สิบ", "ร้อย",
            "พัน", "หมื่น", "แสน", ""
    };
    private static final String[] DIGIT_WORDS = {
            "ศูนย์", "หนึ่ง", "สอง",
            "สาม", "สี่", "ห้า", "หก",
            "เจ็ด", "แปด", "เก้า"
    };

    //~ Instance fields ························································

    private final BigDecimal amount;
    private String amountText;

    //~ Constructors ···························································

    public BahtText(double amount) {
        this.amount = new BigDecimal(amount);
    }

    public BahtText(float amount) {
        this.amount = new BigDecimal(amount);
    }

    public BahtText(int amount) {
        this.amount = new BigDecimal(amount);
    }

    public BahtText(long amount) {
        this.amount = new BigDecimal(amount);
    }

    public BahtText(String amount) {
        this.amount = new BigDecimal(amount);
    }

    public BahtText(Number amount) {
        this.amount = new BigDecimal(String.valueOf(amount));
    }

    //~ Methods ································································
    public static String getBahtText(Double amount) {
        return getBahtTextByString(String.valueOf(amount));
    }
    public static String getBahtTextByString(String amountstring) {
        BigDecimal amount = new BigDecimal(amountstring);
        StringBuilder buffer = new StringBuilder();
        BigDecimal absolute = amount.abs();
        int precision = absolute.precision();
        int scale = absolute.scale();
        int roundedPrecision = ((precision - scale) + 2);
        MathContext mc = new MathContext(roundedPrecision, RoundingMode.HALF_UP);
        BigDecimal rounded = absolute.round(mc);
        BigDecimal[] compound = rounded.divideAndRemainder(BigDecimal.ONE);
        boolean negativeAmount = (0 > amount.compareTo(BigDecimal.ZERO));

        compound[0] = compound[0].setScale(0);
        compound[1] = compound[1].movePointRight(2);

        if (negativeAmount) {
            buffer.append("ลบ");
        }

        buffer.append(getNumberText(compound[0].toBigIntegerExact()));
        buffer.append("บาท");

        if (0 == compound[1].compareTo(BigDecimal.ZERO)) {
            buffer.append("ถ้วน");
        } else {
            buffer.append(getNumberText(compound[1].toBigIntegerExact()));
            buffer.append("สตางค์");
        }

        return buffer.toString();
    }

    public static String getBahtText(BigDecimal amount) {
        StringBuilder buffer = new StringBuilder();
        BigDecimal absolute = amount.abs();
        int precision = absolute.precision();
        int scale = absolute.scale();
        int roundedPrecision = ((precision - scale) + 2);
        MathContext mc = new MathContext(roundedPrecision, RoundingMode.HALF_UP);
        BigDecimal rounded = absolute.round(mc);
        BigDecimal[] compound = rounded.divideAndRemainder(BigDecimal.ONE);
        boolean negativeAmount = (0 > amount.compareTo(BigDecimal.ZERO));

        compound[0] = compound[0].setScale(0);
        compound[1] = compound[1].movePointRight(2);

        if (negativeAmount) {
            buffer.append("ลบ");
        }

        buffer.append(getNumberText(compound[0].toBigIntegerExact()));
        buffer.append("บาท");

        if (0 == compound[1].compareTo(BigDecimal.ZERO)) {
            buffer.append("ถ้วน");
        } else {
            buffer.append(getNumberText(compound[1].toBigIntegerExact()));
            buffer.append("สตางค์");
        }

        return buffer.toString();
    }

    private static String getNumberText(BigInteger number) {
        StringBuilder buffer = new StringBuilder();
        char[] digits = number.toString()
                .toCharArray();

        for (int index = digits.length; index > 0; --index) {
            int digit = Integer.parseInt(String.valueOf(digits[digits.length
                    - index]));
            String digitText = DIGIT_WORDS[digit];
            int scaleIdx = ((1 < index)
                    ? ((index - 1) % 6)
                    : 6);

            if ((1 == scaleIdx) && (2 == digit)) {
                digitText = "ยี่";
            }

            if (1 == digit) {
                switch (scaleIdx) {
                    case 0:
                    case 6:
                        buffer.append((index < digits.length)
                                ? "เอ็ด"
                                : digitText);
                        break;
                    case 1:
                        break;

                    default:
                        buffer.append(digitText);

                        break;
                }
            } else if (0 == digit) {
                if (0 == scaleIdx) {
                    buffer.append(NUMBER_SCALES[scaleIdx]);
                }

                continue;
            } else {
                buffer.append(digitText);
            }

            buffer.append(NUMBER_SCALES[scaleIdx]);
        }

        return buffer.toString();
    }

    public String toString() {

        synchronized (this) {
            if (null == amountText) {
                this.amountText = getBahtText(this.amount);
            }
        }
        return this.amountText;
    }
}