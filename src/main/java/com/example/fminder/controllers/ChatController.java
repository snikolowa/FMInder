package com.example.fminder.controllers;

import com.example.fminder.models.Message;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ChatController extends BaseController{

    @GetMapping("/chat/{receiverId}")
    public ResponseEntity<List<Message>> getChat(@PathVariable int receiverId, HttpServletRequest request){
        int currentUserId = authenticationHelper.getLoggedUserId(request);

        return new ResponseEntity<>(chatService.getChat(currentUserId, receiverId), HttpStatus.OK);
    }

    @PostMapping("/chat/{receiverId}")
    public ResponseEntity<Message> saveMessage(@PathVariable int receiverId, @Valid @RequestBody Message message, HttpServletRequest request){
        int currentUserId = authenticationHelper.getLoggedUserId(request);

        return new ResponseEntity<>(chatService.saveMessage(message, currentUserId, receiverId), HttpStatus.CREATED);
    }
}
