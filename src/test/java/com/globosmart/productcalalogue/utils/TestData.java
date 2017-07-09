package com.globosmart.productcalalogue.utils;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

public class TestData {

    private JdbcTemplate jdbcTemplate;

    public TestData(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void givenProductsExist() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("target/test-classes/testdata.sql"));
        LineNumberReader fileReader = new LineNumberReader(in);
        String query = ScriptUtils.readScript(fileReader, "--", ";");
        for (String sql : query.split("\n")) {
            jdbcTemplate.update(sql);
        }

    }
}
