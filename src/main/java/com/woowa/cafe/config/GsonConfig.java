package com.woowa.cafe.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonConfig {
    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
}
