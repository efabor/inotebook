package oracle.marouani.hicham.inotebook.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import oracle.marouani.hicham.inotebook.exceptions.NotFoundResourceException;
import reactor.core.publisher.Mono;

public class CommandRunner {

	private String script;

	public CommandRunner(String script) {
		this.script = script;
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
            Runtime run  = Runtime.getRuntime();
            String[] cmds = new String[3];
            cmds[0] = new String("python");
            cmds[1] = new String("-c");
            cmds[2] = "\"" + script + "\"";

            Process proc = run.exec(cmds);

            try {
				proc.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
	            Mono.error(new NotFoundResourceException(cmds[0] + " " +
	            		cmds[1] + " " + cmds[2]));
			}

            StringBuilder output = streamReaderToStringBuilder(new InputStreamReader(proc.getInputStream()));
            StringBuilder errOutput = streamReaderToStringBuilder(new InputStreamReader(proc.getErrorStream()));

            if (output.length() > 0)
            	return output.toString();
            if (errOutput.length() > 0)
            	return errOutput.toString();

            return "";
	}

}
