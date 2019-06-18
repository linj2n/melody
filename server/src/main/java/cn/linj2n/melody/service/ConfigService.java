package cn.linj2n.melody.service;

import cn.linj2n.melody.domain.Option;

import java.util.List;
import java.util.Map;

public interface ConfigService {
    List<Option> listAllOptions();

    Map<String, String> fecthAllOptionMap();

    List<Option> updateOptions(List<Option> options);

    void updateOptions(Map<String, String> optionMap);
}

