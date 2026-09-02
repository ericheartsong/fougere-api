package org.ecma.fougere.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.ecma.fougere.domain.Category;
import org.ecma.fougere.service.CategoryService;

import java.util.List;

@Path("/category") // Définition de la route de base de l'API
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    //@GET // Spécifie que cette méthode répond aux requêtes HTTP GET
    //@Produces(MediaType.APPLICATION_JSON)// Indique que le format de réponse est du texte brut
    //public Category hello() {
    //    return service.getCategory();
    //}

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Category> getAll() {
        // Panache fournit la méthode listAll() automatiquement
        return Category.listAll();
    }
}
