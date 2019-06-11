package cn.linj2n.melody.service.impl;

import cn.linj2n.melody.domain.Category;
import cn.linj2n.melody.domain.Post;
import cn.linj2n.melody.domain.Tag;
import cn.linj2n.melody.domain.enumeration.PostStatus;
import cn.linj2n.melody.repository.CategoryRepository;
import cn.linj2n.melody.repository.PostRepository;
import cn.linj2n.melody.repository.TagRepository;
import cn.linj2n.melody.repository.support.PostSpecification;
import cn.linj2n.melody.service.SiteService;
import cn.linj2n.melody.web.dto.Archive;
import cn.linj2n.melody.web.dto.PostDTO;
import cn.linj2n.melody.web.utils.DTOModelMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;


@Service
@Transactional(readOnly = true)
public class SiteServiceImpl implements SiteService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SiteServiceImpl.class);


    private PostRepository postRepository;

    private DTOModelMapper dtoModelMapper;

    private TagRepository tagRepository;

    private CategoryRepository categoryRepository;

    @Autowired
    public SiteServiceImpl(PostRepository postRepository, DTOModelMapper dtoModelMapper, TagRepository tagRepository, CategoryRepository categoryRepository) {
        this.postRepository = postRepository;
        this.dtoModelMapper = dtoModelMapper;
        this.tagRepository = tagRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Tag> listAllTagsWithPosts() {
        return tagRepository.findAll()
                .stream()
                .filter(tag -> !tag.getPosts().isEmpty())
                .collect(toList());
    }

    @Override
    public List<Category> listAllCategoriesWithPosts() {
        return categoryRepository.findAll()
                .stream()
                .filter(category -> !category.getPosts().isEmpty())
                .collect(toList());
    }

    @Override
    public List<Archive> listArchives() {
        return groupPostsByMonth(listAllPostsFromDB());
    }

    @Override
    public Map<String, List<Archive>> getArchivesGroupByYear() {
        List<Archive> archives = groupPostsByMonth(listAllPostsFromDB());
        return groupArchivesByYear(archives);
    }

    @Override
    public Map<String, List<Archive>> getArchivesByTagId(long tagId) {
        return tagRepository.findOptionalById(tagId).map(t -> {
            List<Post> posts = t.getPosts()
                    .stream()
                    .filter(post -> post.getStatus().equals(PostStatus.PUBLISHED))
                    .collect(toList());
            return groupArchivesByYear(groupPostsByMonth(posts));
        }).orElse(null);
    }

    @Override
    public Map<String, List<Archive>> getArchivesByCategoryId(long categoryId) {
        return categoryRepository.findOptionalById(categoryId).map(c -> {
            List<Post> posts = c.getPosts()
                    .stream()
                    .filter(post -> post.getStatus().equals(PostStatus.PUBLISHED))
                    .collect(toList());
            return groupArchivesByYear(groupPostsByMonth(posts));
        }).orElse(null);
    }

    @Override
    public List<Archive> getArchivesGroupByCategory() {
        List<Archive> archives = new ArrayList<>();
        for (Category category : categoryRepository.findAll()) {
            if (!category.getPosts().isEmpty()) {
                Archive archive = new Archive(category.getName());
                for (Post post : category.getPosts()) {
                    archive.addPost(dtoModelMapper.convertToDTO(post));
                }
                archives.add(archive);
            }
        }
        return archives;
    }

    @Override
    public TreeMap<Integer, List<PostDTO>> groupPostsByYear(List<Post> posts) {
        return posts
                .stream()
                .filter(post -> post.getStatus().equals(PostStatus.PUBLISHED))
                .map(dtoModelMapper::convertToDTO)
                .collect(groupingBy(PostDTO::getYearOfCreation,TreeMap::new,Collectors.toList()));
    }

    @Override
    public Map<Integer, Map<Month, List<PostDTO>>> groupPostsByYearMonth(List<Post> posts) {
        return posts
                .stream()
                .filter(post -> post.getStatus().equals(PostStatus.PUBLISHED))
                .map(dtoModelMapper::convertToDTO)
                .collect(groupingBy(PostDTO::getYearOfCreation,
                        groupingBy(PostDTO::getMonthOfCreation)));
    }

    @Override
    public TreeMap<Integer, List<PostDTO>> groupAllPostsByYear() {
        return groupPostsByYear(postRepository.findAllByOrderByCreatedAtDesc());
    }

    @Override
    public Map<Integer, Map<Month, List<PostDTO>>> groupAllPostsByYearMonth() {
        return groupPostsByYearMonth(postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(post -> {
                    post.getTags().size();
                    post.getCategories().size();
                    return post;
                })
                .collect(toList()));
    }

    @Override
    public Map<String, List<PostDTO>> groupAllPostsByCategory() {
        Map<String, List<PostDTO>> postsByCategoryName = categoryRepository
                .findAll()
                .stream()
                .collect(
                        groupingBy(Category::getName,
                                mapping(category -> category.getPosts()
                                                .stream()
                                                .filter(post -> post.getStatus().equals(PostStatus.PUBLISHED))
                                                .map(dtoModelMapper::convertToDTO)
                                                .collect(toList()),
                                        Collector.of(ArrayList::new,
                                                List::addAll,
                                                (x, y) -> {
                                                    x.addAll(y);
                                                    return x;
                                                })
                                )
                        )
                );
        postsByCategoryName
                .entrySet()
                .removeIf(categoryNameEntry -> categoryNameEntry.getValue().isEmpty());

        return postsByCategoryName;
    }

    @Override
    public Optional<Tag> getTagWithPostsById(long tagId) {
        return tagRepository.findOptionalById(tagId).map(tag -> {
            tag.getPosts().stream().sorted(Comparator.comparing(Post::getCreatedAt).reversed());
            tag.getPosts().size();
            return tag;
        });
    }

    @Override
    public Optional<Archive> getArchiveByTagId(long tagId) {
        return tagRepository.findOptionalById(tagId)
                .map(tag -> {
                    return Optional.of(new Archive(tag.getName(), tag.getPosts()
                            .stream()
                            .map(dtoModelMapper::convertToDTO)
                            .sorted(Comparator.comparing(PostDTO::getCreatedAt).reversed())
                            .collect(toList())));
                }).orElse(Optional.empty());
    }

    @Override
    public Optional<Category> getCategoryWithPostsById(long categoryId) {
        return categoryRepository.findOptionalById(categoryId).map(category -> {
            category.getPosts().stream().sorted(Comparator.comparing(Post::getCreatedAt).reversed());
            category.getPosts().size();
            return category;
        });
    }

    private Map<String, List<Archive>> groupArchivesByYear(List<Archive> archives) {
        Map<String, List<Archive>> archivesByYear = new HashMap<>();
        String groupYear = "";
        List<Archive> archivesGroup = new ArrayList<>();
        for (Archive archive : archives) {
            String archiveYear = archive.getName().substring(0, 4);
            if (!archiveYear.equals(groupYear)) {
                if (!groupYear.isEmpty()) {
                    archivesGroup = new ArrayList<>();
                }
                archivesByYear.put(archiveYear, archivesGroup);
                groupYear = archiveYear;
            }
            archivesGroup.add(archive);
        }
        return archivesByYear;
    }

    private List<Archive> groupPostsByMonth(List<Post> posts) {
        if (posts == null || posts.isEmpty()) {
            return null;
        }
        DateTimeFormatter yyyyMM = DateTimeFormatter.ofPattern("yyyyMM");
        Archive monthArchive = new Archive();
        List<Archive> archives = new ArrayList<>();
        String nameOfArchive = "";
        for (Post post : posts) {
            String month = post.getCreatedAt().format(yyyyMM);
            if (!nameOfArchive.equals(month)) {
                if (!nameOfArchive.isEmpty()) {
                    monthArchive = new Archive();
                }
                monthArchive.setName(month);
                nameOfArchive = month;
                archives.add(monthArchive);
            }
            monthArchive.addPost(dtoModelMapper.convertToDTO(post));
        }
        return archives;
    }


    private List<Post> listAllPostsFromDB() {
        return postRepository.findAllByOrderByCreatedAtDesc(PostSpecification.isPublished());

    }

    @Override
    public Page<PostDTO> listPostsByPage(PageRequest pageRequest) {
        // TODO: Eliminate "unchecked" warnings.
        Page<Post> postDTOPage = postRepository.findAll(PostSpecification.isPublished(), pageRequest);
        return postDTOPage.map(dtoModelMapper::convertToDTO);
    }
}
