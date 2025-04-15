package com.example.demoshop.domain.item.common;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Setting(settingPath = "elasticsearch/mappings/es-item-settings.json")
@Mapping(mappingPath = "elasticsearch/mappings/es-item-mapping.json")
@Document(indexName = "itemSearch")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ItemSearch {

    @Id
    private String id;

    @Field(name = "item_name", type = FieldType.Text)
    private String itemName;

    @Field(name = "description", type = FieldType.Text)
    private String description;

    @Builder
    public ItemSearch(String itemName, String description) {
        this.itemName = itemName;
        this.description = description;
    }
}
