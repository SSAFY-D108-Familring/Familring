package com.familring.userservice.config.firebase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class FcmMessage {

    private boolean validateOnly;
    private Message message;

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Message {
        private Notification notification;
        private String token;
        private FcmDto data; //FcmDto
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Notification {
        private String title; //FcmDTO것과동일
        private String body;  //FcmDTO것과동일
        private String image;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class FcmDto { //FcmDto
        private String title;
        private String body;
    }
}