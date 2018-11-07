package cn.linj2n.melody.web.controller.admin;

import cn.linj2n.melody.domain.Tag;
import cn.linj2n.melody.service.TagService;
import cn.linj2n.melody.web.dto.PostDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(value = "/admin/tags")
public class TagController {

    private TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @ModelAttribute("allTags")
    public List<Tag> getAllTags() {
        return tagService.listAllTagsWithPosts();
    }

    @RequestMapping(value = {"","/", "/index"}, method = GET)
    public ModelAndView setupPostList(ModelMap modelMap) {
        return new ModelAndView("admin/tags", modelMap);
    }
}
