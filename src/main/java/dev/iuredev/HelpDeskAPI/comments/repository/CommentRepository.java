package dev.iuredev.HelpDeskAPI.comments.repository;

import dev.iuredev.HelpDeskAPI.comments.model.CommentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentModel, Long> {

    List<CommentModel> findAllByTicketIdOrderByAsc(Long ticketId);

}
