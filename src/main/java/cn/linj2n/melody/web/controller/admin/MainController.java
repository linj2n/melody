package cn.linj2n.melody.web.controller.admin;

import cn.linj2n.melody.domain.User;
import cn.linj2n.melody.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Locale;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
@RequestMapping(value = "/admin")
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    private UserService userService;

    private MessageSource messageSource;

    @Autowired
    public MainController(UserService userService, MessageSource messageSource) {
        this.userService = userService;
        this.messageSource = messageSource;
    }

    @RequestMapping(value = "/login", method = GET)
    public String login() {
        return "admin/login";
    }

    @RequestMapping(value = {"/index"}, method = GET)
    public String getIndex() {
        return "admin/index";
    }

    @RequestMapping(value = "/account/forget_password", method = GET)
    public String getResetEmailInputView() {
        return "admin/forget-password";
    }
    @RequestMapping(value = "/account/password_reset/{resetKey}", method = GET)
    public ModelAndView getPasswordResetView(@PathVariable final String resetKey, ModelMap model, Locale local) {
        User user = userService.geptUserByPasswordResetKey(resetKey).orElse(null);
        if (user == null) {
            logger.info("Get URL /account/password_reset/{} : Wrong Key.", resetKey);
            model.addAttribute("error",messageSource.getMessage("account.wrongResetLink",null,local));
            return new ModelAndView("/admin/login",model);
        }
        model.addAttribute("user", user);
        model.addAttribute("resetKey", resetKey);
        return new ModelAndView("/admin/password-reset", model);
    }

    @RequestMapping(value = "/blank", method = GET)
    public String blank() {
        return "admin/blank";
    }
}
