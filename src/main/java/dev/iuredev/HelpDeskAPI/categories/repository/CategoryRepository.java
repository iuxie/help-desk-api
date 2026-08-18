package dev.iuredev.HelpDeskAPI.categories.repository;

import dev.iuredev.HelpDeskAPI.categories.model.CategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryModel, Long> {
}
