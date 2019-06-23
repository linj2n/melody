package cn.linj2n.melody.web.rest;

import cn.linj2n.melody.service.ConfigService;
import cn.linj2n.melody.web.utils.ResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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
    public ResponseEntity<?> listAllSiteConfigOptions() {
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse(null, configService.fecthAllOptionMap()), HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/config",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateConfig(@RequestBody Map<String, String> optionMap) {
        configService.updateOptions(optionMap);
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse("更新成功", null), HttpStatus.OK);
    }
}
