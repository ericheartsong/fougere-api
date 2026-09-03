package org.ecma.fougere.domain;

import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import org.bson.codecs.pojo.annotations.BsonId;

@MongoEntity(collection = "categorie")
public class Category extends PanacheMongoEntity {
    String code;
    String name;
    String description;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
