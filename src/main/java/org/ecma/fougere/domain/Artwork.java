package org.ecma.fougere.domain;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

@MongoEntity(collection = "artwork")
public class Artwork extends PanacheMongoEntity {

    @NotBlank(message = "ARTWORK-0001")
    @Size(max = 100, message = "ARTWORK-0130")
    String name;

    @Size(max = 100, message = "ARTWORK-0130")
    String subtitle;

    @Size(max = 200, message = "ARTWORK-0120")
    String urlImage;

    @Size(max = 500, message = "ARTWORK-0040")
    String description;


    @NotBlank(message = "ARTWORK-0001")
    @Size(max = 100, message = "ARTWORK-0100")
    String urlDescription;

    @NotBlank(message = "ARTWORK-0001")
    @Size(max = 200, message = "ARTWORK-0120")
    @URL(message = "ARTWORK-0030")
    String url;

    @SuppressWarnings("unused")
    public String getUrlDescription() {
        return urlDescription;
    }
    @SuppressWarnings("unused")
    public void setUrlDescription(String urlDescription) {
        this.urlDescription = urlDescription;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }
    public void setName(String userName) {
        this.name = userName;
    }

    public String getSubtitle() {
        return subtitle;
    }
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
