package com.stasmobstudios.payconiqtest.util;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stanislovas Mickus on 07/09/2017.
 */

public class JsonUtil {
    public static void writeStringArray(final JsonWriter out, final String name, final String[] strings) throws IOException {
        if (strings == null) out.name(name).nullValue();
        out.name(name).beginArray();
        for (String s : strings) {
            out.value(s);
        }
        out.name(name).endArray();
    }

    public static String[] getStringArray(final JsonReader jsonReader)  throws IOException {
        String[] strings;

        jsonReader.beginArray();
        List<String> stringsList = new ArrayList<String>();
        while (jsonReader.hasNext())
        {
            stringsList.add(jsonReader.nextString());
        }
        strings = new String[stringsList.size()];
        stringsList.toArray(strings);
        jsonReader.endArray();

        return strings;
    }
}
