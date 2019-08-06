package cn.linj2n.melody.service;

public interface CountingService {

    void increasePostVisitCount(String visitorId, Long postId);

    void increaseSiteVisitCount(String visitorId);
}
