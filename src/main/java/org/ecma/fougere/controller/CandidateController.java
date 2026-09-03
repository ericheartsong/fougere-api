package org.ecma.fougere.controller;

import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;
import org.ecma.fougere.domain.Candidate;
import org.ecma.fougere.service.CandidateService;

import java.net.URI;
import java.util.List;

@Path("/candidate")
public class CandidateController {
    private final CandidateService service;

    public CandidateController(CandidateService service) {
        this.service = service;
    }

    @GET
    @Path("/uuid/{uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByUiid(@PathParam("uuid") String uuid) {
        return service.getCandidateByUuid(uuid)
                .map(candidate -> Response.ok(candidate).build()) // Si présent : 200 OK avec l'objet
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") String id) {
        if (!ObjectId.isValid(id)) {
            throw new BadRequestException("CANDIDATE-1000");
        }
        return service.getCandidateById(new ObjectId(id))
                .map(candidate -> Response.ok(candidate).build()) // Si présent : 200 OK avec l'objet
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Candidate> getAll() {
        // Panache fournit la méthode listAll() automatiquement
        return service.getAll();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCandidate (@Valid Candidate candidate) {
        return service.createCandidate(candidate)
                .map(createdObject -> Response.status(Response.Status.CREATED).entity(createdObject).build())
                .orElseGet(() -> Response.status(Response.Status.BAD_REQUEST).build());
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCandidate (@PathParam("id") String id, @Valid Candidate candidate) {

        if (!ObjectId.isValid(id)) {
            throw new BadRequestException("CANDIDATE-1000");
        }

        candidate.id = new ObjectId(id);
        return service.updateCandidate(candidate)
                .map(updatedObject -> Response.ok(updatedObject).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }
}
