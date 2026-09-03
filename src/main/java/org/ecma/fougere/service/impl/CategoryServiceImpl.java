package org.ecma.fougere.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import org.ecma.fougere.domain.Category;
import org.ecma.fougere.service.CategoryService;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CategoryServiceImpl implements CategoryService {

    public Optional<Category> getCategoryByCode(String codeCategory) {
        return Category.find("code", codeCategory).firstResultOptional();
    }

    public List<Category> getAll() {
        return Category.listAll();
    }
}
