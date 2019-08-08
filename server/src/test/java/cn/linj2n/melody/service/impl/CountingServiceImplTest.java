package cn.linj2n.melody.service.impl;

import cn.linj2n.melody.domain.Post;
import cn.linj2n.melody.domain.webdataanalysis.ResourceUniqueVisitor;
import cn.linj2n.melody.domain.webdataanalysis.ResourceView;
import cn.linj2n.melody.repository.ResourceUniqueVisitRepository;
import cn.linj2n.melody.repository.ResourceViewRepository;
import cn.linj2n.melody.utils.DateUtil;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CountingServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(CountingServiceImplTest.class);

    private static final int MOCK_DEFAULT_SESSION_ID_LENGTH = 42;

    private static final long DEF_POST_ID = 1;

    private List<ResourceUniqueVisitor> uvs = new ArrayList<>();

    private List<ResourceView> pvs = new ArrayList<>();

    private static final int DEF_SIZE = 28;

    private List<String> mockSessionIds ;

    private List<Long> mockPostIds ;

    @Autowired
    private CountingServiceImpl countingService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ResourceUniqueVisitRepository resourceUniqueVisitRepository;

    @Autowired
    private ResourceViewRepository resourceViewRepository;

    @Resource(name = "redisCountingTemplate")
    private HashOperations<String, String, Integer> cache;

    private static final String CACHE_POST_PV = "counting.post.pv";

    private static final String CACHE_POST_UV = "counting.post.uv";

    private static final String CACHE_SITE_PV = "counting.site";

    private static final String CACHE_SITE_UV = "visitorIds";

    private static final String SITE_PV = "site_pv";

    private static final String SITE_UV = "site_uv";

    private static final String CACHE_POST_ID_LIST = "counting.postIds";

    @Before
    public void prepare() {
        resourceUniqueVisitRepository.deleteAll();
        resourceViewRepository.deleteAll();
        logger.info("Init data set for testing.");

        mockSessionIds = getRandomSessionIdList(DEF_SIZE + 1);
        mockPostIds = LongStream.rangeClosed(1, DEF_SIZE).boxed().collect(Collectors.toList());

        mockPostIds.forEach(postId -> {
            String sessionId = mockSessionIds.get(postId.intValue());
            countingService.increaseSiteVisitCount(sessionId);
            countingService.increasePostVisitCount(sessionId, postId);
        });

        mockPostIds.forEach(postId -> {
            if (postId % 2 == 0) {
                String sessionId = mockSessionIds.get(postId.intValue());
                countingService.increaseSiteVisitCount(sessionId);
                countingService.increasePostVisitCount(sessionId, postId);
            }
        });
        for (int i = 1; i <= 15; i ++) {
            Post post = new Post();
            post.setId(DEF_POST_ID);
            ResourceView resourceView = new ResourceView(SITE_PV, Long.valueOf(i), post, DateUtil.daysAgo(i));
            ResourceUniqueVisitor resourceUniqueVisitor = new ResourceUniqueVisitor(SITE_UV, Long.valueOf(i), post, DateUtil.daysAgo(i));
            uvs.add(resourceUniqueVisitRepository.save(resourceUniqueVisitor));
            pvs.add(resourceViewRepository.save(resourceView));
        }
    }

    @Test
    public void test_increaseUniqueVisit() {
        assertTrue(cache.get(CACHE_SITE_PV, SITE_PV).equals(42));
        assertTrue(redisTemplate.opsForHyperLogLog().size(CACHE_SITE_UV).compareTo(28L) <= 0);
    }

    @Test
    public void test_increasePostVisitCount() {
        mockPostIds.forEach(postId -> {
            if (postId % 2 == 0) {
                assertTrue(cache.get(CACHE_POST_PV, "post_" + postId).equals(2));
            } else {
                assertTrue(cache.get(CACHE_POST_PV, "post_" + postId).equals(1));
            }
        });

        mockPostIds.forEach(postId -> {
            assertTrue(cache.get(CACHE_POST_UV, "post_" + postId).equals(1));
        });
        assertTrue(cache.get(CACHE_POST_UV, "post_1").equals(1));
    }

    @Test
    public void test_saveCacheDataToDB() {
        countingService.saveCacheDataToDB();
    }

    @Test
    public void test_listSiteUniqueVisitorDataForTheLast15Days() {
        assertEquals(15, countingService.listSiteUniqueVisitorDataForTheLast15Days().size());
    }

    public void test_listSiteViewDataForTheLast15Days() {
        assertEquals(15, countingService.listSiteViewDataForTheLast15Days().size());
    }

    @After
    public void finalize() {
        logger.info("finalize");
        resetData();
    }

    private void resetData() {

        redisTemplate.delete(CACHE_SITE_UV);
        redisTemplate.delete(CACHE_SITE_PV);
        redisTemplate.delete(CACHE_POST_ID_LIST);
        redisTemplate.delete(CACHE_POST_PV);
        redisTemplate.delete(CACHE_POST_UV);

        mockSessionIds.forEach(id -> {
            redisTemplate.delete(id);
        });

        resourceUniqueVisitRepository.deleteAll();
        resourceViewRepository.deleteAll();

    }

    private String getRandomSessionId() {
        return RandomStringUtils.randomAscii(MOCK_DEFAULT_SESSION_ID_LENGTH);
    }

    private List<String> getRandomSessionIdList(int numbs) {
        List<String> ids = new ArrayList<>(numbs);
        for (int i = 0; i < numbs; i ++) {
            String sessionId = getRandomSessionId();
            logger.info("SessionID: {} .", sessionId);
            ids.add(sessionId);
        }
        return ids;
    }

}