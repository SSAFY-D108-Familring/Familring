package com.familring.albumservice.exception.album;

import com.familring.albumservice.exception.base.ApplicationException;
import com.familring.albumservice.exception.constant.ErrorDetail;

public class InvalidAlbumRequestException extends ApplicationException {
    public InvalidAlbumRequestException() {
        super(ErrorDetail.INVALID_ALBUM_REQUEST);
    }
}
