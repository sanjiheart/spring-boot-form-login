package tw.sanjiheart.ctrl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationController {

  @GetMapping("/login")
  String login() {
    return "login";
  }
  
  @GetMapping("/logout")
  String logout() {
    return "logout";
  }

}
