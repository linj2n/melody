package cn.linj2n.melody.web.rest;


import cn.linj2n.melody.domain.Tag;
import cn.linj2n.melody.repository.TagRepository;
import cn.linj2n.melody.service.TagService;
import cn.linj2n.melody.web.utils.ResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class TagResource {
    private static final Logger logger = LoggerFactory.getLogger(TagResource.class);

    private TagService tagService;

    @Autowired
    public TagResource(TagService tagService) {
        this.tagService = tagService;
    }


    @RequestMapping(value = "/v1/tags",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createTag(@RequestBody Tag newTag) {
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse("标签创建成功", tagService.createTag(newTag)), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/v1/tags/{tagId}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateTag(@RequestBody Tag tag) {
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse("标签信息更新成功", tagService.updateTag(tag)), HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/tags/{tagId}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteTagById(@PathVariable(value = "tagId") Long tagId) {
        logger.info("request to delete tag[id={}]",tagId);
        if (!tagService.existsById(tagId)) {
            return new ResponseEntity<>(ResponseBuilder.buildFailedResponse("标签不存在", null), HttpStatus.NOT_FOUND);
        }
        tagService.removeTagById(tagId);
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse("删除成功", null), HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/tags",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllTags() {
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse(null, tagService.listAllTags()), HttpStatus.OK);
    }
}
