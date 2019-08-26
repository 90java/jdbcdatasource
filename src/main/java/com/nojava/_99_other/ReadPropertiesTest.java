package com.nojava._99_other;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ReadPropertiesTest {

    @Test
    public void test01() throws IOException {
        String s = getClass().getResource("/").getFile().toString();
        System.out.println(s);

        Properties properties = new Properties();
        properties.load(new FileInputStream(s+"db.properties"));

        System.out.println(properties.getProperty("url"));

    }

}
