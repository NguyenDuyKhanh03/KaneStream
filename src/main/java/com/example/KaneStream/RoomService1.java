package com.example.KaneStream;

import io.livekit.server.AccessToken;
import io.livekit.server.RoomCreate;
import io.livekit.server.RoomService;
import io.livekit.server.VideoGrant;
import livekit.LivekitModels;
import livekit.LivekitRoom;
import org.springframework.stereotype.Service;
import retrofit2.Call;

@Service
public class RoomService1 {
    private final RoomService service;
    private final String apiKey;
    private final String secret;



    public Call<LivekitModels.Room> createRoom(
            String name,
            Integer emptyTimeout,
            Integer maxParticipants,
            String nodeId,
            String metadata,
            Integer minPlayoutDelay,
            Integer maxPlayoutDelay,
            Boolean syncStreams,
            Integer departureTimeout
    ){
        LivekitRoom.CreateRoomRequest.Builder requestBuilder = LivekitRoom.CreateRoomRequest.newBuilder();

        requestBuilder.setName(name);

        if (emptyTimeout != null) {
            requestBuilder.setEmptyTimeout(emptyTimeout);
        }
        if (maxParticipants != null) {
            requestBuilder.setMaxParticipants(maxParticipants);
        }
        if (nodeId != null) {
            requestBuilder.setNodeId(nodeId);
        }
        if (metadata != null) {
            requestBuilder.setMetadata(metadata);
        }
        if (minPlayoutDelay != null) {
            requestBuilder.setMinPlayoutDelay(minPlayoutDelay);
        }
        if (maxPlayoutDelay != null) {
            requestBuilder.setMaxPlayoutDelay(maxPlayoutDelay);
        }
        if (syncStreams != null) {
            requestBuilder.setSyncStreams(syncStreams);
        }
        if (departureTimeout != null) {
            requestBuilder.setDepartureTimeout(departureTimeout);
        }

        LivekitRoom.CreateRoomRequest request = requestBuilder.build();

        // Assuming authHeader(RoomCreate(true)) returns the necessary credentials
        String credentials = authHeader(new RoomCreate(true));
        return service.createRoom(request, credentials);
    }
    private String authHeader(VideoGrant...videoGrants){
        AccessToken accessToken = new AccessToken(apiKey, secret);
        accessToken.addGrants(videoGrants);

        String jwt = accessToken.toJwt();

        return "Bearer $jwt";
    }
}
