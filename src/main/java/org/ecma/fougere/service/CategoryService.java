package org.ecma.fougere.service;

import org.ecma.fougere.domain.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    Optional<Category> getCategory(String idCategory);
    List<Category> getAll();
}
