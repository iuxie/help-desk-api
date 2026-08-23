package dev.iuredev.HelpDeskAPI.categories.repository;

import dev.iuredev.HelpDeskAPI.categories.model.CategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryModel, Long> {

    List<CategoryModel> findAllByActiveTrue();

}
