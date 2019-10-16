/*
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *
 * Copyright (c) 2014, Purdue University
 * Copyright (c) 2014, 2019, Oracle and/or its affiliates
 *
 * All rights reserved.
 */
package com.oracle.truffle.r.test.builtins;

import org.junit.Test;

import com.oracle.truffle.r.test.TestBase;
import java.util.Locale;

// Checkstyle: stop line length check

public class TestBuiltin_weekdaysDate extends TestBase {

    @Test
    public void testweekdaysDate1() {
        // Explicit locale setting is a workaround to run this test on JDK9.
        // Otherwise the test was returning "Thu" instead of "Thursday"
        // due to JDK bug 8130845.
        Locale.setDefault(Locale.ENGLISH);

        assertEval("argv <- structure(list(x = structure(16352, class = 'Date')), .Names = 'x');do.call('weekdays.Date', argv)");
    }
}
