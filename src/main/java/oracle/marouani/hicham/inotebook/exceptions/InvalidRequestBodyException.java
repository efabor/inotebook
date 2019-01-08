package oracle.marouani.hicham.inotebook.exceptions;

public class InvalidRequestBodyException extends BadRequestException {

  public InvalidRequestBodyException(Class<?> expectedClass) {
    super("The provided request body is invalid. Expected a " + expectedClass.getSimpleName() + " element.");
  }

}
