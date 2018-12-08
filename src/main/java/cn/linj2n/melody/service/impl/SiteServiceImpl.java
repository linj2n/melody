package cn.linj2n.melody.service.impl;

import cn.linj2n.melody.domain.Post;
import cn.linj2n.melody.domain.Tag;
import cn.linj2n.melody.repository.PostRepository;
import cn.linj2n.melody.repository.TagRepository;
import cn.linj2n.melody.service.SiteService;
import cn.linj2n.melody.web.dto.Archive;
import cn.linj2n.melody.web.utils.DTOModelMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class SiteServiceImpl implements SiteService{

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SiteServiceImpl.class);


    private PostRepository postRepository;

    private DTOModelMapper dtoModelMapper;

    private TagRepository tagRepository;

    @Autowired
    public SiteServiceImpl(PostRepository postRepository, DTOModelMapper dtoModelMapper, TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.dtoModelMapper = dtoModelMapper;
        this.tagRepository = tagRepository;
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
    public Map<String, List<Archive>> getArchivesByTagId(Long tagId) {
        return tagRepository.findOptionalById(tagId).map(t -> {
            List<Post> posts = t.getPosts()
                    .stream()
                    .filter(post -> post.getStatus().equals("post"))
                    .collect(Collectors.toList());
            return groupArchivesByYear(groupPostsByMonth(posts));
        }).orElse(null);
    }

    private Map<String,List<Archive>> groupArchivesByYear(List<Archive> archives) {
        Map<String, List<Archive>> archivesByYear = new HashMap<>();
        String groupYear = "";
        List<Archive> archivesGroup = new ArrayList<>();
        for (Archive archive : archives) {
            String archiveYear = archive.getName().substring(0,4);
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
        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .filter(post -> post.getStatus().equals("post"))
                .collect(Collectors.toList());
    }
}
