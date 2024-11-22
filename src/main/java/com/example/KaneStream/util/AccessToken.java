package com.example.KaneStream.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import io.livekit.server.RoomJoin;
import io.livekit.server.SIPGrant;
import io.livekit.server.VideoGrant;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Getter
@Setter
public class AccessToken {
    private final String apiKey;
    private final String secret;
    private final Set<VideoGrant> videoGrants= new HashSet<>();
    private final Set<SIPGrant> sipGrants= new HashSet<>();

    private long ttl= TimeUnit.MINUTES.convert(6, TimeUnit.HOURS);
    private Date expiration=null;
    private Date notBefore=null;
    private String name=null;
    private String identity=null;
    private String metadata=null;
    private String sha256=null;
    private Map<String, String> attributes = new HashMap<>();

    public AccessToken(String apiKey, String secret) {
        this.apiKey = apiKey;
        this.secret = secret;
    }

    public void addGrants(VideoGrant... grants) {
        videoGrants.addAll(Arrays.asList(grants));
    }

    public void addGrants(Iterable<VideoGrant> grants) {
        grants.forEach(videoGrants::add);
    }

    public void clearGrants() {
        videoGrants.clear();
    }

    public void addSIPGrants(SIPGrant... grants) {
        sipGrants.addAll(Arrays.asList(grants));
    }

    public void addSIPGrants(Iterable<SIPGrant> grants) {
        grants.forEach(sipGrants::add);
    }

    public void clearSIPGrants() {
        sipGrants.clear();
    }

    public String toJwt() {
        JWTCreator.Builder builder= JWT.create().withIssuer(apiKey);

        if(expiration!=null){
            builder.withExpiresAt(expiration);
        }else{
            builder.withExpiresAt(new Date(System.currentTimeMillis() + ttl));
        }

        if(notBefore!=null){
            builder.withNotBefore(notBefore);
        }

        if(identity!=null){
            builder.withSubject(identity).withJWTId(identity);
        }else{
            boolean hasRoomJoin = videoGrants.stream()
                    .anyMatch(grant -> grant instanceof RoomJoin && Boolean.TRUE.equals(((RoomJoin) grant).getValue()));
            if(hasRoomJoin){
                throw new IllegalStateException("identity is required for join, but is not set.");
            }
        }

        Map<String, Object> claimsMap= new HashMap<>();
        claimsMap.put("video", videoGrants);
        claimsMap.put("sip", sipGrants);
        if (name != null)
            claimsMap.put("name", name);

        if (metadata != null)
            claimsMap.put("metadata", metadata);

        if (sha256 != null)
            claimsMap.put("sha256", sha256);

        if (!attributes.isEmpty())
            claimsMap.put("attributes", new HashMap<>(attributes));

        claimsMap.forEach((key, value) -> withClaimAny(builder, key, value));

        Algorithm alg= Algorithm.HMAC256(secret);
        return builder.sign(alg);
    }



    private void withClaimAny(JWTCreator.Builder jwtBuilder, String name, Object value) {
        if (value instanceof Boolean) {
            jwtBuilder.withClaim(name, (Boolean) value);
        } else if (value instanceof Integer) {
            jwtBuilder.withClaim(name, (Integer) value);
        } else if (value instanceof Long) {
            jwtBuilder.withClaim(name, (Long) value);
        } else if (value instanceof Double) {
            jwtBuilder.withClaim(name, (Double) value);
        } else if (value instanceof String) {
            jwtBuilder.withClaim(name, (String) value);
        } else if (value instanceof Date) {
            jwtBuilder.withClaim(name, (Date) value);
        } else if (value instanceof Instant) {
            jwtBuilder.withClaim(name, Date.from((Instant) value));
        } else if (value instanceof List<?>) {
            jwtBuilder.withClaim(name, (List<?>) value);
        } else if (value instanceof Map<?, ?>) {
            jwtBuilder.withClaim(name, (Map<String, ?>) value);
        }
    }


}
