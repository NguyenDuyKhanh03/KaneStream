package com.example.KaneStream.domain.post.post;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import com.example.KaneStream.domain.post.post_like.PostLike;
import com.example.KaneStream.domain.post.post_like.PostLikeId;
import com.example.KaneStream.domain.post.post_like.PostLikeRepository;
import com.example.KaneStream.domain.topic.Topic;
import com.example.KaneStream.domain.topic.TopicService;
import com.example.KaneStream.domain.user.UserDto;
import com.example.KaneStream.domain.user.entity.User;
import com.example.KaneStream.domain.user.service.UserService;
import com.example.KaneStream.exeption.CustomAccessDeniedException;
import com.example.KaneStream.exeption.ResourceNotFoundException;
import com.example.KaneStream.exeption.UserNotAuthenticatedException;
import com.example.KaneStream.integration.minio.MinioChannel;
import com.example.KaneStream.mapper.Mapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;
    private final TopicService topicService;
    private final MinioChannel minioChannel;
    private final PostLikeRepository postLikeRepository;

    private final Mapper<Post,PostDto> postMapper;

    private final ElasticsearchClient client;


    @Transactional
    public PostResponse createPost(PostRequest request) {
        User user=userService.getCurrentUser()
                .orElseThrow(()->new ResourceNotFoundException("User not logged in."));

        Post post=new Post();
        post.setAuthor(user);
        post.setContent(request.getContent());

        Topic topic=topicService.getTopicById(request.getTopic());
        post.setTopic(topic);

        if(request.getFile()!=null){
            post.setImage(minioChannel.upload(request.getFile()));
        }

        int count=topic.getPostsCount();
        topic.setPostsCount(count+1);
        topicService.updateTopic(topic);

        user.setPostsCount(user.getPostsCount()+1);
        userService.updateUser(user);

        PostResponse postResponse=new PostResponse(
                new UserDto(user.getId(),user.getLastName(),user.getAvatar(),user.getUsername(),user.getBio(),user.getPostsCount(),user.getFollowersCount(),0,"123" ),
                postMapper.mapFrom(postRepository.save(post))
        );

        addPost(postResponse);

        return postResponse;
    }

    @Transactional
    public void deletePost(UUID postId) {
        User user=userService.getCurrentUser()
                .orElseThrow(()->new UserNotAuthenticatedException("User not logged in."));

        Post post=postRepository.findById(postId)
                .orElseThrow(()->new ResourceNotFoundException("Post not found"));

        if(post.getAuthor()==user){
            postRepository.delete(post);

            user.setPostsCount(user.getPostsCount()-1);
            userService.updateUser(user);
        }
        else{
            throw new CustomAccessDeniedException("You are not allowed to delete this post");
        }

    }

    @Transactional
    public PostDto updatePost(UUID postId, PostRequest request) {
        User user=userService.getCurrentUser()
                .orElseThrow(()->new UserNotAuthenticatedException("User not logged in"));

        Post post=postRepository.findById(postId)
                .orElseThrow(()->new ResourceNotFoundException("Post not found"));

        if(post.getAuthor()==user){
            if(request.getContent()!=null){
                post.setContent(request.getContent());
            }
            if(request.getFile()!=null){
                post.setImage(minioChannel.upload(request.getFile()));
            }
            return postMapper.mapFrom(postRepository.save(post));
        }
        else{
            throw new CustomAccessDeniedException("You are not allowed to delete this post");
        }
    }

    public Page<PostResponse> getPosts(int page) {


        Sort sort=Sort.by("likedCount","commentsCount").descending();

        Pageable pageable= PageRequest.of(page,10,sort);

        Page<Post> posts=postRepository.findAll(pageable);
        return posts.map(post -> {
            User user=post.getAuthor();
            PostDto postDto=postMapper.mapFrom(post);
            return new PostResponse(
                    new UserDto(user.getId(),user.getLastName(),user.getAvatar(),user.getUsername(),user.getBio(),user.getPostsCount(),user.getFollowersCount(),0,"123" ),
                    postDto
            );

        });


    }

    @Transactional
    public int updateLikeCount(UUID postId, String username) {
        User user=userService.getUserByUsername(username)
                        .orElseThrow(
                                ()->new UserNotAuthenticatedException("User not logged in")
                        );

        System.out.println(user.getUsername());
        Post post=postRepository.findById(postId)
                .orElseThrow(()->new ResourceNotFoundException("Post not found"));

        PostLike postLike= postLikeRepository.findById(new PostLikeId(postId,user.getId())).orElse(null);

        if(Objects.isNull(postLike)){
            post.setLikedCount(post.getLikedCount()+1);
            postLike= new PostLike();
            postLike.setId(new PostLikeId(postId,user.getId()));
            postLikeRepository.save(postLike);
        }
        else{
            post.setLikedCount(post.getLikedCount()-1);
            postLikeRepository.delete(postLike);
        }


        return postRepository.save(post).getLikedCount();
    }

    public Optional<Post> getPostById(UUID postId) {
        return postRepository.findById(postId);
    }

    @Transactional
    public void updateCommentCount(UUID postId) {
        Post post= postRepository.findById(postId).orElseThrow(
                ()->new ResourceNotFoundException("Post not found")
        );

        post.setCommentsCount(post.getCommentsCount()+1);
        postRepository.save(post);



    }



    private void createIndexIfNotExists(String indexName) {
        boolean indexExists = false;
        try {
            indexExists = client.indices().exists(e -> e.index(indexName)).value();
            if (!indexExists) {
                // Nếu không tồn tại, tạo chỉ mục
                CreateIndexRequest createIndexRequest = new CreateIndexRequest.Builder()
                        .index(indexName)
                        .build();

                CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void addPost(PostResponse response) {
        createIndexIfNotExists("post");
        IndexRequest<PostResponse> indexRequest= IndexRequest.of(i-> i
                .index("posts")
                .id(response.getPost().getId().toString())
                .document(response)
        );
        try {
            client.index(indexRequest);
        }catch (IOException e){
            throw new RuntimeException(e);
        }

    }

    public List<PostResponse> search(String keyword) {
        // Use SearchResponse to search for posts by their content and sort like count.
        try {
            SearchResponse<PostResponse> searchResponse = client.search(s -> s
                            .index("posts")
                            .query(q -> q
                                    .match(m -> m
                                            .field("post.content")
                                            .query(keyword)
                                    )
                            )
                            .sort(sort -> sort //
                                    .field(f -> f
                                            .field("post.likedCount")
                                            .order(SortOrder.Desc)
                                    )
                            ),
                    PostResponse.class
            );

            return searchResponse.hits().hits().stream()
                    .map(Hit::source)
                    .toList();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }


}
