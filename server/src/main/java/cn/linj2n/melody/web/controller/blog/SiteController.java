package cn.linj2n.melody.web.controller.blog;


import cn.linj2n.melody.domain.Category;
import cn.linj2n.melody.domain.Post;
import cn.linj2n.melody.domain.Tag;
import cn.linj2n.melody.service.CategoryService;
import cn.linj2n.melody.service.PostService;
import cn.linj2n.melody.service.SiteService;
import cn.linj2n.melody.service.TagService;
import cn.linj2n.melody.web.dto.Archive;
import cn.linj2n.melody.web.dto.PostDTO;
import cn.linj2n.melody.web.utils.DTOModelMapper;
import cn.linj2n.melody.web.utils.ViewUtils;
import javafx.geometry.Pos;
import org.jsoup.Jsoup;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.*;
import java.util.stream.Collectors.*;

import static java.util.stream.Collectors.toList;


@Controller
public class SiteController {

    private final static int DEFAULT_PAGE_NUMS = 0;

    private final static int DEFAULT_PAGE_SIZE = 10;

    private final static int DEFAULT_CONTENT_PREVIEW_SIZE = 300;

    private PostService postService;

    private DTOModelMapper dtoModelMapper;

    private SiteService siteService;

    private ViewUtils viewUtils;

    private static String themes = "/themes/hux/";

    @Autowired
    public SiteController(PostService postService, DTOModelMapper dtoModelMapper,  ViewUtils viewUtils, SiteService siteService) {
        this.postService = postService;
        this.dtoModelMapper = dtoModelMapper;
        this.viewUtils = viewUtils;
        this.siteService = siteService;
    }

    @ModelAttribute("allTags")
    public List<Tag> getAllTags() {
        return siteService.listAllTagsWithPosts();
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
    public String getPageById(@PathVariable(value = "postId") Long postId, ModelMap modelMap) {
        // TODO: handle the valid postId
        Post post = postService.getPost(postId).orElse(new Post());
        postService.increasePostViews(postId);
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
    public String listPosts(ModelMap modelMap,  @RequestParam(name = "page",required = false) Integer pageNum, @RequestParam(name = "size",required = false) Integer pageSize) {
        PageRequest request = new PageRequest(pageNum == null ? DEFAULT_PAGE_NUMS : pageNum - 1,
                pageSize == null ? DEFAULT_PAGE_SIZE : pageSize);
        Page<PostDTO> postDTOS = siteService.listPostsByPage(request).map(postDto -> {
            String contentPreview = Jsoup.parse(postDto.getContent()).text();
            postDto.setContent(contentPreview.length() > DEFAULT_CONTENT_PREVIEW_SIZE
                    ? contentPreview.substring(0, DEFAULT_CONTENT_PREVIEW_SIZE)
                    : contentPreview);
            return postDto;
        });
        modelMap.addAttribute("postPage", postDTOS);
        return themes + "index";
    }
}
