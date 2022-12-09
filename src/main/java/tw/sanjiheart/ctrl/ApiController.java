package tw.sanjiheart.ctrl;

public final class ApiController {

  public static final String API = "/api";

  public static final String USERS = API + "/users";
  public static final String USERS_USERNAME = USERS + "/{username:.+}";
  public static final String USERS_ME = USERS + "/me";

  private ApiController() {
    throw new IllegalStateException("Utility class");
  }

}
