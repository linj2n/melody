package cn.linj2n.melody.service;

import cn.linj2n.melody.domain.Post;
import cn.linj2n.melody.domain.Tag;
import cn.linj2n.melody.domain.enumeration.PostStatus;
import cn.linj2n.melody.repository.PostRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TagServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(TagServiceTest.class);

    @Autowired
    private TagService tagService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

//    @Before
//    @Transactional
//    public void prepareDataForTest() {
//        Post newPost1 = new Post("TestPost1","Hello, I'm a testcase1.",PostStatus.DRAFT,"/test1",99999L);
//        Post newPost2 = new Post("TestPost2","Hello, I'm a testcase2.",PostStatus.DRAFT,"/test2",9999L);
//        Post newPost3 = new Post("TestPost3","Hello, I'm a testcase3.",PostStatus.DRAFT,"/test3",999L);
//        Post newPost4 = new Post("TestPost4","Hello, I'm a testcase4.",PostStatus.DRAFT,"/test4",99L);
//        List<Tag> tags = new ArrayList<Tag>(Arrays.asList(new Tag("tag1"),
//                new Tag("tag2"),
//                new Tag("tag3"),
//                new Tag("tag4"))
//        );
//
//        long start = System.currentTimeMillis();
//        logger.info("newPost1.id= " + newPost1.getId());
//        newPost1.addTag(tags.get(0));
//
//        for (int i = 0; i < 2; i ++) {
//            newPost2.addTag(tags.get(i));
//        }
//        for (int i1 = 0; i1 < 3; i1 ++) {
//            newPost3.addTag(tags.get(i1));
//        }
//        for (int i2 = 0; i2 < 4; i2 ++) {
//            newPost4.addTag(tags.get(i2));
//        }
//        postRepository.save(Arrays.asList(newPost1, newPost2, newPost3, newPost4));
//        long end = System.currentTimeMillis();
//        logger.info("Insert time: {}", end - start);
//    }

    @Test
    public void test_listAllTags() {
//        assertEquals(4,tagService.listTags().size());
//        assertEquals(4,tagService.listTagsWithPosts().get(0).getPosts().size());
    }

    @Test
    public void test_getTags() {
        assertTrue(tagService.getTagByName("tag1").isPresent());
        assertTrue(tagService.getTagWithPosts("tag1").isPresent());
        assertEquals(4,tagService.getTagWithPosts("tag1").get().getPosts().size());
    }

    @Test
    public void test_createTag() {
        assertNotEquals(0,tagService.createTag("tag5").getId().longValue());
    }

    @Test
    public void test_removeTag() {
        Tag tag = tagService.getTagByName("tag1").orElseGet(null);
        assertNotNull(tag);
        tagService.removeTagByName("tag1");
        assertEquals(Optional.empty(), tagService.getTagByName("tag1"));
        postService.listAllPosts().stream().peek(post -> {
            assertTrue(!post.getTags().contains(tag));
        });
    }


}