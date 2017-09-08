package com.stasmobstudios.payconiqtest;

import com.stasmobstudios.payconiqtest.util.DateUtil;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTest {
    @BeforeClass
    public static void testClassSetup() {
        System.out.println("Getting test class ready");
    }

    @AfterClass
    public static void testClassCleanup() {
        System.out.println("Done with tests");
    }

    @Before
    public void setup() {
        System.out.println("Ready for testing!");
    }

    @After
    public void cleanup() {
        System.out.println("Done with unit test!");
    }

    @Test
    public void testDateConvertionToString() {
        Date currentDate = new Date();
        String stringFromDate = DateUtil.dateToString(currentDate, "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        assertEquals("Test current date/string conversion works bidirectionally ", currentDate.getTime(), DateUtil.stringToDate(stringFromDate, "yyyy-MM-dd'T'HH:mm:ss.SSSZ").getTime());
    }
}