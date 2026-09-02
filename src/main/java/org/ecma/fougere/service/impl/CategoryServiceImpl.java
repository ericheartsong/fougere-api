package org.ecma.fougere.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import org.ecma.fougere.domain.Category;
import org.ecma.fougere.service.CategoryService;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CategoryServiceImpl implements CategoryService {

    public Optional<Category> getCategory(String idCategory) {
        return Category.find("code", idCategory).firstResultOptional();
    }

    public List<Category> getAll() {
        return Category.listAll();
    }
}
