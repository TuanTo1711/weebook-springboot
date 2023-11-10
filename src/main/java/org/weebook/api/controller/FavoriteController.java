package org.weebook.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.weebook.api.dto.CategoryDto;
import org.weebook.api.service.FavoriteService;
import org.weebook.api.web.request.FavoriteRequest;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/favorite")
public class FavoriteController {
    final FavoriteService favoriteService;
    @PostMapping
    public void findAll(@RequestBody FavoriteRequest favoriteRequest){
         favoriteService.Favorite(favoriteRequest);
    }

}
