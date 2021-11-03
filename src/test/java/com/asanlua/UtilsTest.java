package com.asanlua;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junitparams.JUnitParamsRunner.$;
import static org.hamcrest.core.Is.is;

@RunWith(JUnitParamsRunner.class)
public class UtilsTest
{

    private static final Object[] getPercentileLimitsAndValidity() {
        return $(
                $(0.1f, 0.2f, true),
                $(1.2f, 0.2f, false),
                $(-13, 12, false),
                $(0.3f, 0.2f, false),
                $(0.3f, 1.2f, false)
        );

    }

    private static final Object[] getCsvPositionAndExpectedValue() {
        return $(
                $("2", 0, 2f),
                $(",2021-01-25 08:38:00,2021-01-25 08:50:00,,4.93,,,248,168,,20.76,2.75,0.5,0,0,0.3,24.31,0\n", 4, 4.93f),
                $("a,3,84.3", 1, 3f),
                $("a,3,84.3", 2, 84.3f)
        );

    }

    @Test
    @Parameters(method = "getPercentileLimitsAndValidity")
    public void testArePercentileLimitsValid(float lowerLimit, float upperLimit, boolean isValid) {
        // Given
        Assert.assertThat(isValid, is(Utils.arePercentileLimitsValid(lowerLimit, upperLimit)));
    }

    @Test
    @Parameters(method = "getCsvPositionAndExpectedValue")
    public void testGetCsvValue(String csv, int position, float expectedValue) throws Exception {
        // Given
        Assert.assertThat(expectedValue, is(Utils.getCsvValue(csv, position)));
    }

}
