package com.example.demoshop.domain.item;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QItem is a Querydsl query type for Item
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItem extends EntityPathBase<Item> {

    private static final long serialVersionUID = -1571576376L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QItem item = new QItem("item");

    public final DateTimePath<java.time.LocalDateTime> createAt = createDateTime("createAt", java.time.LocalDateTime.class);

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<ItemImg, QItemImg> itemImgList = this.<ItemImg, QItemImg>createList("itemImgList", ItemImg.class, QItemImg.class, PathInits.DIRECT2);

    public final NumberPath<Integer> likeCount = createNumber("likeCount", Integer.class);

    public final EnumPath<com.example.demoshop.domain.item.common.MainCategory> mainCategory = createEnum("mainCategory", com.example.demoshop.domain.item.common.MainCategory.class);

    public final StringPath nameKor = createString("nameKor");

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final SetPath<RecommendedTag, QRecommendedTag> recommendedTagList = this.<RecommendedTag, QRecommendedTag>createSet("recommendedTagList", RecommendedTag.class, QRecommendedTag.class, PathInits.DIRECT2);

    public final EnumPath<com.example.demoshop.domain.item.common.SanrioCharacters> sanrioCharacters = createEnum("sanrioCharacters", com.example.demoshop.domain.item.common.SanrioCharacters.class);

    public final EnumPath<com.example.demoshop.domain.item.common.SubCategory> subCategory = createEnum("subCategory", com.example.demoshop.domain.item.common.SubCategory.class);

    public final com.example.demoshop.domain.users.user.QUser uploader;

    public final SetPath<UserDefinedTag, QUserDefinedTag> userDefinedTagList = this.<UserDefinedTag, QUserDefinedTag>createSet("userDefinedTagList", UserDefinedTag.class, QUserDefinedTag.class, PathInits.DIRECT2);

    public QItem(String variable) {
        this(Item.class, forVariable(variable), INITS);
    }

    public QItem(Path<? extends Item> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QItem(PathMetadata metadata, PathInits inits) {
        this(Item.class, metadata, inits);
    }

    public QItem(Class<? extends Item> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.uploader = inits.isInitialized("uploader") ? new com.example.demoshop.domain.users.user.QUser(forProperty("uploader"), inits.get("uploader")) : null;
    }

}

