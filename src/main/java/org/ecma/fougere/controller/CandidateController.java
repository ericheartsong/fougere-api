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
import org.ecma.fougere.domain.Candidate;
import org.ecma.fougere.service.CandidateService;
import org.jboss.logging.Logger;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@Path("/candidate")
public class CandidateController {
    private final CandidateService service;

    @Inject
    JsonWebToken jwt;

    private static final Logger LOG = Logger.getLogger(CandidateController.class);

    public CandidateController(CandidateService service) {
        this.service = service;
    }

    @GET
    @Path("/uuid/{uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    @RunOnVirtualThread
    @RolesAllowed("read:core-entities")

    public Response getByUiid(@PathParam("uuid") String uuid) {
        LOG.info(Objects.toString(jwt.getSubject(), "unknown user")
                .concat(" : call getByUiid -")
                .concat (Objects.toString(uuid, "")));
        return service.getCandidateByUuid(uuid)
                .map(candidate -> Response.ok(candidate).build()) // Si présent : 200 OK avec l'objet
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
        return service.getCandidateIndicators()
                .map(domainIndicator -> Response.ok(domainIndicator).build()) // Si présent : 200 OK avec l'objet
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
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
            throw new BadRequestException("CANDIDATE-1000");
        }
        return service.getCandidateById(new ObjectId(id))
                .map(candidate -> Response.ok(candidate).build()) // Si présent : 200 OK avec l'objet
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RunOnVirtualThread
    @RolesAllowed("read:core-entities")
    public List<Candidate> getCandidates(@QueryParam("name") String name) {
        LOG.info(Objects.toString(jwt.getSubject(), "unknown user")
                .concat(" : call getCandidates -")
                .concat (Objects.toString(name, "")));
        if (name != null && !name.trim().isEmpty()) {
            return service.getFiltered(name);
        } else {
            // Panache fournit la méthode listAll() automatiquement
            return service.getAll();
        }
    }


    @GET
    @Path("/info/{uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    @RunOnVirtualThread
    @RolesAllowed("read:core-entities")
    public Response getCandidateInfo(@PathParam("uuid") String uuid){
        LOG.info(Objects.toString(jwt.getSubject(), "unknown user")
                .concat(" : call getCandidateInfo -")
                .concat (Objects.toString(uuid, "")));
        try {
            return service.getCandidateInfoByUuid(uuid)
                    .map(candidate -> Response.ok(candidate).build()) // Si présent : 200 OK avec l'objet
                    .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());

        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @RunOnVirtualThread
    @RolesAllowed("write:core-entities")
    public Response createCandidate (@Valid Candidate candidate, @HeaderParam("X-WS-CONN-ID") String connexionId) {
        LOG.info(Objects.toString(jwt.getSubject(), "unknown user")
                .concat(" : call createCandidate -")
                .concat (Objects.toString(candidate.getUuid(), "")));
        return service.createCandidate(candidate, connexionId)
                .map(createdObject -> Response.status(Response.Status.CREATED).entity(createdObject).build())
                .orElseGet(() -> Response.status(Response.Status.BAD_REQUEST).build());
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RunOnVirtualThread
    @RolesAllowed("write:core-entities")
    public Response updateCandidate (@PathParam("id") String id, @Valid Candidate candidate, @HeaderParam("X-WS-CONN-ID") String connexionId) {
        LOG.info(Objects.toString(jwt.getSubject(), "unknown user")
                .concat(" : call createCandidate -")
                .concat (Objects.toString(id, "")));
        if (!ObjectId.isValid(id)) {
            throw new BadRequestException("CANDIDATE-1000");
        }

        candidate.id = new ObjectId(id);
        return service.updateCandidate(candidate, connexionId)
                .map(updatedObject -> Response.ok(updatedObject).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }
}
