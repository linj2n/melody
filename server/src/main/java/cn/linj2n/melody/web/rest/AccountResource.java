package cn.linj2n.melody.web.rest;

import cn.linj2n.melody.domain.User;
import cn.linj2n.melody.security.SecurityUtil;
import cn.linj2n.melody.service.EmailService;
import cn.linj2n.melody.service.UserService;
import cn.linj2n.melody.web.dto.*;
import cn.linj2n.melody.web.dto.support.ResponseCode;
import cn.linj2n.melody.web.utils.DTOModelMapper;
import cn.linj2n.melody.web.utils.ResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.MessageSource;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/api")
public class AccountResource {

    private final Logger logger = LoggerFactory.getLogger(AccountResource.class);

    private static final String BASE_URL = "http://localhost:8080";

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private EmailService emailService;

    @Autowired
    private DTOModelMapper dtoModelMapper;

    @RequestMapping(
            value = "/blank",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBlank() {
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse(), HttpStatus.OK);
    }

    @RequestMapping(
            value = "/v1/account",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getAccountInfo() {

        return userService
                .getUserByLogin(SecurityUtil.getCurrentUserLogin())
                .map(user -> new ResponseEntity<>(
                        ResponseBuilder.buildSuccessResponse(null, dtoModelMapper.convertToDTO(user)),
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(
                        ResponseBuilder.buildFailedResponse("没有访问权限", null),
                        HttpStatus.OK
                ));
    }

    @RequestMapping(
            value = "/v1/account/email",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> changeEmail(@Valid @RequestBody EmailModificationInfoDTO emailModificationInfoDTO, Locale locale) {
        return userService
                .getUserByLogin(SecurityUtil.getCurrentUserLogin())
                .map(user -> {
                    boolean isValid = userService.validateVerificationCode(user, emailModificationInfoDTO.getCode());
                    if (!isValid) {
                        return new ResponseEntity<>(ResponseBuilder.buildFailedResponse("验证码错误", null), HttpStatus.OK);
                    }
                    userService.changeEmail(user, emailModificationInfoDTO.getEmail());
                    return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse("修改成功", null), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(
                        ResponseBuilder.buildFailedResponse("没有访问权限", null),
                        HttpStatus.OK
                ));

    }

    @RequestMapping(
            value = "/v1/account/password",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> changePassword(@Valid @RequestBody PasswordModificationInfoDTO passwordModificationInfoDTO) {
        return userService
                .getUserByLogin(SecurityUtil.getCurrentUserLogin())
                .map(user -> {
                    boolean isValid = userService.validateVerificationCode(user, passwordModificationInfoDTO.getCode());
                    if (!isValid) {
                        ResponseDTO failResponse = ResponseBuilder.buildFailedResponse("验证码错误", null);
                        return new ResponseEntity<>(failResponse, HttpStatus.OK);
                    }
                    userService.changePassword(user, passwordModificationInfoDTO.getPassword());
                    ResponseDTO successResponse = ResponseBuilder.buildSuccessResponse("密码修改成功", null);
                    return new ResponseEntity<>(successResponse, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(
                        ResponseBuilder.buildFailedResponse("没有访问权限", null),
                        HttpStatus.OK
                ));
    }

    @RequestMapping(
            value = "/v1/account/username",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> changeUsername(@Valid @RequestBody UserNameModificationInfoDTO userNameModificationInfoDTO) {
        return userService
                .getUserByLogin(SecurityUtil.getCurrentUserLogin())
                .map(user -> {
                    boolean isValid = userService.validateVerificationCode(user, userNameModificationInfoDTO.getCode());
                    if (!isValid) {
                        ResponseDTO failResponse = ResponseBuilder.buildFailedResponse("验证码错误", null);
                        return new ResponseEntity<>(failResponse, HttpStatus.OK);
                    }
                    userService.changeUsername(user, userNameModificationInfoDTO.getUsername());
                    ResponseDTO successResponse = ResponseBuilder.buildSuccessResponse("用户名修改成功", null);
                    return new ResponseEntity<>(successResponse, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(
                        ResponseBuilder.buildFailedResponse("没有访问权限", null),
                        HttpStatus.OK
                ));
    }

    @RequestMapping(
            value = "/v1/account/verification_code",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> sendResetKey() {
        return userService.getUserByLogin(SecurityUtil.getCurrentUserLogin())
                .map(user -> {
                    userService.generateVerificationCode(user);
                    emailService.sendVerificationCode(user, BASE_URL);
                    return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse("验证码已成功发到你的邮箱中", null), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(
                        ResponseBuilder.buildFailedResponse("没有访问权限", null),
                        HttpStatus.OK
                ));
    }

    @RequestMapping(
            value = "/v1/account/verification_code/{code}/_validate",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> validateResetKey(@PathVariable(value = "code") String code) {
        return userService.getUserByLogin(SecurityUtil.getCurrentUserLogin())
                .map(user -> {
                    boolean isValid = userService.validateVerificationCode(user, code);
                    Map<String, Boolean> res = new HashMap<>();
                    res.put("result", isValid);
                    ResponseDTO responseDTO =
                            isValid ? ResponseBuilder.buildSuccessResponse("验证码正确", res)
                                    : ResponseBuilder.buildFailedResponse("验证码错误, 请重新尝试", res);
                    return new ResponseEntity<>(responseDTO, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(
                        ResponseBuilder.buildFailedResponse("没有访问权限", null),
                        HttpStatus.OK
                ));
    }


    @RequestMapping(
            value = "/v1/account/password_reset/{resetCode}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changePassword(@PathVariable(value = "resetCode") final String resetCode, @ModelAttribute(value = "newPassword") final String newPassword, Locale local) {
        logger.info("Start to request password_reset . resetKey: {}", resetCode);
        ResponseDTO successResponse = ResponseBuilder.buildSuccessResponse(messageSource.getMessage("account.passwordReset.success",null,local), null);
        ResponseDTO failedResponse = ResponseBuilder.buildSuccessResponse(messageSource.getMessage("account.passwordReset.failed",null,local), null);
        return userService.resetPassword(newPassword,resetCode)
                .map(user -> new ResponseEntity<>(successResponse, HttpStatus.OK))
                .orElse(new ResponseEntity<>(failedResponse,HttpStatus.OK));
    }

    @RequestMapping(
            value = "/v1/account/password_reset",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> requestPasswordReset(@ModelAttribute(value = "userEmail") String userEmail, Locale local) {
        logger.info("Start to request password_reset . needResetEmail: {}", userEmail);
        ResponseDTO successResponse = ResponseBuilder.buildSuccessResponse(messageSource.getMessage("account.passwordResetRequest.success",null,local), null);
        ResponseDTO failedResponse = ResponseBuilder.buildSuccessResponse(messageSource.getMessage("account.passwordResetRequest.failed",null,local), null);
        return userService.requestPasswordReset(userEmail)
                .map(user -> {
                    emailService.sendPasswordResetLinkToUserMail(user, BASE_URL);
                    logger.info("Request SUCCESS: {}",successResponse.getMessage());
                    return new ResponseEntity<>(successResponse,HttpStatus.CREATED);
                })
                .orElse(new ResponseEntity<>(failedResponse,HttpStatus.BAD_REQUEST));
    }

    @RequestMapping(
            value = "/v1/account/registration",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> addAccount(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        logger.info("Register a new Account: -----> UserDto {}", userDTO.toString());
        User user = userService.createUserInformation(userDTO.getLogin(), userDTO.getPassword(), userDTO.getLogin(), userDTO.getEmail());
//        emailService.sendActivationEmail(user, BASE_URL);
//        return new ResponseEntity<>(HttpStatus.CREATED);
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse(),HttpStatus.CREATED);
    }

    @RequestMapping(
            value = "/v1/account/existence",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> checkIfExistUser(@RequestParam(value = "login", required = false, defaultValue = "") final String login,
                                              @RequestParam(value = "email", required = false, defaultValue = "") final String email) {
        Map<String,Boolean> result = new HashMap<>();
        result.put("existed",userService.checkIfExitUserActivatedByLoginOrEmail(login, email));
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse(null, result),HttpStatus.OK);
    }

}
