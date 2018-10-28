package cn.linj2n.melody.web.controller.admin;

import cn.linj2n.melody.service.PostService;
import cn.linj2n.melody.web.dto.PostDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/admin/posts")
public class PostController {

    public static final Logger logger = LoggerFactory.getLogger(PostController.class);

    private PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @RequestMapping(value = {"/", "/index"}, method = GET)
    public ModelAndView listPost(ModelMap modelMap) {
        return new ModelAndView("admin/post");
    }

    @RequestMapping(value = "/new", method = POST)
    public ModelAndView createNewPost() {
        return null;
    }

    @RequestMapping(value = "/{postId}/edit", method = GET)
    public ModelAndView requestEditPost() {
        return null;
    }
}
