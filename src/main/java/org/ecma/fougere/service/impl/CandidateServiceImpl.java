package org.ecma.fougere.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;
import org.ecma.fougere.domain.AvatarInfo;
import org.ecma.fougere.domain.Candidate;
import org.ecma.fougere.domain.DomainIndicators;
import org.ecma.fougere.notifier.NotificationMessage;
import org.ecma.fougere.notifier.NotifierWebSocket;
import org.ecma.fougere.service.CandidateService;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
public class CandidateServiceImpl implements CandidateService {

    private static final String K_WORLD_API_SL = "https://world.secondlife.com/resident/";
    private static final String K_PREFIX_URL_PROFILE = "https://picture-service.secondlife.com/";
    private static final String K_SUFFIX_URL_PROFILE = "/320x240.jpg";

    @Inject
    NotifierWebSocket notifierService;

    @Override
    public Optional<Candidate> getCandidateByUuid(String uuid) {
        return Candidate.find("uuid", uuid).firstResultOptional();
    }


    @Override
    public Optional<Candidate> getCandidateById(ObjectId id) {
        return Candidate.findByIdOptional(id);
    }

    @Override
    public List<Candidate> getAll() {
        return Candidate.listAll();
    }

    @Override
    public List<Candidate> getFiltered(String name) {
        String regexPattern = "(?i).*" + name + ".*";
        return Candidate.list("name", java.util.regex.Pattern.compile(regexPattern));
    }

    @Override
    public Optional<AvatarInfo> getCandidateInfoByUuid(String uuid) throws Exception {
        AvatarInfo info = new AvatarInfo();

        info.setUuid(uuid);
        String body = callAPISL(uuid);

        // Récupération de l'url de profil
        Pattern pattern = Pattern.compile("<meta name=\"imageid\" content=\"([^\"]+)\">");
        Matcher matcher = pattern.matcher(body);

        if (matcher.find()) {
            info.setUrlProfile(K_PREFIX_URL_PROFILE.concat(matcher.group(1)).concat(K_SUFFIX_URL_PROFILE));
        } else {
            info.setUrlProfile(null);
        }

        // Récupération des username et display name
        Pattern pattern2 = Pattern.compile("<title>(.+?)\\s*\\((.+?)\\)</title>");
        Matcher matcher2 = pattern2.matcher(body);

        if (matcher2.find()) {
            // Le premier groupe (.+?) capture ce qui précède la parenthèse
            info.setDisplayName(matcher2.group(1).trim());
            // Le deuxième groupe (.+?) capture ce qui est dans la parenthèse
            info.setUsername(matcher2.group(2).trim());
        } else {

            Pattern pattern3 = Pattern.compile("<title>(.+?)</title>");
            Matcher matcher3 = pattern3.matcher(body);
            if (matcher3.find()) {
                // Le premier groupe (.+?) capture ce qui précède la parenthèse
                info.setDisplayName(matcher3.group(1).trim());
                // Le deuxième groupe (.+?) capture ce qui est dans la parenthèse
                info.setUsername(matcher3.group(1).trim());
            } else{
                info.setDisplayName(null);
                info.setUsername(null);
            }
        }

        return Optional.of(info);
    }

    @Override
    public Optional<DomainIndicators> getCandidateIndicators() {
        DomainIndicators ind = new DomainIndicators();
        ind.setNbElements(Candidate.count());
        return Optional.of(ind);
    }

    @Override
    public Optional<Candidate> createCandidate(Candidate candidate,String connectionId) {
        if (candidate.id != null) {
            return Optional.empty();
        }
        candidate.persist();

        NotificationMessage notification =  new NotificationMessage(
                NotificationMessage.codeTypeMessage.CANDIDATE_CREATED.name(),
                Objects.toString(connectionId,""),
                candidate.id.toHexString(),
                Objects.toString(candidate.getDisplayName(), ""));

        // Reactive call to avoid latency
        notifierService.Notifier(notification, connectionId).await().indefinitely();

        return Optional.of(candidate);
    }

    @Override
    public Optional<Candidate> updateCandidate(Candidate candidate, String connectionId) {
        if (ObjectId.isValid(candidate.id.toHexString())) {
            Candidate candidateExist = Candidate.findById(candidate.id);
            if (candidateExist != null) {
                candidate.update();

                NotificationMessage notification =  new NotificationMessage(
                        NotificationMessage.codeTypeMessage.CANDIDATE_UPDATED.name(),
                        Objects.toString(connectionId,""),
                        candidate.id.toHexString(),
                        Objects.toString(candidate.getDisplayName(), ""));

                // Reactive call to avoid latency
                notifierService.Notifier(notification, connectionId).await().indefinitely();

                return Optional.of(candidate);
            } else  {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    private String callAPISL(String uuid) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(K_WORLD_API_SL.concat(uuid)))
                    .GET()
                    .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

}
