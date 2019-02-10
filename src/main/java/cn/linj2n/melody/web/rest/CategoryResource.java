package cn.linj2n.melody.web.rest;


import cn.linj2n.melody.domain.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class CategoryResource {
    private static final Logger logger = LoggerFactory.getLogger(CategoryResource.class);
}
