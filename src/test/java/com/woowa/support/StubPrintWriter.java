package com.woowa.support;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class StubPrintWriter extends PrintWriter {

    private String value = "";

    public StubPrintWriter() {
        super(new StringWriter());
    }

    @Override
    public void print(String s) {
        value = s;
    }

    public String getPrintedValue() {
        return value;
    }
}
