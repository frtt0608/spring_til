package com.spring;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class HelloTest {

    @Test
    public void simpleProxy() {
        Hello hello = new HelloTarget();
        assertThat(hello.sayHello("HG"), is("Hello HG"));
        assertThat(hello.sayHi("HG"), is("Hi HG"));
        assertThat(hello.sayThankYou("HG"), is("Thank you HG"));
    }
}
