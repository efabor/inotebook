package oracle.marouani.hicham.inotebook.json.reader;

import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import oracle.marouani.hicham.inotebook.domain.Command;

public class CommandReader {

  private static final ObjectMapper JSON = new ObjectMapper();

  public static Optional<Command> read(String value) {
    try {
      final JsonNode node = JSON.readTree(value);

      return Optional.of(new Command(
        node.get("code").asText()
      ));
    } catch (Exception e) {
      return Optional.empty();
    }
  }

}
