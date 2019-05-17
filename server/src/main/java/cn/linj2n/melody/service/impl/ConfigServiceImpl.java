package cn.linj2n.melody.service.impl;

import cn.linj2n.melody.domain.Option;
import cn.linj2n.melody.repository.OptionRepository;
import cn.linj2n.melody.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class ConfigServiceImpl implements ConfigService {
    private OptionRepository optionRepository;

    @Autowired
    public ConfigServiceImpl(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    @Override
    public List<Option> listAllOptions() {
        return optionRepository.findAll();
    }

    @Override
    public List<Option> updateOptions(List<Option> options) {
        options.forEach(option -> {
            optionRepository.save(option);
        });
        return options;
    }
}
