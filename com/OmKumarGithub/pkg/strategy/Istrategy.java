package pkg.strategy;
import pkg.domain.Server;

public interface Istrategy {
   Server next(Server[] servers);
}
