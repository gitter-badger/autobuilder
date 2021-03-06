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

package com.github.jakubkolar.autobuilder.groovy;

import groovy.lang.GroovyObjectSupport;

import java.util.Objects;

final class Variable extends GroovyObjectSupport {

    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public Object getProperty(String property) {
        // Called when a nested property is resolved, e.g. in:
        // TableDSL.parseSingle builder, { a.b.c = 1 }
        // this method will be called as: v.getProperty('b'), where v = Variable('a'),
        // result will be: Variable('a.b')
        return new Variable(name + '.' + property);
    }

    @Override
    public void setProperty(String property, Object newValue) {
        // Called when a nested property is set, e.g. in:
        // TableDSL.parseSingle builder, { a.b.c = 1 }
        // this method will be called as: v.setProperty('c', 1), where v = Variable('a.b')
        TableDSL.setProperty(name + '.' + property, newValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Variable other = (Variable) obj;
        return Objects.equals(this.name, other.name);
    }

    @Override
    public String toString() {
        return "Variable[" + name + ']';
    }
}
