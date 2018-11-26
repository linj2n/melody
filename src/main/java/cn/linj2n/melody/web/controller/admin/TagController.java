package cn.linj2n.melody.web.controller.admin;

import cn.linj2n.melody.domain.Tag;
import cn.linj2n.melody.service.TagService;
import cn.linj2n.melody.web.dto.PostDTO;
import cn.linj2n.melody.web.utils.DTOModelMapper;
import cn.linj2n.melody.web.utils.ViewUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(value = "/admin/tags")
public class TagController {

    private TagService tagService;

    private DTOModelMapper dtoModelMapper;

    @Autowired
    public TagController(TagService tagService, ModelMapper modelMapper,ViewUtils viewUtils, DTOModelMapper dtoModelMapper) {
        this.tagService = tagService;
        this.dtoModelMapper = dtoModelMapper;
    }

    @ModelAttribute("allTags")
    public List<Tag> getAllTags() {
        return tagService.listAllTagsWithPosts();
    }

    @RequestMapping(value = {"","/", "/index"}, method = GET)
    public ModelAndView setupPostList(ModelMap modelMap) {
        return new ModelAndView("admin/tags", modelMap);
    }

    @RequestMapping(value = "/{tagId}/posts", method = GET)
    public ModelAndView listPostsByTagId(@PathVariable(value = "tagId") Long tagId, ModelMap modelMap) {
        // TODO: handle invalid tagId
        List<PostDTO> allPosts=new ArrayList<>();
        tagService.getTagWithPosts(tagId).map(u -> {
            u.getPosts().forEach(post -> {
                allPosts.add(dtoModelMapper.convertToDTO(post));
            });
            return u;
        });
        modelMap.addAttribute("selectedTagId",tagId);
        modelMap.addAttribute("allPosts", allPosts);
        return new ModelAndView("admin/posts", modelMap);
    }
}
