/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Jakub Kolar
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.jakubkolar.autobuilder;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Test;

public class AutoBuilderIT {

    @Test
    public void testNothingJustPrintTheFields() {
        AutoBuilder.registerValue("TestClass2.string", "SomeValue");


        TestClass2 t = AutoBuilder.instanceOf(TestClass2.class).build();
        System.out.println(t);
        t.c.compareTo(1);
        System.out.println(Comparable.class.isAssignableFrom(String.class));

    }

    @Test(expected = UnsupportedOperationException.class)
    public void genericClassesCannotBeResolved() {
        GenericClass<?> t = AutoBuilder.instanceOf(GenericClass.class).build();
        System.out.println(t);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void noTypeInfoForComparable_Unsupported() {
        Comparable<?> t = AutoBuilder.instanceOf(Comparable.class).build();
        System.out.println(t);
    }

    public static class TestClass2 {
        public String string;
        public Comparable<String> c1;
        public Comparable<Integer> c;
        public Object o;

        @Override
        public String toString() {
            return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
        }
    }

    public static class GenericClass<T> {
        T genericField;
        Comparable<T> genericComparableField;
        Comparable<Number> c2;
        Comparable<? super Comparable<Integer>> c3;
        int i;
        byte b;
        char ch;
        long l;
    }
}
