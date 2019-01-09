package oracle.marouani.hicham.inotebook.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import oracle.marouani.hicham.inotebook.exceptions.NotFoundResourceException;
import reactor.core.publisher.Mono;

public class CommandRunner {

	private String script, compiler;

	public CommandRunner(String compiler, String script) {
		this.script = script.trim();
		this.compiler = compiler;
	}

	public StringBuilder streamReaderToStringBuilder(InputStreamReader isr) throws IOException {
        BufferedReader br = new BufferedReader(isr);
        StringBuilder output = new StringBuilder();
        String line;
        while ( (line = br.readLine()) != null) {
            if (output != null)
                output.append(line + "\n"); 
        }

        return output;
	}

	public String runScript() throws IOException { 
		ProcessBuilder pb = new ProcessBuilder(this.compiler, "-c", this.script);
		Process proc = pb.start();
		StringBuilder output = streamReaderToStringBuilder(
				new InputStreamReader(proc.getInputStream()));
		StringBuilder errOutput = streamReaderToStringBuilder(
				new InputStreamReader(proc.getErrorStream()));

		if (output.length() > 0)
			return output.toString();
		if (errOutput.length() > 0)
			return errOutput.toString();

		return "";
	}

}
