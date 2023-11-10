package org.weebook.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.weebook.api.entity.Favorite;
import org.weebook.api.entity.Product;
import org.weebook.api.entity.User;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Favorite findByUserEqualsAndProductEquals( User user,Product product);


}
