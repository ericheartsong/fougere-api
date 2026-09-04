package org.ecma.fougere.service;

import org.bson.types.ObjectId;
import org.ecma.fougere.domain.Artwork;
import org.ecma.fougere.domain.Candidate;
import org.ecma.fougere.domain.Category;

import java.util.List;
import java.util.Optional;

public interface ArtworkService {

    Optional<Artwork> createArtwork(Artwork artwork);

    Optional<Artwork> updateArtwork(Artwork artwork);

    Optional<Artwork> getArtworkById(ObjectId id);

    List<Artwork> getAll();

    List<Artwork> getFiltered(String name);
}
