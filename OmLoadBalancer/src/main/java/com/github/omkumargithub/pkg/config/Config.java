package com.github.omkumargithub.pkg.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.omkumargithub.pkg.domain.Service;

public class Config {
    // public Service[] services;
    public ArrayList<Service> services;

    String startegy;




    public static Config loadConfigFromFile(String filePath) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();

        try {
            System.out.println(mapper);
            return mapper.readValue(new File(filePath), Config.class);
        } catch (IOException e) {
            System.out.println(e);
            return null; // Handle exception properly in your application
        }
    }

    // public Config() {
    //     ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    //     mapper.findAndRegisterModules();

    //     try {
    //         Config config = mapper.readValue(new File("OmLoadBalancer\\src\\main\\resouces\\wConfig.yaml"), Config.class);

    //     } catch (IOException e) {
    //         System.out.println(e);
    //     }
    // }
}






