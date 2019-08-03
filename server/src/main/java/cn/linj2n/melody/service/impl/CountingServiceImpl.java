package cn.linj2n.melody.service.impl;

import cn.linj2n.melody.service.CountingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.time.ZonedDateTime;

public class CountingServiceImpl implements CountingService {

    private static final int DEFAULT_DAY_OFFSET = 1;

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource(name = "redisCountingTemplate")
    HashOperations<String, String, Integer> counting;

    private static final String PV_HOST = "counting.pv.host";

    private static final String PV_POSTS = "counting.pv.posts";

    private static final String UV_HOST = "counting.uv.host";

    private static final String UV_POSTS = "counting.uv.post";
    

    @Override
    public void increaseUniqueVisit(String sessionId, Long postId) {

    }

    @Override
    public void increasePageView(Long postId) {

    }

    @Override
    public void increaseHostUniqueVisit() {

    }
}
