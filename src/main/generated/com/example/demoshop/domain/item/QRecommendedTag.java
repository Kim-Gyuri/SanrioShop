package com.example.demoshop.domain.item;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecommendedTag is a Querydsl query type for RecommendedTag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecommendedTag extends EntityPathBase<RecommendedTag> {

    private static final long serialVersionUID = 1837700020L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecommendedTag recommendedTag = new QRecommendedTag("recommendedTag");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QItem item;

    public final NumberPath<Long> tagFrequency = createNumber("tagFrequency", Long.class);

    public final EnumPath<com.example.demoshop.domain.item.common.TagOption> tagOption = createEnum("tagOption", com.example.demoshop.domain.item.common.TagOption.class);

    public QRecommendedTag(String variable) {
        this(RecommendedTag.class, forVariable(variable), INITS);
    }

    public QRecommendedTag(Path<? extends RecommendedTag> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecommendedTag(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecommendedTag(PathMetadata metadata, PathInits inits) {
        this(RecommendedTag.class, metadata, inits);
    }

    public QRecommendedTag(Class<? extends RecommendedTag> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.item = inits.isInitialized("item") ? new QItem(forProperty("item"), inits.get("item")) : null;
    }

}

