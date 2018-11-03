package cn.linj2n.melody.web.controller.admin;

import cn.linj2n.melody.domain.Post;
import cn.linj2n.melody.service.CategoryService;
import cn.linj2n.melody.service.PostService;
import cn.linj2n.melody.service.TagService;
import cn.linj2n.melody.web.dto.PostDTO;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.*;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping(value = "/admin/posts")
public class PostController {

    public static final Logger logger = LoggerFactory.getLogger(PostController.class);

    private PostService postService;

    private TagService tagService;

    private CategoryService categoryService;

    private ModelMapper modelMapper;

    @Autowired
    public PostController(PostService postService, TagService tagService, CategoryService categoryService, ModelMapper modelMapper) {
        this.postService = postService;
        this.tagService = tagService;
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    @RequestMapping(value = {"/", "/index"}, method = GET)
    public ModelAndView setupPostList(ModelMap modelMap) {
        List<PostDTO> allPosts=new ArrayList<PostDTO>();
        postService.listAllPosts().forEach(post -> {
            PostDTO postDTO = modelMapper.map(post,PostDTO.class);
            postDTO.setId(post.getId());
        });

//        modelMap.addAttribute("allPosts", postService.listAllPosts());
        return new ModelAndView("admin/posts", modelMap);
    }

    @RequestMapping(value = "/new", method = GET)
    public String createPost() {
        String postIdString = postService.createPost().getId().toString();
        return "redirect:/admin/posts/" + postIdString + "/edit";
    }

    @RequestMapping(value = "/{postId}/edit", method = GET)
    public ModelAndView setupPostEditView(@PathVariable(value = "postId") Long id, ModelMap modelMap) {

        // TODO: Using @ModelAttribute to setup all Tags/Categories data
        modelMap.addAttribute("post", postService.getPost(id).orElseGet(null));
        modelMap.addAttribute("allTags", tagService.listTags());
        modelMap.addAttribute("allCategories", categoryService.listCategories());
        return new ModelAndView("admin/edit-post",modelMap);
    }
}