package com.example.KaneStream.domain.chat.chat_message;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.http.Body;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatService chatService;

    @GetMapping("/get-messages/{roomId}")
    public ResponseEntity<List<ChatMessage>> getMessages(@PathVariable UUID roomId) {
        return new ResponseEntity<>(chatService.loadMessageByRoom(roomId), HttpStatus.OK);
    }

    @MessageMapping("/message/send/room/{roomId}")
    @SendTo("/topic/message/room/{roomId}")
    public ChatMessage sendMessage(@DestinationVariable UUID roomId,@Body String message, Principal principal) {
        String username = principal.getName();
        return chatService.sendMessage(roomId,message,username);
    }

    @MessageMapping("/message/delete/{messageId}")
    @SendTo("/topic/message/delete/{messageId}")
    public ChatMessage deleteMessage(@DestinationVariable UUID messageId, Principal principal) {
        String username = principal.getName();
        return chatService.deleteMessage(messageId,username);
    }


}
