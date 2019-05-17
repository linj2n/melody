package cn.linj2n.melody.service;

import cn.linj2n.melody.domain.Option;

import java.util.List;

public interface ConfigService {
    public List<Option> listAllOptions();

    public List<Option> updateOptions(List<Option> options);
}
