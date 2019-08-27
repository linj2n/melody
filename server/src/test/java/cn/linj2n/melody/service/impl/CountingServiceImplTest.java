package cn.linj2n.melody.service.impl;

import cn.linj2n.melody.domain.traffic.ResourceUniqueVisitor;
import cn.linj2n.melody.domain.traffic.ResourceView;
import cn.linj2n.melody.repository.ResourceUniqueVisitRepository;
import cn.linj2n.melody.repository.ResourceViewRepository;
import cn.linj2n.melody.utils.DateUtil;
import cn.linj2n.melody.web.dto.TrafficEntryDTO;
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

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CountingServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(CountingServiceImplTest.class);

    private static final int MOCK_DEFAULT_SESSION_ID_LENGTH = 42;

    private static final long DEF_POST_ID = 1;

    private static final long MIN_VIEWS = 0;

    private static final long MIN_UV = 0;

    private static final long MAX_VIEWS = 10000;

    private static final long MAX_UV = 2500;

    private static final int DEF_LENGTH = 31;

    private List<Long> mockSiteUniqueVisitorNums = new ArrayList<>();

    private List<Long> mockSiteViewNums = new ArrayList<>();

    private List<ResourceUniqueVisitor> uvs = new ArrayList<>();

    private List<ResourceView> pvs = new ArrayList<>();

    private static final int DEF_SIZE = 28;

    private List<String> mockSessionIds = new ArrayList<>();

    private List<Long> mockPostIds = new ArrayList<>();

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
        resetData();
        logger.info("Init data set for testing.");

        mockSessionIds = getRandomSessionIdList(DEF_SIZE + 1);
        mockPostIds = LongStream.rangeClosed(1, DEF_SIZE).boxed().collect(Collectors.toList());
        mockSiteUniqueVisitorNums = new Random().longs(DEF_LENGTH, MIN_UV, MAX_UV).boxed().map(uv -> {
            mockSiteViewNums.add(uv + (long) (Math.random() * (MAX_VIEWS - uv)));
            return uv;
        }).collect(Collectors.toList());


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

        ZonedDateTime day = DateUtil.getStartOfYesterday().minusDays(1);
        for (int i = 0; i < DEF_LENGTH; i ++) {
            ResourceUniqueVisitor newUv = new ResourceUniqueVisitor(SITE_UV, day.minusDays(i), mockSiteUniqueVisitorNums.get(i), null);
            ResourceView newPv = new ResourceView(SITE_PV, day.minusDays(i), mockSiteViewNums.get(i), null);

            resourceViewRepository.save(newPv);
            resourceUniqueVisitRepository.save(newUv);
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
        return ;
    }


    @Test
    public void test_listTheNumberOfCommentsForTheLast15Days() {
        List<TrafficEntryDTO> result = countingService.listTheNumberOfCommentsForTheLast15Days();
        assertEquals(15, result.size());
    }

    @Test
    public void test_getTrafficDataForTheLast15Days() {
        List<TrafficEntryDTO> result = countingService.getTrafficDataForTheLast15Days();
        assertEquals(15, result.size());
    }

    @After
    public void finalize() {
        logger.info("finalize");
//        resetData();
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