package cn.linj2n.melody.web.controller.blog;


import cn.linj2n.melody.domain.Category;
import cn.linj2n.melody.domain.Option;
import cn.linj2n.melody.domain.Post;
import cn.linj2n.melody.domain.Tag;
import cn.linj2n.melody.service.ConfigService;
import cn.linj2n.melody.service.CountingService;
import cn.linj2n.melody.service.PostService;
import cn.linj2n.melody.service.SiteService;
import cn.linj2n.melody.web.dto.PostDTO;
import cn.linj2n.melody.web.utils.DTOModelMapper;
import cn.linj2n.melody.web.utils.ViewUtils;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static java.util.stream.Collectors.toList;


@Controller
public class SiteController {

    private final static Logger LOGGER = LoggerFactory.getLogger(SiteController.class);

    private PostService postService;

    private DTOModelMapper dtoModelMapper;

    private SiteService siteService;

    private ConfigService configService;

    private CountingService countingService;

    private ViewUtils viewUtils;

    private static String themes = "/themes/hux/";

    @Autowired
    public SiteController(PostService postService, DTOModelMapper dtoModelMapper, SiteService siteService, ConfigService configService, CountingService countingService, ViewUtils viewUtils) {
        this.postService = postService;
        this.dtoModelMapper = dtoModelMapper;
        this.siteService = siteService;
        this.configService = configService;
        this.countingService = countingService;
        this.viewUtils = viewUtils;
    }

    @ModelAttribute("allTags")
    public List<Tag> getAllTags() {
        return siteService.listAllTagsWithPosts();
    }

    @ModelAttribute("config")
    public Map<String, String> listAllOptions() {
        return configService.fetchAllOptionMap();
    }

    @ModelAttribute("allCategories")
    public List<Category> getAllCategories() {
        return siteService.listAllCategoriesWithPosts();
    }

    @ModelAttribute("localeOfUsingEnglish")
    public Locale getLocale() {
        return new Locale("ENGLISH");
    }

    @RequestMapping(value = "/posts/{postId}")
    public String getPageById(@PathVariable(value = "postId") Long postId, ModelMap modelMap, HttpServletRequest request) {
        Post post = postService.getPost(postId).orElse(new Post());
        String sessionId = request.getSession(true).getId();
        countingService.increasePostVisitCount(sessionId, postId);
        postService.increasePostViews(post.getId());
        PostDTO postDTO = dtoModelMapper.convertToDTO(post);
        modelMap.addAttribute("post",postDTO);
        modelMap.addAttribute("prePost", dtoModelMapper.convertToDTO(postService.getPost(post.getId() - 1L).orElse(null)));
        modelMap.addAttribute("nextPost", dtoModelMapper.convertToDTO(postService.getPost(post.getId() + 1L).orElse(null)));
        return themes + "posts";
    }

    @RequestMapping(value = "/archives")
    public String getArchives(ModelMap modelMap) {
        Map<Integer, List<PostDTO>> postsByYear = siteService.groupAllPostsByYear().descendingMap();
        modelMap.addAttribute("postNums",
                postsByYear
                        .values()
                        .stream()
                        .mapToLong(Collection::size)
                        .sum());
        modelMap.addAttribute("postsMap", postsByYear);
        return themes + "archives";
    }

    @RequestMapping(value = "/tags")
    public String getAllTagsPage() {
        return themes + "tags";
    }

    @RequestMapping(value = "/tags/{tagId}")
    public String getArchivesByTagId(ModelMap modelMap, @PathVariable(name = "tagId") Long tagId ) {
        return siteService.getTagWithPostsById(tagId)
                .map(tag -> {
                    Map<String, List<PostDTO>> postsByTagName = new HashMap<>(1);
                    postsByTagName.put(tag.getName(),
                            tag.getPosts()
                                    .stream()
                                    .map(dtoModelMapper::convertToDTO)
                                    .collect(toList()));
                    modelMap.addAttribute("postsMap", postsByTagName);
                    modelMap.addAttribute("archivesTitle", tag.getName());
                    modelMap.addAttribute("archivesType", "tag");
                    return themes + "archives";
                })
                .orElse(themes + "archives/error");
    }

    @RequestMapping(value = "/categories/{categoryId}")
    public String getArchivesByCategories(ModelMap modelMap, @PathVariable(name = "categoryId") Long categoryId ) {
        return siteService.getCategoryWithPostsById(categoryId)
                .map(category -> {
                    Map<String, List<PostDTO>> postsByCategoryName = new HashMap<>(1);
                    postsByCategoryName.put(category.getName(),
                            category.getPosts()
                                    .stream()
                                    .map(dtoModelMapper::convertToDTO)
                                    .collect(toList()));
                    modelMap.addAttribute("postsMap", postsByCategoryName);
                    modelMap.addAttribute("archivesTitle", category.getName());
                    modelMap.addAttribute("archivesType", "category");
                    return themes + "archives";
                })
                .orElse(themes + "archives/error");
    }

    @RequestMapping(value = "/categories")
    public String getCategories(ModelMap modelMap) {
        Map<String,List<PostDTO>> postsByCategoryName = siteService.groupAllPostsByCategory();
        modelMap.addAttribute("postNums",
                postsByCategoryName
                        .values()
                        .stream()
                        .mapToLong(Collection::size)
                        .sum());
        modelMap.addAttribute("postsByCategoryName", postsByCategoryName);
        return themes + "categories";
    }

    @RequestMapping(value = {"/","index"})
    public String listPosts(ModelMap modelMap, @PageableDefault Pageable pageable) {
        modelMap.addAttribute("postPage", siteService.listPostsByPage(pageable));
        return themes + "index";
    }
}
