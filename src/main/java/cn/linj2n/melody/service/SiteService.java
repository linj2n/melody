package cn.linj2n.melody.service;

import cn.linj2n.melody.web.dto.Archive;

import java.util.List;
import java.util.Map;

public interface SiteService {

    List<Archive> listArchives();

    Map<String, List<Archive>> getArchivesGroupByYear();

    Map<String, List<Archive>> getArchivesByTagId(Long tagId);
}