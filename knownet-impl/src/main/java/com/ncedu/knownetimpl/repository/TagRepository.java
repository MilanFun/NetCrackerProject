package com.ncedu.knownetimpl.repository;

import com.ncedu.knownetimpl.model.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

  boolean existsByTitle(String title);

  @Query(value = "WITH RECURSIVE a AS(SELECT id, parent_id, title FROM tags WHERE id = :id UNION SELECT t.id, t.parent_id, t.title FROM tags t JOIN a ON t.id = a.parent_id) SELECT id, parent_id, title FROM a", nativeQuery = true)
  List<Tag> findWithParents(Long id);

  List<Tag> findByParentId(Long parentId);

  List<Tag> findByTitle(String title);

  @Query(value = "SELECT max(id) FROM tags", nativeQuery = true)
  Long getLastId();
}
