package com.github.omkumargithub.pkg.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.omkumargithub.pkg.domain.Service;
import com.github.omkumargithub.pkg.domain.Replica;


public class Config {
    // public Service[] services;
    public ArrayList<Service> services;

    // String startegy;




    public static Config loadConfigFromFile(String filePath) {
        System.out.println("Loading Config File Started");
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();

        try {
            System.out.println(mapper);
            System.out.println("Loading Config File completed");

            Config config = mapper.readValue(new File(filePath), Config.class);
            // System.out.println("Config startegy : "+config.startegy + "     type is     "+ config.startegy.getClass());
            System.out.println("Config Services  type is  "+config.services.getClass() );
            
            for (Service s :config.services){
                System.out.println("Config Service name  : "+s.name + "     type is     "+ s.name.getClass());
                System.out.println("Config Service matcher  : "+s.matcher + "     type is     "+ s.matcher.getClass());
                System.out.println("Config Service strategy  : "+s.strategy + "     type is     "+ s.strategy.getClass());
                System.out.println("Config Service replica type is  "+ s.replicas.getClass() );
                for(Replica r :s.replicas){
                    System.out.println("Config Service Replica url  : "+r.url + "     type is     "+ r.url.getClass() );
                    // for(HashMap<String, String> m :r.metaData){
                    System.out.println("Config Service replica metadata type is  "+ r.metaData.getClass() );
                    r.metaData.forEach((k,v)->{

                        System.out.println("Config Service Replica Key  : "+k  + "     type is     "+ k.getClass());
                        System.out.println("Config Service Replica value  : "+v + "     type is     "+ v.getClass());


                    });
                }


            }


            return config;
        

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






