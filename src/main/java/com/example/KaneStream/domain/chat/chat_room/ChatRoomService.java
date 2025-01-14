package com.example.KaneStream.domain.chat.chat_room;

import com.example.KaneStream.domain.user.entity.User;
import com.example.KaneStream.domain.user.service.UserService;
import com.example.KaneStream.exeption.CustomAccessDeniedException;
import com.example.KaneStream.exeption.ResourceAlreadyExistsException;
import com.example.KaneStream.exeption.ResourceNotFoundException;
import com.example.KaneStream.exeption.UserNotAuthenticatedException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserService userService;

    @Transactional
    public ChatRoom createChatRoom(ChatRoomRequest request) {
        chatRoomRepository.findByRoomName(request.getRoomName())
                .ifPresent(chatRoom -> {
                    throw new ResourceAlreadyExistsException("Room already exists");
                });

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomName(request.getRoomName());
        chatRoom.setCreator(userService.getCurrentUser().orElseThrow(
                () -> new UserNotAuthenticatedException("User not authenticated")
        ));
        chatRoom.setType(ChatType.valueOf(request.getChatType()));
        ChatRoom chatRoom1 = chatRoomRepository.save(chatRoom);

        if( request.getMembers() != null ) {
            for (UUID member : request.getMembers()) {
                User user= userService.getUserById(member).orElseThrow(
                        () -> new ResourceNotFoundException("User not found")
                );
                ChatRoomMember chatRoomMember = new ChatRoomMember();
                chatRoomMember.setUser(user);
                chatRoomMember.setRole(MemberRole.member);
                chatRoomMember.setRoom(chatRoom1);
                chatRoom1.getMembers().add(chatRoomMember);
            }
        }
        return chatRoomRepository.save(chatRoom1);

    }

    public List<String> getChatType(){
        return Arrays.stream(ChatType.values())
                .map(ChatType::name)
                .collect(Collectors.toList());
    }

    public ChatRoom deleteRoom(UUID roomId) {
        ChatRoom chatRoom= chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        List<ChatRoomMember> members = chatRoom.getMembers();

        for (ChatRoomMember chatRoomMember : members) {
            if(chatRoomMember.getUser().getId().equals(chatRoomMember.getUser().getId()) && (chatRoomMember.getRole() == MemberRole.member || chatRoomMember.getRole() == MemberRole.admin) ) {
                chatRoom.setStatus("deleted");
                return chatRoomRepository.save(chatRoom);
            }
            else{
                throw new CustomAccessDeniedException("Access denied");
            }

        }
        throw new CustomAccessDeniedException("The user is not currently in this room.");

    }

}
