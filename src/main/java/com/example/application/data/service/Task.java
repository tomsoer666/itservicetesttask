package com.example.application.data.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Task {

    public static String[] subSortedArray(String[] array1, String[] array2) {
        List<String> resultList = new ArrayList<>();
        for (String element2 : array2) {
            for (String element1 : array1) {
                if (element2.contains(element1)) {
                    if (!resultList.contains(element1)) {
                        resultList.add(element1);
                    }
                }
            }
        }
        Collections.sort(resultList);
        return resultList.toArray(new String[resultList.size()]);
    }

    public static String expanded(long number) {
        try {
            if (number < 0) {
                return "Enter digit greater then 0!";
            } else if (number == 0) {
                return "0";
            } else {
                String[] inputString = Long.toString(number).split("");
                StringBuilder result = new StringBuilder();
                for (int i = 0; i < inputString.length - 1; i++) {
                    if (Long.parseLong(inputString[i]) > 0) {
                        result.append(inputString[i]);
                        for (int j = i; j < inputString.length - 1; j++) {
                            result.append('0');
                        }
                        result.append(" + ");
                    }
                }
                result.append(inputString[inputString.length - 1]);
                return result.toString();
            }
        } catch (java.lang.NumberFormatException e) {
            return "Enter digit greater then 0!";
        }
    }
}
