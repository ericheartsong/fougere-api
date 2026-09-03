package org.ecma.fougere.controller;

import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;
import org.ecma.fougere.domain.Artwork;
import org.ecma.fougere.service.ArtworkService;

import java.util.List;

@Path("/artwork")
public class ArtworkController {
    private final ArtworkService service;

    public ArtworkController(ArtworkService service) {
        this.service = service;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") String id) {
        if (!ObjectId.isValid(id)) {
            throw new BadRequestException("ARTWORK-1000");
        }
        return service.getArtworkById(new ObjectId(id))
                .map(artwork -> Response.ok(artwork).build()) // Si présent : 200 OK avec l'objet
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Artwork> getAll() {
        // Panache fournit la méthode listAll() automatiquement
        return service.getAll();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createArtwork (@Valid Artwork artwork) {
        return service.createArtwork(artwork)
                .map(createdObject -> Response.status(Response.Status.CREATED).entity(createdObject).build())
                .orElseGet(() -> Response.status(Response.Status.BAD_REQUEST).build());
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateArtwork (@PathParam("id") String id, @Valid Artwork artwork) {

        if (!ObjectId.isValid(id)) {
            throw new BadRequestException("ARTWORK-1000");
        }

        artwork.id = new ObjectId(id);
        return service.updateArtwork(artwork)
                .map(updatedObject -> Response.ok(updatedObject).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }
}
