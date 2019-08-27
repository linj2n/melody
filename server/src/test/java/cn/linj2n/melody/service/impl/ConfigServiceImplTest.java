package cn.linj2n.melody.service.impl;

import cn.linj2n.melody.domain.Option;
import cn.linj2n.melody.repository.OptionRepository;
import cn.linj2n.melody.service.ConfigService;
import org.apache.commons.lang.RandomStringUtils;
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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConfigServiceImplTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigServiceImplTest.class);

    private static final String MOCK_VALUE = RandomStringUtils.randomAscii(11);

    private static final String MOCK_OPTION_NAME = "siteDescription";

    private static final List<String> MOCK_OPTION_NAME_LIST = Stream.of("siteDescription", "siteSubtitle").collect(Collectors.toList());

    private static final String CACHE_OPTIONS = "cache.options";

    private List<Option> options = new ArrayList<>();

    @Autowired
    private RedisTemplate redisTemplate;


    @Resource(name = "redisOptionTemplate")
    private HashOperations<String, String, String> cache;

    @Autowired
    private ConfigService configService;

    @Before
    public void prepare_env() {
        redisTemplate.delete(CACHE_OPTIONS);
        assertTrue(cache.entries(CACHE_OPTIONS).isEmpty());

    }

    @Test
    public void test_listAllOptions() {
        configService.listAllOptions();
        assertTrue(!configService.listAllOptions().isEmpty());
        assertTrue(!cache.entries(CACHE_OPTIONS).isEmpty());
    }

    @Test
    public void test_updateOptions() {
        options = configService.listAllOptions();
        Option siteDescriptionOpt = options.stream().filter(option -> option.getName().equals(MOCK_OPTION_NAME)).findFirst().orElse(null);
        siteDescriptionOpt.setValue(MOCK_VALUE);
        assertTrue(options.contains(siteDescriptionOpt));

        configService.updateOptions(options);

        assertTrue(configService.listAllOptions().stream().anyMatch(option -> option.getName().equals(MOCK_OPTION_NAME) && option.getValue().equals(MOCK_VALUE)));
    }

    @Test
    public void test_fetchAllOptionMap() {
        assertTrue(!configService.fetchAllOptionMap().isEmpty());
    }

    @Test
    public void test_fetchOptionMap() {
        Map<String, String> res = configService.fetchOptionMap(MOCK_OPTION_NAME_LIST);
        assertNotNull(res);
        assertEquals(MOCK_OPTION_NAME_LIST.size(), res.size());
        res.forEach((key, value) -> assertTrue(!value.isEmpty()));
    }
}