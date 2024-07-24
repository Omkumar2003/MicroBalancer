# MicroBalancer

![MicroBalancer](MicroBalancer.png)

A load balancer supporting multiple LB strategies written in java.

## Goal

The goal of this project is purley educational.

The balancer's main abstraction is a `Service`, each service has a name,
a balancing strategy, and a group of identical replicas that can serve requests
for the same service.

the service is configured via `yaml` file that is provided at startup, the most
recent example can be found in the `examples` folder.


```yaml
services: 
  - 
    matcher: /ui
    name: "Web UI"
    replicas: 
      - "http://192.168.23.1:8081"
      - "http://192.168.23.5:8082"
    strategy: RoundRobin
  - 
    matcher: /api/v1
    name: "Stateless API"
    replicas: 
      - "http://192.168.23.1:8081"
      - "http://192.168.23.5:8082"
    strategy: WeightedRoundRobin
```

## Building a demo

If you want to try load balancer, you can run the demo server in the 'MicroBalancer\src\main\java\com\github\omkumargithub\TestMain.java' directory, which starts server on sepecific port.

Launching the load balancer  
- maven should be downloaded
```
 mvn clean package
 java -jar .\target\MicroBalancer-1.0.jar
```