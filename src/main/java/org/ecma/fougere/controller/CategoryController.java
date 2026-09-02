package org.ecma.fougere.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.ecma.fougere.domain.Category;
import org.ecma.fougere.service.CategoryService;
import org.ecma.fougere.service.impl.CategoryServiceImpl;

import java.util.List;
import java.util.Optional;

@Path("/category") // Définition de la route de base de l'API
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryServiceImpl service) {
        this.service = service;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") String idCategory) {
        return service.getCategory(idCategory)
                .map(category -> Response.ok(category).build()) // Si présent : 200 OK avec l'objet
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Category> getAll() {
        // Panache fournit la méthode listAll() automatiquement
        return service.getAll();
    }
}
