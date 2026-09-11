package org.ecma.fougere.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;
import org.ecma.fougere.domain.Artwork;
import org.ecma.fougere.domain.DomainIndicators;
import org.ecma.fougere.notifier.NotificationMessage;
import org.ecma.fougere.notifier.NotifierWebSocket;
import org.ecma.fougere.service.ArtworkService;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ArtworkServiceImpl implements ArtworkService {

    @Inject
    NotifierWebSocket notifierService;

    @Override
    public Optional<Artwork> getArtworkById(ObjectId id) {
        return Artwork.findByIdOptional(id);
    }

    @Override
    public List<Artwork> getAll() {
        return Artwork.listAll();
    }

    @Override
    public List<Artwork> getFiltered(String name) {
        String regexPattern = "(?i).*" + name + ".*";
        return Artwork.list("name", java.util.regex.Pattern.compile(regexPattern));
    }

    @Override
    public Optional<DomainIndicators> getArtworkIndicators() {
        DomainIndicators ind = new DomainIndicators();
        ind.setNbElements(Artwork.count());
        return Optional.of(ind);
    }

    @Override
    public Optional<Artwork> createArtwork(Artwork artwork, String connectionId) {
        artwork.setName(artwork.getName().trim());
        if (artwork.id != null) {
            return Optional.empty();
        }
        artwork.persist();

        NotificationMessage notification =  new NotificationMessage(
                NotificationMessage.codeTypeMessage.ARTWORK_CREATED.name(),
                connectionId,
                artwork.id.toHexString(),
                artwork.getName()
                );

        // Reactive call to avoid latency
        notifierService.Notifier(notification, connectionId).await().indefinitely();


        return Optional.of(artwork);
    }

    @Override
    public Optional<Artwork> updateArtwork(Artwork artwork, String connectionId) {
        if (ObjectId.isValid(artwork.id.toHexString())) {
            Artwork artworkExist = Artwork.findById(artwork.id);
            if (artworkExist != null) {
                artwork.update();

                NotificationMessage notification =  new NotificationMessage(
                        NotificationMessage.codeTypeMessage.ARTWORK_UPDATED.name(),
                        connectionId,
                        artwork.id.toHexString(),
                        artwork.getName()
                );
                notifierService.Notifier(notification, connectionId).await().indefinitely();

                return Optional.of(artwork);
            } else  {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

}
