package pkg.config;

import domain.Service;
import domain.Server;
import strategy.Rb;
import health.Checker;

public class Config {
    Service[] services;
    String startergy;
}


class ServerList{
    Server[] servers;
    String Names;
    Rb strategy;
    Checker healthChecker;
}
