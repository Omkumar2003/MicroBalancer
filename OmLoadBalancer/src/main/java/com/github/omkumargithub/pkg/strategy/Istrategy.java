package com.github.omkumargithub.pkg.strategy;


import java.util.List;

// import pkg.domain.Server;
import com.github.omkumargithub.pkg.domain.Server;

public interface Istrategy {
    Server next(List<Server> servers);
}

