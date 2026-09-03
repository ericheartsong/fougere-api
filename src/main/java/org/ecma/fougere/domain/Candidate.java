package org.ecma.fougere.domain;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.util.List;

@MongoEntity(collection = "candidate")
public class Candidate extends PanacheMongoEntity {

    public static class MediaLink {

        @NotBlank(message = "CANDIDATE-0001")
        @Size(max = 100, message = "CANDIDATE-0100")
        String description;

        @NotBlank(message = "CANDIDATE-0001")
        @Size(max = 200, message = "CANDIDATE-0120")
        @URL(message = "CANDIDATE-0030")
        String url;

        // Constructeur vide explicite requis par MongoDB
        public MediaLink() {}

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    @NotBlank(message = "CANDIDATE-0001")
    @Pattern(
            regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$",
            message = "CANDIDATE-0020"
    )
    String uuid;

    @NotBlank(message = "CANDIDATE-0001")
    @Pattern(regexp = "^[a-zA-Z0-9._\\s-]+$", message = "CANDIDATE-0020")
    @Size(max = 63, message = "CANDIDATE-0130")
    String username;

    @Size(max = 31, message = "CANDIDATE-0110")
    String displayName;

    @Size(min=0, max = 254, message = "CANDIDATE-0140")
    @Email(message = "CANDIDATE-0010")
    String email;

    @URL(message = "CANDIDATE-0030")
    @Size(min=0, max = 255, message = "CANDIDATE-0150")
    String urlProfile;

    @Size(max = 500, message = "CANDIDATE-0040")
    String biography;

    @Size(min = 0, max = 3, message = "CANDIDATE-0050")
    List<MediaLink> links;

    public List<MediaLink> getLinks() {
        return links;
    }
    public void setLinks(List<MediaLink> links) {
        this.links = links;
    }

    public String getBiography() {
        return biography;
    }
    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getUrlProfile() {
        return urlProfile;
    }
    public void setUrlProfile(String urlProfile) {
        this.urlProfile = urlProfile;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

}
