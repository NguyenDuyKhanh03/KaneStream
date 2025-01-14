package com.example.KaneStream.domain.chat.chat_room;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/chat-room")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @PostMapping("/create-room")
    public ResponseEntity<ChatRoom> createRoom(@RequestBody ChatRoomRequest request) {
        ChatRoom chatRoom= chatRoomService.createChatRoom(request);
        return new ResponseEntity<>(chatRoom, HttpStatus.CREATED);
    }

    @GetMapping("/get-list-type")
    public ResponseEntity<List<String>> getListType() {
        return new ResponseEntity<>(chatRoomService.getChatType(), HttpStatus.OK);
    }

    @PostMapping("/delete-room/{roomId}")
    public ResponseEntity<ChatRoom> deleteRoom(@PathVariable UUID roomId) {
        return new ResponseEntity<>(chatRoomService.deleteRoom(roomId), HttpStatus.OK);
    }
}
