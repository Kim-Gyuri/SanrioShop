package com.example.demoshop.domain.item;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserDefinedTag is a Querydsl query type for UserDefinedTag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserDefinedTag extends EntityPathBase<UserDefinedTag> {

    private static final long serialVersionUID = -1695935631L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserDefinedTag userDefinedTag = new QUserDefinedTag("userDefinedTag");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QItem item;

    public final StringPath name = createString("name");

    public final NumberPath<Long> tagFrequency = createNumber("tagFrequency", Long.class);

    public QUserDefinedTag(String variable) {
        this(UserDefinedTag.class, forVariable(variable), INITS);
    }

    public QUserDefinedTag(Path<? extends UserDefinedTag> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserDefinedTag(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserDefinedTag(PathMetadata metadata, PathInits inits) {
        this(UserDefinedTag.class, metadata, inits);
    }

    public QUserDefinedTag(Class<? extends UserDefinedTag> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.item = inits.isInitialized("item") ? new QItem(forProperty("item"), inits.get("item")) : null;
    }

}

