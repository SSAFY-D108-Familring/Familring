package com.familring.albumservice.exception.album;

import com.familring.albumservice.exception.base.ApplicationException;
import com.familring.albumservice.exception.constant.ErrorDetail;

public class AlbumNotFoundException extends ApplicationException {

    public AlbumNotFoundException() {
        super(ErrorDetail.ALBUM_NOT_FOUND);
    }
}
