package tw.sanjiheart.ctrl;

public interface ApiController {

  public static final String API = "/api";

  public static final String USERS = API + "/users";
  public static final String USERS_USERNAME = USERS + "/{username:.+}";
  public static final String USERS_ME = USERS + "/me";

}
