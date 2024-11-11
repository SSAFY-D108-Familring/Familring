package com.familring.notificationservice.controller.client;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client/notifications")
@RequiredArgsConstructor
@Log4j2
@Hidden
public class NotificationClientController {
}
