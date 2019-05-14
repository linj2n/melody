package cn.linj2n.melody.web.rest;


import cn.linj2n.melody.domain.Category;
import cn.linj2n.melody.service.CategoryService;
import cn.linj2n.melody.web.utils.ResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
public class CategoryResource {
    private static final Logger logger = LoggerFactory.getLogger(CategoryResource.class);

    private CategoryService categoryService;

    @Autowired
    public CategoryResource(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @RequestMapping(value = "/v1/categories",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listAllCategories() {
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse(categoryService.listAllCategories()), HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/categories/{categoryId}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateCategory(@RequestBody Category category) {
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse("信息更新成功", categoryService.updateCategory(category)), HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/categories/{categoryId}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteTagById(@PathVariable(value = "categoryId") Long categoryId) {
        logger.info("request to delete category[id={}]",categoryId);
        if (!categoryService.existsById(categoryId)) {
            return new ResponseEntity<>(ResponseBuilder.buildFailedResponse("分类不存在"), HttpStatus.OK);
        }
        categoryService.removeCategoryById(categoryId);
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse("删除成功"), HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/categories",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createCategory(@RequestBody Category newCategory) {
        return new ResponseEntity<>(ResponseBuilder.buildSuccessResponse("创建成功", categoryService.createCategory(newCategory)),HttpStatus.CREATED);
    }
}
