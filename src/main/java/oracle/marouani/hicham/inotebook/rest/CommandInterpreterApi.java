package oracle.marouani.hicham.inotebook.rest;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import oracle.marouani.hicham.inotebook.Config;
import oracle.marouani.hicham.inotebook.domain.Command;
import oracle.marouani.hicham.inotebook.exceptions.InvalidRequestBodyException;
import oracle.marouani.hicham.inotebook.exceptions.NotFoundResourceException;
import oracle.marouani.hicham.inotebook.json.JsonWriter;
import oracle.marouani.hicham.inotebook.json.reader.CommandReader;
import oracle.marouani.hicham.inotebook.services.CommandInterpreterService;
import oracle.marouani.hicham.inotebook.utils.MonoUtils;
import oracle.marouani.hicham.inotebook.utils.Responses;
import reactor.core.publisher.Mono;

public class CommandInterpreterApi {

  // Paths
  public static final String BASE_PATH = "";

  private final CommandInterpreterService commandInterpreterService;

  public final RouterFunction<ServerResponse> routerFunction;

  public CommandInterpreterApi(CommandInterpreterService commandInterpreterService) {
    this.commandInterpreterService = commandInterpreterService;

    this.routerFunction = RouterFunctions.
    route(POST(BASE_PATH).and(contentType(MediaType.APPLICATION_JSON)), this::handleRemoteCommand);
  }

  private Mono<ServerResponse> handleRemoteCommand(ServerRequest request) {
	    return request.bodyToMono(String.class).
	      flatMap(this::readCommandFromRequestBody).
	      flatMap(this.commandInterpreterService::handleCommand).
	      flatMap(JsonWriter::write).
	      flatMap(Responses::ok).
	      onErrorResume(NotFoundResourceException.class, Responses::notFound).
	      onErrorResume(InvalidRequestBodyException.class, Responses::badRequest).
	      subscribeOn(Config.APPLICATION_SCHEDULER);
	  }

  private Mono<Command> readCommandFromRequestBody(String body) {
	    return MonoUtils.fromOptional(
	      CommandReader.read(body),
	      () -> new InvalidRequestBodyException(Command.class)
	    );
	  }

}
