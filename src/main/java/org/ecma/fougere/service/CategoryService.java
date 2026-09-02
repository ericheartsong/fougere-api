package org.ecma.fougere.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.ecma.fougere.domain.Category;

@ApplicationScoped
public class CategoryService {

    public Category getCategory() {
        Category cat = new Category();
        cat.setCode("AMOV");
        cat.setName("Adult movie3");
        cat.setDescription("Adult movie catgory");
        return cat;
    }
}
