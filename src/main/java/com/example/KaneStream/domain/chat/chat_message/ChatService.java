package com.example.KaneStream.domain.chat.chat_message;

import com.example.KaneStream.domain.chat.chat_room.ChatRoom;
import com.example.KaneStream.domain.chat.chat_room.ChatRoomService;
import com.example.KaneStream.domain.user.entity.User;
import com.example.KaneStream.domain.user.service.UserService;
import com.example.KaneStream.exeption.CustomAccessDeniedException;
import com.example.KaneStream.exeption.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;
    private final UserService userService;

    public List<ChatMessage> loadMessageByRoom(UUID roomId) {
        ChatRoom chatRoom = chatRoomService.getChatRoom(roomId);
        User currentUser = userService.getCurrentUser()
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        boolean isRoom= chatRoom.getMembers().stream()
                .anyMatch(u -> u.getUser().getId().equals(currentUser.getId()));

        if(isRoom){
            return chatMessageRepository.findAllByRoomId(roomId);
        }
        throw new CustomAccessDeniedException("Access denied");

    }


    @Transactional
    public ChatMessage sendMessage(UUID roomId, String message, String username) {
        User user=userService.getUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        ChatRoom chatRoom = chatRoomService.getChatRoom(roomId);

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent(message);
        chatMessage.setRoom(chatRoom);
        chatMessage.setSender(user);
        return chatMessageRepository.save(chatMessage);
    }

    public ChatMessage deleteMessage(UUID messageId, String username) {
        User user=userService.getUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        ChatMessage chatMessage = chatMessageRepository.getReferenceById(messageId);
        if(!chatMessage.getSender().equals(user)){
            throw new CustomAccessDeniedException("Access denied");
        }
        chatMessageRepository.delete(chatMessage);
        return chatMessage;
    }
}
