/******************************************************************************
 * Copyright 2019-2020 Exactpro (Exactpro Systems Limited)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.exactpro.epfast.converter;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

class ConverterTest {
    private TypeConverter converter = new TypeConverter();

    @Test
    void decimalToInt() {
        assertAll(
            () -> assertThat(converter.decimalToLong(BigDecimal.valueOf(Integer.MAX_VALUE)))
                .isEqualTo(Integer.MAX_VALUE),
            () -> assertThat(converter.decimalToInt(BigDecimal.valueOf(25)))
                .isEqualTo(25),
            () -> assertThat(converter.decimalToInt(BigDecimal.ZERO))
                .isEqualTo(0),
            () -> assertThat(converter.decimalToInt(BigDecimal.valueOf(-1)))
                .isEqualTo(-1),
            () -> assertThat(converter.decimalToInt(BigDecimal.valueOf(-25)))
                .isEqualTo(-25),
            () -> assertThat(converter.decimalToLong(BigDecimal.valueOf(Integer.MIN_VALUE)))
                .isEqualTo(Integer.MIN_VALUE),

            () -> assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> converter.decimalToInt(new BigDecimal("10.50")))
                .withMessageMatching(wordPattern("10.50"))
                .withMessageMatching(wordPattern("int")),

            () -> assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> converter.decimalToInt(new BigDecimal("10000000000000.00")))
                .withMessageMatching(wordPattern("10000000000000.00"))
                .withMessageMatching(wordPattern("int"))
        );
    }

    @Test
    void decimalToLong() {
        assertAll(
            () -> assertThat(converter.decimalToLong(BigDecimal.valueOf(Long.MAX_VALUE)))
                .isEqualTo(Long.MAX_VALUE),
            () -> assertThat(converter.decimalToLong(new BigDecimal("1000000000000000.00")))
                .isEqualTo(1000000000000000L),
            () -> assertThat(converter.decimalToLong(BigDecimal.ZERO))
                .isEqualTo(0L),
            () -> assertThat(converter.decimalToLong(BigDecimal.valueOf(-1)))
                .isEqualTo(-1L),
            () -> assertThat(converter.decimalToLong(new BigDecimal("-1000000000000000.00")))
                .isEqualTo(-1000000000000000L),
            () -> assertThat(converter.decimalToLong(BigDecimal.valueOf(Long.MIN_VALUE)))
                .isEqualTo(Long.MIN_VALUE),

            () -> assertThatExceptionOfType(ArithmeticException.class)
                .isThrownBy(() -> converter.decimalToLong(new BigDecimal("10.50")))
                .withMessageMatching(wordPattern("10.50"))
                .withMessageMatching(wordPattern("long")),
            () -> assertThatExceptionOfType(ArithmeticException.class)
                .isThrownBy(() -> converter.decimalToLong(new BigDecimal("10000000000000000000000000.00")))
                .withMessageMatching(wordPattern("10000000000000000000000000.00"))
                .withMessageMatching(wordPattern("long"))
        );
    }

    @Test
    void decimalToBigInteger() {
        assertAll(
            () -> assertThat(converter.decimalToBigInteger(new BigDecimal("10000000000000000000000000.00")))
                .isEqualTo(new BigInteger("10000000000000000000000000")),
            () -> assertThat(converter.decimalToBigInteger(BigDecimal.ZERO)).isEqualTo(BigInteger.ZERO),
            () -> assertThat(converter.decimalToBigInteger(BigDecimal.valueOf(-1))).isEqualTo(BigInteger.valueOf(-1)),
            () -> assertThat(converter.decimalToBigInteger(new BigDecimal("-10000000000000000000000000.00")))
                .isEqualTo(new BigInteger("-10000000000000000000000000")),

            () -> assertThatExceptionOfType(ArithmeticException.class)
                .isThrownBy(() -> converter.decimalToBigInteger(new BigDecimal("10.50")))
                .withMessageMatching(wordPattern("10.50"))
                .withMessageMatching(wordPattern("BigInteger"))
        );
    }

    @Test
    void decimalToString() {
        assertAll(
            () -> assertThat(converter.decimalToString(new BigDecimal("5.5555E2")))
                .isEqualTo("555.55"),
            () -> assertThat(converter.decimalToString(BigDecimal.ZERO))
                .isEqualTo("0"),
            () -> assertThat(converter.decimalToString(new BigDecimal("-555.55")))
                .isEqualTo("-555.55")
        );
    }

    @Test
    void uint64ToBigDecimal() {
        assertAll(
            () -> assertThat(converter.uint64ToBigDecimal(-1L))
                .isEqualTo(BigDecimal.valueOf(Long.MAX_VALUE).multiply(BigDecimal.valueOf(2)).add(BigDecimal.ONE)),
            () -> assertThat(converter.uint64ToBigDecimal(-25L))
                .isEqualTo(new BigDecimal("18446744073709551591")),
            () -> assertThat(converter.uint64ToBigDecimal(Long.MIN_VALUE))
                .isEqualTo(new BigDecimal(BigInteger.ONE.shiftLeft(63))),
            () -> assertThat(converter.uint64ToBigDecimal(Long.MAX_VALUE))
                .isEqualTo(BigDecimal.valueOf(Long.MAX_VALUE)),
            () -> assertThat(converter.uint64ToBigDecimal(25L))
                .isEqualTo(BigDecimal.valueOf(25L)),
            () -> assertThat(converter.uint64ToBigDecimal(0L))
                .isEqualTo(BigDecimal.ZERO)
        );
    }

    @Test
    void uint64ToBigInteger() {
        assertAll(
            () -> assertThat(converter.uint64ToBigInteger(-1L))
                .isEqualTo(BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(2)).add(BigInteger.ONE)),
            () -> assertThat(converter.uint64ToBigInteger(-25L))
                .isEqualTo(new BigInteger("18446744073709551591")),
            () -> assertThat(converter.uint64ToBigInteger(Long.MIN_VALUE))
                .isEqualTo(BigInteger.ONE.shiftLeft(63)),
            () -> assertThat(converter.uint64ToBigInteger(Long.MAX_VALUE))
                .isEqualTo(BigInteger.valueOf(Long.MAX_VALUE)),
            () -> assertThat(converter.uint64ToBigInteger(25L))
                .isEqualTo(BigInteger.valueOf(25L)),
            () -> assertThat(converter.uint64ToBigInteger(0L))
                .isEqualTo(BigInteger.ZERO)
        );
    }

    @Test
    void uint64ToLong() {
        assertAll(
            () -> assertThat(converter.uint64ToLong(Long.MAX_VALUE))
                .isEqualTo(Long.MAX_VALUE),
            () -> assertThat(converter.uint64ToLong(25L))
                .isEqualTo(25L),
            () -> assertThat(converter.uint64ToLong(0L))
                .isEqualTo(0L),

            () -> assertThatExceptionOfType(ArithmeticException.class)
                .isThrownBy(() -> converter.uint64ToLong(-1L))
                .withMessageMatching(wordPattern(
                    BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(2)).add(BigInteger.ONE).toString()))
                .withMessageMatching(wordPattern("long")),
            () -> assertThatExceptionOfType(ArithmeticException.class)
                .isThrownBy(() -> converter.uint64ToLong(-25L))
                .withMessageMatching(wordPattern("18446744073709551591"))
                .withMessageMatching(wordPattern("long")),
            () -> assertThatExceptionOfType(ArithmeticException.class)
                .isThrownBy(() -> converter.uint64ToLong(Long.MIN_VALUE))
                .withMessageMatching(wordPattern(BigInteger.ONE.shiftLeft(63).toString()))
                .withMessageMatching(wordPattern("long"))
        );
    }

    @Test
    void uint64ToInt() {
        assertAll(
            () -> assertThat(converter.uint64ToInt(Integer.MAX_VALUE))
                .isEqualTo(Integer.MAX_VALUE),
            () -> assertThat(converter.uint64ToInt(25L))
                .isEqualTo(25L),
            () -> assertThat(converter.uint64ToInt(0L))
                .isEqualTo(0L),

            () -> assertThatExceptionOfType(ArithmeticException.class)
                .isThrownBy(() -> converter.uint64ToInt(-1L))
                .withMessageMatching(wordPattern(
                    BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(2)).add(BigInteger.ONE).toString()))
                .withMessageMatching(wordPattern("int")),
            () -> assertThatExceptionOfType(ArithmeticException.class)
                .isThrownBy(() -> converter.uint64ToInt(-25L))
                .withMessageMatching(wordPattern("18446744073709551591"))
                .withMessageMatching(wordPattern("int")),
            () -> assertThatExceptionOfType(ArithmeticException.class)
                .isThrownBy(() -> converter.uint64ToInt(Integer.MAX_VALUE + 1L))
                .withMessageMatching(wordPattern(Long.toString(Integer.MAX_VALUE + 1L)))
                .withMessageMatching(wordPattern("int"))
        );
    }

    @Test
    void uint64ToString() {
        assertAll(
            () -> assertThat(converter.uint64ToString(-1L))
                .isEqualTo("18446744073709551615"),
            () -> assertThat(converter.uint64ToString(-25L))
                .isEqualTo("18446744073709551591"),
            () -> assertThat(converter.uint64ToString(25L))
                .isEqualTo("25"),
            () -> assertThat(converter.uint64ToString(0L))
                .isEqualTo("0")
        );
    }

    @Test
    void int64ToBigDecimal() {
        assertAll(
            () -> assertThat(converter.int64ToBigDecimal(Long.MAX_VALUE))
                .isEqualTo(BigDecimal.valueOf(Long.MAX_VALUE)),
            () -> assertThat(converter.int64ToBigDecimal(25L))
                .isEqualTo(BigDecimal.valueOf(25L)),
            () -> assertThat(converter.int64ToBigDecimal(0L))
                .isEqualTo(BigDecimal.ZERO),
            () -> assertThat(converter.int64ToBigDecimal(-1L))
                .isEqualTo(BigDecimal.valueOf(-1L)),
            () -> assertThat(converter.int64ToBigDecimal(-25L))
                .isEqualTo(BigDecimal.valueOf(-25L)),
            () -> assertThat(converter.int64ToBigDecimal(Long.MIN_VALUE))
                .isEqualTo(BigDecimal.valueOf(Long.MIN_VALUE))
        );
    }

    @Test
    void int64ToBigInteger() {
        assertAll(
            () -> assertThat(converter.int64ToBigInteger(Long.MAX_VALUE))
                .isEqualTo(BigInteger.valueOf(Long.MAX_VALUE)),
            () -> assertThat(converter.int64ToBigInteger(25L))
                .isEqualTo(BigInteger.valueOf(25L)),
            () -> assertThat(converter.int64ToBigInteger(0L))
                .isEqualTo(BigInteger.ZERO),
            () -> assertThat(converter.int64ToBigInteger(-1L))
                .isEqualTo(BigInteger.valueOf(-1L)),
            () -> assertThat(converter.int64ToBigInteger(-25L))
                .isEqualTo(BigInteger.valueOf(-25L)),
            () -> assertThat(converter.int64ToBigInteger(Long.MIN_VALUE))
                .isEqualTo(BigInteger.valueOf(Long.MIN_VALUE))
        );
    }

    @Test
    void int64ToInt() {
        assertAll(
            () -> assertThat(converter.int64ToInt(Integer.MAX_VALUE))
                .isEqualTo(Integer.MAX_VALUE),
            () -> assertThat(converter.int64ToInt(25L))
                .isEqualTo(25),
            () -> assertThat(converter.int64ToInt(0L))
                .isEqualTo(0),
            () -> assertThat(converter.int64ToInt(-1L))
                .isEqualTo(-1),
            () -> assertThat(converter.int64ToInt(-25L))
                .isEqualTo(-25),
            () -> assertThat(converter.int64ToInt(Integer.MIN_VALUE))
                .isEqualTo(Integer.MIN_VALUE),

            () -> assertThatExceptionOfType(ArithmeticException.class)
                .isThrownBy(() -> converter.int64ToInt(Long.MAX_VALUE))
                .withMessageMatching(wordPattern(Long.toString(Long.MAX_VALUE)))
                .withMessageMatching(wordPattern("int")),
            () -> assertThatExceptionOfType(ArithmeticException.class)
                .isThrownBy(() -> converter.int64ToInt(Integer.MAX_VALUE + 1L))
                .withMessageMatching(wordPattern(Long.toString(Integer.MAX_VALUE + 1L)))
                .withMessageMatching(wordPattern("int")),
            () -> assertThatExceptionOfType(ArithmeticException.class)
                .isThrownBy(() -> converter.int64ToInt(Integer.MIN_VALUE - 1L))
                .withMessageMatching(wordPattern(Long.toString(Integer.MIN_VALUE - 1L)))
                .withMessageMatching(wordPattern("int")),
            () -> assertThatExceptionOfType(ArithmeticException.class)
                .isThrownBy(() -> converter.int64ToInt(Long.MIN_VALUE))
                .withMessageMatching(wordPattern(Long.toString(Long.MIN_VALUE)))
                .withMessageMatching(wordPattern("int"))
        );
    }

    @Test
    void int64ToString() {
        assertAll(
            () -> assertThat(converter.int64ToString(25L))
                .isEqualTo("25"),
            () -> assertThat(converter.int64ToString(0L))
                .isEqualTo("0"),
            () -> assertThat(converter.int64ToString(-1L))
                .isEqualTo("-1"),
            () -> assertThat(converter.int64ToString(-25L))
                .isEqualTo("-25")
        );
    }

    @Test
    void uint32ToBigDecimal() {
        assertAll(
            () -> assertThat(converter.uint32ToBigDecimal(-1))
                .isEqualTo(BigDecimal.valueOf(0xFFFF_FFFFL)),
            () -> assertThat(converter.uint32ToBigDecimal(-25))
                .isEqualTo(BigDecimal.valueOf(0xFFFF_FFE7L)),
            () -> assertThat(converter.uint32ToBigDecimal(Integer.MIN_VALUE))
                .isEqualTo(new BigDecimal(BigInteger.ONE.shiftLeft(31))),
            () -> assertThat(converter.uint32ToBigDecimal(Integer.MAX_VALUE))
                .isEqualTo(BigDecimal.valueOf(Integer.MAX_VALUE)),
            () -> assertThat(converter.uint32ToBigDecimal(25))
                .isEqualTo(BigDecimal.valueOf(25)),
            () -> assertThat(converter.uint32ToBigDecimal(0))
                .isEqualTo(BigDecimal.ZERO)
        );
    }

    @Test
    void uint32ToBigInteger() {
        assertAll(
            () -> assertThat(converter.uint32ToBigInteger(-1))
                .isEqualTo(BigInteger.valueOf(0xFFFF_FFFFL)),
            () -> assertThat(converter.uint32ToBigInteger(-25))
                .isEqualTo(BigInteger.valueOf(0xFFFF_FFE7L)),
            () -> assertThat(converter.uint32ToBigInteger(Integer.MIN_VALUE))
                .isEqualTo(BigInteger.ONE.shiftLeft(31)),
            () -> assertThat(converter.uint32ToBigInteger(Integer.MAX_VALUE))
                .isEqualTo(BigInteger.valueOf(Integer.MAX_VALUE)),
            () -> assertThat(converter.uint32ToBigInteger(25))
                .isEqualTo(BigInteger.valueOf(25)),
            () -> assertThat(converter.uint32ToBigInteger(0))
                .isEqualTo(BigInteger.ZERO)
        );
    }

    @Test
    void uint32ToLong() {
        assertAll(
            () -> assertThat(converter.uint32ToLong(-1))
                .isEqualTo(0xFFFF_FFFFL),
            () -> assertThat(converter.uint32ToLong(-25))
                .isEqualTo(0xFFFF_FFE7L),
            () -> assertThat(converter.uint32ToLong(Integer.MIN_VALUE))
                .isEqualTo(0x8000_0000L),
            () -> assertThat(converter.uint32ToLong(Integer.MAX_VALUE))
                .isEqualTo(Integer.MAX_VALUE),
            () -> assertThat(converter.uint32ToLong(25))
                .isEqualTo(25),
            () -> assertThat(converter.uint32ToLong(0))
                .isEqualTo(0)
        );
    }

    @Test
    void uint32ToInt() {
        assertAll(
            () -> assertThat(converter.uint32ToInt(Integer.MAX_VALUE))
                .isEqualTo(Integer.MAX_VALUE),
            () -> assertThat(converter.uint32ToInt(25))
                .isEqualTo(25),
            () -> assertThat(converter.uint32ToInt(0))
                .isEqualTo(0),

            () -> assertThatExceptionOfType(ArithmeticException.class)
                .isThrownBy(() -> converter.uint32ToInt(-1))
                .withMessageMatching(wordPattern(Long.toString(0xFFFF_FFFFL)))
                .withMessageMatching(wordPattern("int")),
            () -> assertThatExceptionOfType(ArithmeticException.class)
                .isThrownBy(() -> converter.uint32ToInt(-25))
                .withMessageMatching(wordPattern(Long.toString(0xFFFF_FFE7L)))
                .withMessageMatching(wordPattern("int")),
            () -> assertThatExceptionOfType(ArithmeticException.class)
                .isThrownBy(() -> converter.uint32ToInt(Integer.MIN_VALUE))
                .withMessageMatching(wordPattern(Long.toString(0x8000_0000L)))
                .withMessageMatching(wordPattern("int"))
        );
    }

    @Test
    void uint32ToString() {
        assertAll(
            () -> assertThat(converter.uint32ToString(-1))
                .isEqualTo(Long.toString(0xFFFF_FFFFL)),
            () -> assertThat(converter.uint32ToString(-25))
                .isEqualTo(Long.toString(0xFFFF_FFE7L)),
            () -> assertThat(converter.uint32ToString(25))
                .isEqualTo("25"),
            () -> assertThat(converter.uint32ToString(0))
                .isEqualTo("0")
        );
    }

    @Test
    void int32ToBigDecimal() {
        assertAll(
            () -> assertThat(converter.int32ToBigDecimal(Integer.MAX_VALUE))
                .isEqualTo(BigDecimal.valueOf(Integer.MAX_VALUE)),
            () -> assertThat(converter.int32ToBigDecimal(25))
                .isEqualTo(BigDecimal.valueOf(25)),
            () -> assertThat(converter.int32ToBigDecimal(0))
                .isEqualTo(BigDecimal.ZERO),
            () -> assertThat(converter.int32ToBigDecimal(-1))
                .isEqualTo(BigDecimal.valueOf(-1)),
            () -> assertThat(converter.int32ToBigDecimal(-25))
                .isEqualTo(BigDecimal.valueOf(-25)),
            () -> assertThat(converter.int32ToBigDecimal(Integer.MIN_VALUE))
                .isEqualTo(BigDecimal.valueOf(Integer.MIN_VALUE))
        );
    }

    @Test
    void int32ToBigInteger() {
        assertAll(
            () -> assertThat(converter.int32ToBigInteger(Integer.MAX_VALUE))
                .isEqualTo(BigInteger.valueOf(Integer.MAX_VALUE)),
            () -> assertThat(converter.int32ToBigInteger(25))
                .isEqualTo(BigInteger.valueOf(25)),
            () -> assertThat(converter.int32ToBigInteger(0))
                .isEqualTo(BigInteger.ZERO),
            () -> assertThat(converter.int32ToBigInteger(-1))
                .isEqualTo(BigInteger.valueOf(-1)),
            () -> assertThat(converter.int32ToBigInteger(-25))
                .isEqualTo(BigInteger.valueOf(-25)),
            () -> assertThat(converter.int32ToBigInteger(Integer.MIN_VALUE))
                .isEqualTo(BigInteger.valueOf(Integer.MIN_VALUE))
        );
    }

    @Test
    void int32ToLong() {
        assertAll(
            () -> assertThat(converter.int32ToLong(Integer.MAX_VALUE))
                .isEqualTo(Integer.MAX_VALUE),
            () -> assertThat(converter.int32ToLong(25))
                .isEqualTo(25L),
            () -> assertThat(converter.int32ToLong(0))
                .isEqualTo(0L),
            () -> assertThat(converter.int32ToLong(-1))
                .isEqualTo(-1L),
            () -> assertThat(converter.int32ToLong(-25))
                .isEqualTo(-25L),
            () -> assertThat(converter.int32ToLong(Integer.MIN_VALUE))
                .isEqualTo(Integer.MIN_VALUE)
        );
    }

    @Test
    void int32ToString() {
        assertAll(
            () -> assertThat(converter.int32ToString(25))
                .isEqualTo("25"),
            () -> assertThat(converter.int32ToString(0))
                .isEqualTo("0"),
            () -> assertThat(converter.int32ToString(-1))
                .isEqualTo("-1"),
            () -> assertThat(converter.int32ToString(-25))
                .isEqualTo("-25")
        );
    }

    @Test
    void stringToBigDecimal() {
        assertAll(
            () -> assertThat(converter.stringToBigDecimal("25.40"))
                .isEqualTo(new BigDecimal("25.40")),
            () -> assertThat(converter.stringToBigDecimal("\t\r\n 25.40 \t\r\n"))
                .isEqualTo(new BigDecimal("25.40")),
            () -> assertThat(converter.stringToBigDecimal("0000"))
                .isEqualTo(BigDecimal.ZERO),
            () -> assertThat(converter.stringToBigDecimal("10000000000000000000"))
                .isEqualTo(new BigDecimal("10000000000000000000")),
            () -> assertThat(converter.stringToBigDecimal("-10000000000000000000000000000000000000000.55555555"))
                .isEqualTo(new BigDecimal("-10000000000000000000000000000000000000000.55555555")),

            () -> assertThatExceptionOfType(NumberFormatException.class)
                .isThrownBy(() -> converter.stringToBigDecimal("1.f"))
                .withMessageMatching(wordPattern("1.f"))
                .withMessageMatching(wordPattern("BigDecimal"))
        );
    }

    @Test
    void stringToBigInteger() {
        assertAll(
            () -> assertThat(converter.stringToBigInteger("25"))
                .isEqualTo(BigInteger.valueOf(25)),
            () -> assertThat(converter.stringToBigInteger("0000"))
                .isEqualTo(BigInteger.ZERO),
            () -> assertThat(converter.stringToBigInteger("10000000000000000000"))
                .isEqualTo(new BigInteger("10000000000000000000")),
            () -> assertThat(converter.stringToBigInteger("\t\r\n 10000000000000000000 \t\r\n"))
                .isEqualTo(new BigInteger("10000000000000000000")),
            () -> assertThat(converter.stringToBigInteger("-10000000000000000000000000000000000000000"))
                .isEqualTo(new BigInteger("-10000000000000000000000000000000000000000")),

            () -> assertThatExceptionOfType(NumberFormatException.class)
                .isThrownBy(() -> converter.stringToBigInteger("1.7"))
                .withMessageMatching(wordPattern("1.7"))
                .withMessageMatching(wordPattern("BigInteger"))
        );
    }

    @Test
    void stringToLong() {
        assertAll(
            () -> assertThat(converter.stringToLong("25"))
                .isEqualTo(25L),
            () -> assertThat(converter.stringToLong("\t\r\n 25 \t\r\n"))
                .isEqualTo(25L),
            () -> assertThat(converter.stringToLong("0"))
                .isEqualTo(0L),
            () -> assertThat(converter.stringToLong("-1"))
                .isEqualTo(-1L),

            () -> assertThatExceptionOfType(NumberFormatException.class)
                .isThrownBy(() -> converter.stringToLong("1.7"))
                .withMessageMatching(wordPattern("1.7"))
                .withMessageMatching(wordPattern("long")),

            () -> assertThatExceptionOfType(NumberFormatException.class)
                .isThrownBy(() -> converter.stringToLong("100000000000000000000000000000"))
                .withMessageMatching(wordPattern("100000000000000000000000000000"))
                .withMessageMatching(wordPattern("long"))
        );
    }

    @Test
    void stringToInt() {
        assertAll(
            () -> assertThat(converter.stringToInt("25"))
                .isEqualTo(25),
            () -> assertThat(converter.stringToInt("\t\r\n 25 \t\r\n"))
                .isEqualTo(25),
            () -> assertThat(converter.stringToInt("0"))
                .isEqualTo(0),
            () -> assertThat(converter.stringToInt("-1"))
                .isEqualTo(-1),

            () -> assertThatExceptionOfType(NumberFormatException.class)
                .isThrownBy(() -> converter.stringToInt("1.7"))
                .withMessageMatching(wordPattern("1.7"))
                .withMessageMatching(wordPattern("int")),

            () -> assertThatExceptionOfType(NumberFormatException.class)
                .isThrownBy(() -> converter.stringToInt("10000000000000"))
                .withMessageMatching(wordPattern("10000000000000"))
                .withMessageMatching(wordPattern("int"))
        );
    }

    @Test
    void stringToByteVector() {
        assertAll(
            () -> assertThat(converter.stringToByteVector(""))
                .isEmpty(),
            () -> assertThat(converter.stringToByteVector("00"))
                .containsExactly(0),
            () -> assertThat(converter.stringToByteVector("01"))
                .containsExactly(1),
            () -> assertThat(converter.stringToByteVector("FF"))
                .containsExactly(0xFF),
            () -> assertThat(converter.stringToByteVector("ff"))
                .containsExactly(0xFF),
            () -> assertThat(converter.stringToByteVector("3f5f7F"))
                .containsExactly(0x3F, 0x5F, 0x7F),
            () -> assertThat(converter.stringToByteVector("\t\r\n 3f5f7F \t\r\n"))
                .containsExactly(0x3F, 0x5F, 0x7F),
            () -> assertThat(converter.stringToByteVector("\t\r\n 3f\t5f\r7 F \t\r\n"))
                .containsExactly(0x3F, 0x5F, 0x7F),

            () -> assertThatExceptionOfType(NumberFormatException.class)
                .isThrownBy(() -> converter.stringToByteVector("3f5f 7"))
                .withMessageMatching(wordPattern("3f5f 7"))
                .withMessageMatching(wordPattern("byte[]")),
            () -> assertThatExceptionOfType(NumberFormatException.class)
                .isThrownBy(() -> converter.stringToByteVector("-00"))
                .withMessageMatching(wordPattern("-00"))
                .withMessageMatching(wordPattern("byte[]")),
            () -> assertThatExceptionOfType(NumberFormatException.class)
                .isThrownBy(() -> converter.stringToByteVector("0g"))
                .withMessageMatching(wordPattern("0g"))
                .withMessageMatching(wordPattern("byte[]"))
        );
    }

    @Test
    void byteVectorToString() {
        assertAll(
            () -> assertThat(converter.byteVectorToString(new byte[]{}))
                .isEqualTo(""),
            () -> assertThat(converter.byteVectorToString(new byte[]{0x00}))
                .isEqualTo("00"),
            () -> assertThat(converter.byteVectorToString(new byte[]{0x01}))
                .isEqualTo("01"),
            () -> assertThat(converter.byteVectorToString(new byte[]{0x10}))
                .isEqualTo("10"),
            () -> assertThat(converter.byteVectorToString(new byte[]{-1}))
                .isEqualTo("ff"),
            () -> assertThat(converter.byteVectorToString(new byte[]{0x3F, 0x5F, 0x7F}))
                .isEqualTo("3f5f7f")
        );
    }

    private static String wordPattern(String word) {
        return "(?:^|.*\\s)" + Pattern.quote(word) + "(?:\\s.*|$)";
    }
}
