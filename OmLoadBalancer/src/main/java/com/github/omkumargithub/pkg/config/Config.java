package com.github.omkumargithub.pkg.config;

import java.io.File;
import java.io.IOException;

import com.github.omkumargithub.pkg.domain.Service;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class Config {
    // public Service[] services;
    public ArrayList<Service> services;

    String startegy;

    public Config() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
        try {
            Config config = mapper.readValue(new File("path/to/your/file.yaml"), Config.class);

        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
