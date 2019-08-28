package cn.linj2n.melody.service;

import cn.linj2n.melody.web.dto.TrafficEntryDTO;

import java.util.List;

public interface CountingService {

    void increasePostVisitCount(String visitorId, Long postId);

    void increaseSiteVisitCount(String visitorId);

    List<TrafficEntryDTO> listTheNumberOfCommentsForTheLast15Days();

    List<TrafficEntryDTO> getTrafficDataForTheLast15Days();

    Long getSiteTotalViews();

    Long getPostTotalNumber();

    Long getCommentTotalNumber();
}
