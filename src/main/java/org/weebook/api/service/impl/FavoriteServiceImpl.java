package org.weebook.api.service.impl;

import jakarta.persistence.Transient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.weebook.api.entity.Favorite;
import org.weebook.api.entity.Product;
import org.weebook.api.entity.User;
import org.weebook.api.repository.FavoriteRepository;
import org.weebook.api.service.FavoriteService;
import org.weebook.api.web.request.FavoriteRequest;

import java.time.Instant;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {
   private final FavoriteRepository favoriteRepository;
    @Override
    public void Favorite(FavoriteRequest favoriteRequest) {
        Favorite favorite = favoriteRepository.findByUserEqualsAndProductEquals(User.
                        builder().id(favoriteRequest.getIdUser()).build(),
                Product.builder().id(favoriteRequest.getIdProduct()).build());
        if (favorite == null) {
            favorite = Favorite.builder()
                    .user(User.builder().id(favoriteRequest.getIdUser()).build())
                    .product(Product.builder().id(favoriteRequest.getIdProduct()).build())
                    .build();
        }
        Instant date = null;
        if (favoriteRequest.getLike()) {
            date = Instant.now();
        }

        favorite.setCreatedAt(date);
        favoriteRepository.save(favorite);
    }
}
