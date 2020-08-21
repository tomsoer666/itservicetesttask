package com.example.application.data.service;

import java.util.ArrayList;
import java.util.List;

public class StringParser {
    public static List<String[]> stringParser(String text) {
        try {
            if (text.trim().contentEquals(";")) {
                List<String[]> result = new ArrayList<>();
                result.add(new String[]{""} );
                result.add(new String[]{""} );
                return result;
            } else {
                List<String[]> result = new ArrayList<>();
                String[] strings = text.split(";");
                String[] strings1;
                for (int i = 0; i < 2; i++) {
                    strings1 = strings[i].split(",");
                    for (int j = 0; j < strings1.length; j++) {
                        strings1[j] = strings1[j].trim();
                    }
                    result.add(strings1);
                }
                return result;
            }
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            throw new java.lang.ArrayIndexOutOfBoundsException();
        }
    }
}
