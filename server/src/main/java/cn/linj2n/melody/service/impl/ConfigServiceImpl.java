package cn.linj2n.melody.service.impl;

import cn.linj2n.melody.domain.Option;
import cn.linj2n.melody.repository.OptionRepository;
import cn.linj2n.melody.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConfigServiceImpl implements ConfigService {

    private static final String CACHE_OPTIONS = "cache.options";

    @Resource(name = "redisOptionTemplate")
    private HashOperations<String, String, String> cache;

    private OptionRepository optionRepository;

    @Autowired
    public ConfigServiceImpl(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    @Override
    public List<Option> listAllOptions() {
        if (cache.entries(CACHE_OPTIONS).isEmpty()) {
           loadAllOptions();
        }
        return cache.entries(CACHE_OPTIONS).entrySet().stream().map(entry -> new Option(entry.getKey(), entry.getValue())).collect(Collectors.toList());
    }

    private void loadAllOptions() {
        optionRepository.findAll()
                .forEach(option -> {
                    cache.put(CACHE_OPTIONS, option.getName(), option.getValue());
                });
    }

    @Override
    public Map<String, String> fetchAllOptionMap() {
//        return optionRepository
//                .findAll()
//                .stream()
//                .collect(Collectors.toMap(Option::getName, Option::getValue));
        if (cache.entries(CACHE_OPTIONS).isEmpty()) {
            loadAllOptions();
        }
        return cache.entries(CACHE_OPTIONS);
    }

    @Override
    public Map<String, String> fetchOptionMap(List<String> optionNames) {
        return fetchAllOptionMap()
                .entrySet()
                .stream()
                .filter(entry -> optionNames.contains(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public List<Option> updateOptions(List<Option> options) {
        optionRepository.save(options);
        loadAllOptions();
        return options;
    }

    @Override
    public void updateOptions(Map<String, String> optionMap) {
        optionMap.forEach((name, value)-> {
            optionRepository.save(new Option(name, value));
        });
        loadAllOptions();
    }
}
