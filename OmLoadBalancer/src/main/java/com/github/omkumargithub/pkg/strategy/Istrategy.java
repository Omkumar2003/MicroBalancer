package com.github.omkumargithub.pkg.strategy;


// import pkg.domain.Server;
import com.github.omkumargithub.pkg.domain.Server;

public interface Istrategy {
   Server next(Server[] servers);
}

