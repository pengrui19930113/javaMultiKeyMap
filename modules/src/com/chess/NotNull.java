package com.chess;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Retention(RetentionPolicy.SOURCE)
@Target(value={PARAMETER
//        ,FIELD
//        ,METHOD
})
public @interface NotNull {
}
