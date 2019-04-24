package cn.linj2n.melody.repository.support;

import cn.linj2n.melody.domain.Post;
import cn.linj2n.melody.domain.enumeration.PostStatus;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class PostSpecification {
    public static Specification<Post> isPublished() {
        return new Specification<Post>() {
            @Override
            public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get("status").as(PostStatus.class), PostStatus.PUBLISHED);
            }
        };
    }
}
