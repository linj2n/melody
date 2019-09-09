package cn.linj2n.melody.service.impl;

import cn.linj2n.melody.domain.Post;
import cn.linj2n.melody.domain.traffic.ResourceUniqueVisitor;
import cn.linj2n.melody.domain.traffic.ResourceView;
import cn.linj2n.melody.repository.CommentRepository;
import cn.linj2n.melody.repository.PostRepository;
import cn.linj2n.melody.repository.ResourceUniqueVisitRepository;
import cn.linj2n.melody.repository.ResourceViewRepository;
import cn.linj2n.melody.service.CountingService;
import cn.linj2n.melody.utils.DateUtil;
import cn.linj2n.melody.web.dto.TrafficEntryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CountingServiceImpl implements CountingService {

    private static final Logger logger = LoggerFactory.getLogger(CountingServiceImpl.class);

    private static final int DEFAULT_EXPIRY_HOUR = 3;

    private static final int DEFAULT_EXPIRY_MIN = 0;

    private static final int NUMBER_OF_DAYS = 15;

    private static final String POST_ = "post_";

    @Autowired
    private ResourceViewRepository resourceViewRepository;

    @Autowired
    private ResourceUniqueVisitRepository resourceUniqueVisitRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource(name = "redisCountingTemplate")
    private HashOperations<String, String, Integer> cache;

    @Resource(name = "redisListTemplate")
    private SetOperations<String, Long> cacheSet;

    private static final String CACHE_POST_PV = "counting.post.pv";

    private static final String CACHE_POST_UV = "counting.post.uv";

    private static final String CACHE_SITE_PV = "counting.site.pv";

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

    @Override
    public List<TrafficEntryDTO> listTheNumberOfCommentsForTheLast15Days() {
        return Stream.iterate(DateUtil.nowLocalDate(), day -> day.minusDays(1))
                .limit(NUMBER_OF_DAYS)
                .map(localDate -> new TrafficEntryDTO(localDate, getTheNumberOfComments(localDate)))
                .collect(Collectors.toList());
    }

    @Override
    public List<TrafficEntryDTO> getTrafficDataForTheLast15Days() {
        Map<LocalDate, Long> pvNumsMap = resourceViewRepository
                .findTop15ByNameOrderByDateDesc(SITE_PV)
                .stream()
                .collect(Collectors.toMap(pv -> pv.getDate().toLocalDate(), ResourceView::getCount));

        Map<LocalDate, Long> uvNumsMap = resourceUniqueVisitRepository
                .findTop15ByNameOrderByDateDesc(SITE_UV)
                .stream()
                .collect(Collectors.toMap(uv -> uv.getDate().toLocalDate(), ResourceUniqueVisitor::getCount));

        return Stream.iterate(DateUtil.nowLocalDate(), day -> day.minusDays(1))
                .limit(NUMBER_OF_DAYS)
                .map(localDate -> new TrafficEntryDTO(localDate, pvNumsMap.getOrDefault(localDate, 0L), uvNumsMap.getOrDefault(localDate, 0L)))
                .collect(Collectors.toList());
    }

    private Long getTheNumberOfComments(LocalDate localDate) {
        return commentRepository.countByCreatedAtBetween(DateUtil.getStartOfDay(localDate), DateUtil.getEndOfDay(localDate));
    }

    @Override
    public Long getSiteTotalViews() {
        return resourceViewRepository.getSiteTotalViews();
    }

    @Override
    public Long getPostTotalNumber() {
        return postRepository.count();
    }

    @Override
    public Long getCommentTotalNumber() {
        return commentRepository.count();
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

    private Optional<Integer> getSitePvCount() {
        return Optional.of(cache.get(CACHE_SITE_PV, SITE_PV));
    }

    private Optional<Long> getSiteUvCount() {
        return Optional.of(redisTemplate.opsForHyperLogLog().size(CACHE_SITE_UV));
    }

    private int getPostUvCount(long postId) {
        return cache.get(CACHE_POST_UV, POST_ + postId);
    }

    private int getPostPvCount(long postId) {
        return cache.get(CACHE_POST_PV, POST_ + postId);
    }

    @Scheduled(cron = "0 0 3 * * *")
    public void saveCacheDataToDB() {
        logger.debug("Scheduled to save counting information." );
        int idCount = cacheSet.size(CACHE_POST_ID_LIST).intValue();

        List<ResourceUniqueVisitor> uvs = new ArrayList<>(idCount);
        List<ResourceView> pvs = new ArrayList<>(idCount);

        // Add site uv and pv
        uvs.add(createNewUV(SITE_UV, getSiteUvCount().orElse(0L), null, DateUtil.getStartOfYesterday()));
        pvs.add(createNewPV(SITE_PV, getSitePvCount().orElse(0), null, DateUtil.getStartOfYesterday()));

        // Add post uv and pv
        List<Long> postIds = new ArrayList<>(idCount);
        for (int i = 0; i < idCount; i ++) {
            Long postId = cacheSet.pop(CACHE_POST_ID_LIST);
            postIds.add(postId);
            uvs.add(createNewUV(POST_ + postId, getPostUvCount(postId), postId, DateUtil.getStartOfYesterday()));
            pvs.add(createNewPV(POST_ + postId, getPostPvCount(postId), postId, DateUtil.getStartOfYesterday()));
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
        redisTemplate.opsForHyperLogLog().delete(CACHE_SITE_UV);
        cache.delete(CACHE_SITE_PV, SITE_PV);
    }

    private ResourceUniqueVisitor createNewUV(String name, long count, Long postId, ZonedDateTime date) {
        ResourceUniqueVisitor newUv = new ResourceUniqueVisitor(name, date, count);
        if (postId != null) {
            Post post = new Post();
            post.setId(postId);
            newUv.setPost(post);
        }
        return newUv;
    }

    private ResourceView createNewPV(String name, long count, Long postId, ZonedDateTime date) {
        ResourceView newPv = new ResourceView(name, date, count);
        if (postId != null) {
            Post post = new Post();
            post.setId(postId);
            newPv.setPost(post);
        }
        return newPv;
    }

}
