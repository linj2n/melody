package cn.linj2n.melody.service.impl;

import cn.linj2n.melody.domain.Post;
import cn.linj2n.melody.domain.webdataanalysis.ResourceUniqueVisitor;
import cn.linj2n.melody.domain.webdataanalysis.ResourceView;
import cn.linj2n.melody.repository.ResourceUniqueVisitRepository;
import cn.linj2n.melody.repository.ResourceViewRepository;
import cn.linj2n.melody.service.CountingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class CountingServiceImpl implements CountingService {

    private static final Logger logger = LoggerFactory.getLogger(CountingServiceImpl.class);

    private static final int DEFAULT_EXPIRY_HOUR = 3;

    private static final int DEFAULT_EXPIRY_MIN = 0;

    private static final String POST_ = "post_";

    @Autowired
    private ResourceViewRepository resourceViewRepository;

    @Autowired
    private ResourceUniqueVisitRepository resourceUniqueVisitRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource(name = "redisCountingTemplate")
    private HashOperations<String, String, Integer> cache;

    @Resource(name = "redisListTemplate")
    private SetOperations<String, Long> cacheSet;


    private static final String CACHE_POST_PV = "counting.post.pv";

    private static final String CACHE_POST_UV = "counting.post.uv";

    private static final String CACHE_SITE_PV = "counting.site";

    private static final String CACHE_SITE_UV = "visitorIds";

    private static final String SITE_PV = "site_pv";

    private static final String SITE_UV = "site_uv";

    private static final String CACHE_POST_ID_LIST = "counting.postIds";


    @Override
    public void increasePostVisitCount(String visitorId, Long postId) {
        cacheSet.add(CACHE_POST_ID_LIST, postId);
        if (!isVisited(visitorId, postId)) {
            cache.increment(CACHE_POST_UV, POST_ + postId, 1L);
            markVisitedTag(visitorId, postId);
        }
        cache.increment(CACHE_POST_PV, POST_ + postId, 1L);
    }

    @Override
    public void increaseSiteVisitCount(String visitorId) {
        redisTemplate.opsForHyperLogLog().add(CACHE_SITE_UV, visitorId);
        redisTemplate.expire(CACHE_SITE_UV, getExpireTime(), TimeUnit.SECONDS);
        cache.increment(CACHE_SITE_PV, SITE_PV, 1L);
    }

    private boolean isVisited(String sessionId, Long postId) {
        return redisTemplate.opsForValue().getBit(sessionId, postId);
    }

    private void markVisitedTag(String visitorId, Long postId) {
        redisTemplate.opsForValue().setBit(visitorId, postId, true);
        redisTemplate.expire(visitorId, getExpireTime(), TimeUnit.SECONDS);
    }

    private long getExpireTime() {
        LocalDate tomorrowDate = LocalDateTime.now().toLocalDate().plusDays(1L);
        LocalTime expiryTime = LocalTime.of(DEFAULT_EXPIRY_HOUR, DEFAULT_EXPIRY_MIN);
        return LocalDateTime.of(tomorrowDate, expiryTime).atZone(ZoneId.systemDefault()).toEpochSecond();
    }

    private int getSitePvCount() {
        return cache.get(CACHE_SITE_PV, SITE_PV);
    }

    private long getSiteUvCount() {
        return redisTemplate.opsForHyperLogLog().size(CACHE_SITE_UV);
    }

    private int getPostUvCount(long postId) {
        return cache.get(CACHE_POST_UV, POST_ + postId);
    }

    private int getPostPvCount(long postId) {
        return cache.get(CACHE_POST_PV, POST_ + postId);
    }

//    @Scheduled(cron = "0 0 3 * * *")
//    @Scheduled()
    public void saveDataFromCache() {
        logger.debug("Scheduled to save counting information." );
        int idCount = cacheSet.size(CACHE_POST_ID_LIST).intValue();

        List<ResourceUniqueVisitor> uvs = new ArrayList<>(idCount);
        List<ResourceView> pvs = new ArrayList<>(idCount);

        // Add site uv and pv
        uvs.add(createNewUV(SITE_UV, getSitePvCount(), null));
        pvs.add(createNewPV(SITE_PV, getSiteUvCount(), null));

        // Add post uv and pv
        List<Long> postIds = new ArrayList<>(idCount);
        for (int i = 0; i < idCount; i ++) {
            Long postId = cacheSet.pop(CACHE_POST_ID_LIST);
            postIds.add(postId);
            uvs.add(createNewUV(POST_ + postId, getPostUvCount(postId), postId));
            pvs.add(createNewPV(POST_ + postId, getPostPvCount(postId), postId));
        }

        resourceViewRepository.save(pvs);
        resourceUniqueVisitRepository.save(uvs);

        resetUvAndPvCache(postIds);
    }

    private void resetUvAndPvCache(List<Long> postIds) {
        postIds.forEach(postId -> {
            cache.put(CACHE_POST_UV, POST_ + postId, 0);
            cache.put(CACHE_POST_PV, POST_ + postId, 0);
        });
    }

    private ResourceUniqueVisitor createNewUV(String name, long count, Long postId) {
        Post post = new Post();
        post.setId(postId);
        return new ResourceUniqueVisitor(name, count, post, getYesterdayDate());
    }

    private ResourceView createNewPV(String name, long count, Long postId) {
        Post post = new Post();
        post.setId(postId);
        name += getYesterdayDateString();
        return new ResourceView(name, count, post, getYesterdayDate());
    }

    private Date getYesterdayDate() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    private String getYesterdayDateString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(getYesterdayDate());
    }

}
