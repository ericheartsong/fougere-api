package org.ecma.fougere.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import org.bson.types.ObjectId;
import org.ecma.fougere.domain.Artwork;
import org.ecma.fougere.domain.Candidate;
import org.ecma.fougere.service.ArtworkService;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ArtworkServiceImpl implements ArtworkService {

    @Override
    public Optional<Artwork> getArtworkById(ObjectId id) {
        return Artwork.findByIdOptional(id);
    }

    @Override
    public List<Artwork> getAll() {
        return Artwork.listAll();
    }

    @Override
    public Optional<Artwork> createArtwork(Artwork artwork) {
        if (artwork.id != null) {
            return Optional.empty();
        }
        artwork.persist();
        return Optional.of(artwork);
    }

    @Override
    public Optional<Artwork> updateArtwork(Artwork artwork) {
        if (ObjectId.isValid(artwork.id.toHexString())) {
            Artwork artworkExist = Artwork.findById(artwork.id);
            if (artworkExist != null) {
                artwork.update();
                return Optional.of(artwork);
            } else  {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

}
