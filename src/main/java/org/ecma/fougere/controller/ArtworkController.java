package org.ecma.fougere.controller;

import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.ecma.fougere.domain.Artwork;
import org.ecma.fougere.service.ArtworkService;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Objects;

@Path("/artwork")
public class ArtworkController {
    private final ArtworkService service;

    @Inject
    JsonWebToken jwt;

    private static final Logger LOG = Logger.getLogger(ArtworkController.class);

    public ArtworkController(ArtworkService service) {
        this.service = service;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RunOnVirtualThread
    @RolesAllowed("read:core-entities")
    public Response getById(@PathParam("id") String id) {
        LOG.info(Objects.toString(jwt.getSubject(), "unknown user")
                .concat(" : call getById -")
                .concat (Objects.toString(id, "")));
        if (!ObjectId.isValid(id)) {
            throw new BadRequestException("ARTWORK-1000");
        }
        return service.getArtworkById(new ObjectId(id))
                .map(artwork -> Response.ok(artwork).build()) // Si présent : 200 OK avec l'objet
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RunOnVirtualThread
    @RolesAllowed("read:core-entities")
    public List<Artwork> getArtworks(@QueryParam("name") String name) {
        LOG.info(Objects.toString(jwt.getSubject(), "unknown user")
                        .concat(" : call getArtworks")
                        .concat (Objects.toString(name, "")));
        if (name != null && !name.trim().isEmpty()) {
            return service.getFiltered(name);
        } else {
            // Panache fournit la méthode listAll() automatiquement
            return service.getAll();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @RunOnVirtualThread
    @RolesAllowed("write:core-entities")
    public Response createArtwork (@Valid Artwork artwork, @HeaderParam("X-WS-CONN-ID") String connexionId) {
        LOG.info(Objects.toString(jwt.getSubject(), "unknown user")
                .concat(" : call createArtwork -")
                .concat(Objects.toString(artwork.getName(), ""))
                .concat(Objects.toString(connexionId, "")));
        return service.createArtwork(artwork, connexionId)
                .map(createdObject -> Response.status(Response.Status.CREATED).entity(createdObject).build())
                .orElseGet(() -> Response.status(Response.Status.BAD_REQUEST).build());
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RunOnVirtualThread
    @RolesAllowed("write:core-entities")
    public Response updateArtwork (@PathParam("id") String id, @Valid Artwork artwork, @HeaderParam("X-WS-CONN-ID") String connexionId) {
        LOG.info(Objects.toString(jwt.getSubject(), "unknown user")
                .concat(" : call updateArtwork -")
                .concat(Objects.toString(id, ""))
                .concat(Objects.toString(connexionId, "")));
        if (!ObjectId.isValid(id)) {
            throw new BadRequestException("ARTWORK-1000");
        }

        artwork.id = new ObjectId(id);
        return service.updateArtwork(artwork,connexionId)
                .map(updatedObject -> Response.ok(updatedObject).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Path("/indicators")
    @Produces(MediaType.APPLICATION_JSON)
    @RunOnVirtualThread
    @RolesAllowed("read:core-entities")
    public Response getIndicators() {
        LOG.info(Objects.toString(jwt.getSubject(), "unknown user")
                .concat(" : call getIndicators -"));
        return service.getArtworkIndicators()
                .map(domainIndicator -> Response.ok(domainIndicator).build()) // Si présent : 200 OK avec l'objet
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }
}
