package cn.linj2n.melody.web.rest;

import cn.linj2n.melody.service.ConfigService;
import cn.linj2n.melody.web.utils.ResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api")
public class ConfigResource {

    private static final Logger logger = LoggerFactory.getLogger(ConfigResource.class);

    private ConfigService configService;

    @Autowired
    public ConfigResource(ConfigService configService) {
        this.configService = configService;
    }

    @RequestMapping(value = "/v1/config",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> fetchSiteConfigOptions(@RequestParam(value = "optionName", required = false) List<String> optionNames/*@RequestParam Map<String, String> optionMap*/) {
        return new ResponseEntity<>(
                ResponseBuilder.buildSuccessResponse(
                        "获取配置信息成功",
                        configService.fetchOptionMap(optionNames)
                ),
                HttpStatus.OK
        );
    }

    @RequestMapping(value = "/v1/config",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateConfig(@RequestBody Map<String, String> optionMap) {
        configService.updateOptions(optionMap);
        return new ResponseEntity<>(
                ResponseBuilder.buildSuccessResponse("更新成功", null),
                HttpStatus.OK
        );
    }
}
