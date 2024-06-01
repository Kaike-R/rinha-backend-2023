package com.rinha2023.rinha2023;

import java.util.List;

public class Util {

    public Util()
    {

    }


    public String criarFlatData(List<String> string)
    {
        if (string == null)
            return null;


        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < string.size(); i++) {
            stringBuilder.append(string.get(i));
            stringBuilder.append("|");
        }

        return stringBuilder.toString();
    }

    public List<String> desfazerFlatData(String string)
    {
        if (string == null)
            return null;

        return List.of(string.split("\\|"));
    }

}
