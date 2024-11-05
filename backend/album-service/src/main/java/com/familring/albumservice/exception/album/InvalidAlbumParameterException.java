package com.familring.albumservice.exception.album;

import com.familring.albumservice.exception.base.ApplicationException;
import com.familring.albumservice.exception.constant.ErrorDetail;

public class InvalidAlbumParameterException extends ApplicationException {

    public InvalidAlbumParameterException() {
        super(ErrorDetail.INVALID_ALBUM_PARAMETER);
    }
}
