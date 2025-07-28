package com.masi.employee.helper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = { ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ExcelCell {
    int column();

    String name();
}
