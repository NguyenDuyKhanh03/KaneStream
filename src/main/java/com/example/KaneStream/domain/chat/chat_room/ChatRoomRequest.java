package com.example.KaneStream.domain.chat.chat_room;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ChatRoomRequest {
    private String roomName;
    private UUID creator;
    private String chatType;
    private List<UUID> members;
}
