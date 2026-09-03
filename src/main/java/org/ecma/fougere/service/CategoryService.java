package org.ecma.fougere.service;

import org.ecma.fougere.domain.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    Optional<Category> getCategoryByCode(String codeCategory);
    List<Category> getAll();
}
