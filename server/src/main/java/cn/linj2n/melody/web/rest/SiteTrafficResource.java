package cn.linj2n.melody.web.rest;

import cn.linj2n.melody.service.CountingService;
import cn.linj2n.melody.web.utils.ResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SiteTrafficResource {

    private static Logger logger = LoggerFactory.getLogger(SiteTrafficResource.class);

    private CountingService countingService;

    @Autowired
    public SiteTrafficResource(CountingService countingService) {
        this.countingService = countingService;
    }

    @RequestMapping(
            value = "/v1/site/views/total-count",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getSiteTotalViews() {
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse(null, countingService.getSiteTotalViews()), HttpStatus.OK);
    }

    @RequestMapping(
            value = "/v1/site/traffic-data",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getTrafficData() {
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse(null, countingService.getTrafficDataForTheLast15Days()), HttpStatus.OK);
    }

    @RequestMapping(
            value = "/v1/comments/recent-count",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> listCommentCountForTheLast15Days() {
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse(null, countingService.listTheNumberOfCommentsForTheLast15Days()), HttpStatus.OK);
    }

    @RequestMapping(
            value = "/v1/comments/total-count",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getCommentTotalCount() {
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse(null, countingService.getCommentTotalNumber()), HttpStatus.OK);
    }

    @RequestMapping(
            value = "/v1/posts/recent-count",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> listPostCountForTheLast15Days() {
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse(null, null), HttpStatus.OK);
    }


    @RequestMapping(
            value = "/v1/posts/total-count",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getPostTotalNumbers() {
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse(null, countingService.getPostTotalNumber()), HttpStatus.OK);
    }
}
