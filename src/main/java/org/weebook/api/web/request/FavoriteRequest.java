package org.weebook.api.web.request;

import lombok.Data;

@Data
public class FavoriteRequest {
    Long idUser;

    Long idProduct;

    Boolean like;
}
