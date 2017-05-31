/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.connector.mysql;

/**
 * A converter API for MySQL Unsigned Integer types. It intends to convert any integer type value from binlong into the correct representation of unsigned numeric
 * MySQL binlog stores unsigned numeric into this format: (insertion value - Maximum data type boundary - 1), therefore to calculate the correct unsigned numeric representation
 * we will inverse the original calculation by applying this calculation: (insertion value + Maximum data type boundary + 1). Please see DBZ-228 for more info
 *
 * @author Omar Al-Safi
 */
public class MySqlUnsignedIntegerConverter {
    /**
     * Maximum values for Unsigned Integer Types. Needed in order to calculate actual value of an Unsigned Integer Types from binlog value.
     * Reference to {@link https://dev.mysql.com/doc/refman/5.7/en/integer-types.html}
     */
    private static final Short TINYINT_MAX_VALUE = 255;
    private static final Integer SMALLINT_MAX_VALUE = 65535;
    private static final Integer MEDIUMINT_MAX_VALUE = 16777215;
    private static final Long INT_MAX_VALUE = 4294967295L;

    /**
     * Private constructor
     */
    private MySqlUnsignedIntegerConverter(){}

    /**
     * Convert original value insertion of type 'TINYINT' into the correct TINYINT UNSIGNED representation
     * Note: Unsigned TINYINT (8-bit) is represented in 'Short' 16-bit data type. Reference: https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html
     *
     * @param originalNumber {@link Short} the original insertion value
     * @return {@link Short} the correct representation of the original insertion value
     */
    public static Short convertUnsignedTinyint(Short originalNumber){
        if (originalNumber < 0){
            return (short) (originalNumber + TINYINT_MAX_VALUE + 1);
        } else {
            return originalNumber;
        }
    }

    /**
     * Convert original value insertion of type 'SMALLINT' into the correct SMALLINT UNSIGNED representation
     * Note: Unsigned SMALLINT (16-bit) is represented in 'Integer' 32-bit data type. Reference: https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html
     *
     * @param originalNumber {@link Integer} the original insertion value
     * @return {@link Integer} the correct representation of the original insertion value
     */
    public static Integer convertUnsignedSmallint(Integer originalNumber){
        if (originalNumber < 0){
            return originalNumber + SMALLINT_MAX_VALUE + 1;
        } else {
            return originalNumber;
        }
    }

    /**
     * Convert original value insertion of type 'MEDIUMINT' into the correct MEDIUMINT UNSIGNED representation
     * Note: Unsigned MEDIUMINT (32-bit) is represented in 'Integer' 32-bit data type since the MAX value of Unsigned MEDIUMINT 16777215 < Max value of Integer 2147483647
     *
     * @param originalNumber {@link Integer} the original insertion value
     * @return {@link Integer} the correct representation of the original insertion value
     */
    public static Integer convertUnsignedMediumint(Integer originalNumber){
        if (originalNumber < 0){
            return originalNumber + MEDIUMINT_MAX_VALUE + 1;
        } else {
            return originalNumber;
        }
    }

    /**
     * Convert original value insertion of type 'INT' into the correct INT UNSIGNED representation
     * Note: Unsigned INT (32-bit) is represented in 'Long' data type within Java. Reference: https://dev.mysql.com/doc/connector-j/5.1/en/connector-j-reference-type-conversions.html
     *
     * @param originalNumber {@link Long} the original insertion value
     * @return {@link Long} the correct representation of the original insertion value
     */
    public static Long convertUnsignedInteger(Long originalNumber){
        if (originalNumber < 0) {
            return originalNumber + INT_MAX_VALUE + 1;
        } else {
            return originalNumber;
        }
    }
}