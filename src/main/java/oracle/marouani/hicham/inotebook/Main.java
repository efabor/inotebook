package oracle.marouani.hicham.inotebook;

import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;

import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.ipc.netty.http.server.HttpServer;
import oracle.marouani.hicham.inotebook.services.CommandInterpreterService;
import oracle.marouani.hicham.inotebook.rest.CommandInterpreterApi;

public class Main {

  public static void main(String[] args) {
    final CommandInterpreterService commandInterpreterService =
    		new CommandInterpreterService();
    final CommandInterpreterApi commandInterpreterApi =
    		new CommandInterpreterApi(commandInterpreterService);

    final RouterFunction<ServerResponse> routerFunction = RouterFunctions.
      nest(RequestPredicates.path("/execute"), commandInterpreterApi.routerFunction);
    final HttpHandler httpHandler = RouterFunctions.toHttpHandler(routerFunction);
    final ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(httpHandler);

    HttpServer.
      create(Config.HOST, Config.PORT).
      startAndAwait(adapter);
  }

}
