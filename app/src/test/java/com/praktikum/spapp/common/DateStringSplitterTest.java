package com.praktikum.spapp.common;

import org.junit.Test;
import org.junit.jupiter.api.Disabled;

import static org.junit.Assert.assertEquals;

public class DateStringSplitterTest {
    @Test
    public void datePrettyPrintTest() {
        System.out.println(DateStringSplitter.datePrettyPrint("2020-07-03T14:54:01.654734+02:00"));
    }

//    @Test
//    @Disabled
//    public void changeToDateFormat(){
//        System.out.print(DateStringSplitter.changeToDateFormat("01.5.2004","20:55 Uhr"));
//    }
}
