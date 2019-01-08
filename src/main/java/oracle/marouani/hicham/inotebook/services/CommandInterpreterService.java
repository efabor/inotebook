package oracle.marouani.hicham.inotebook.services;

import java.io.IOException;
import java.util.regex.Pattern;

import oracle.marouani.hicham.inotebook.Config;
import oracle.marouani.hicham.inotebook.domain.Command;
import reactor.core.publisher.Mono;

public class CommandInterpreterService {

  public Mono<String> handleCommand(Command command) {
	  return Mono.just(command).map(com -> {
		  boolean valid_expression = 
				  Pattern.compile("%python\\s+.+").matcher(com.getCode()).matches();
		  if (valid_expression == true) {
			  String[] elements = com.getCode().split("\\s");

			  elements = com.getCode().split("%python");

			  CommandRunner commandRunner = new CommandRunner(elements[1]);

			  try {
				return commandRunner.runScript();
			  } catch (IOException e) {
				e.printStackTrace();
				return "INVALID";
			  }
		  } else
			  return "INVALID";
	  }).subscribeOn(Config.APPLICATION_SCHEDULER);
  }

}