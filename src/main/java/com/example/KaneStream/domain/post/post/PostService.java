package com.example.KaneStream.domain.post.post;

import com.example.KaneStream.domain.topic.Topic;
import com.example.KaneStream.domain.topic.TopicService;
import com.example.KaneStream.domain.user.entity.User;
import com.example.KaneStream.domain.user.service.UserService;
import com.example.KaneStream.integration.minio.MinioChannel;
import com.example.KaneStream.mapper.Mapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;
    private final TopicService topicService;
    private final MinioChannel minioChannel;

    private final Mapper<Post,PostDto> postMapper;

    @Transactional
    public PostDto createPost(PostRequest request) {
        User user=userService.getCurrentUser()
                .orElseThrow(()->new RuntimeException("User not logged in"));

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

        return postMapper.mapFrom(postRepository.save(post));
    }

    @Transactional
    public void deletePost(UUID postId) {
        User user=userService.getCurrentUser()
                .orElseThrow(()->new RuntimeException("User not logged in"));

        Post post=postRepository.findById(postId)
                .orElseThrow(()->new RuntimeException("Post not found"));

        if(post.getAuthor()==user){
            postRepository.delete(post);
        }
        else{
            throw new RuntimeException("You are not allowed to delete this post");
        }

    }

    public PostDto updatePost(UUID postId, PostRequest request) {
        User user=userService.getCurrentUser()
                .orElseThrow(()->new RuntimeException("User not logged in"));

        Post post=postRepository.findById(postId)
                .orElseThrow(()->new RuntimeException("Post not found"));

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
            throw new RuntimeException("You are not allowed to delete this post");
        }
    }

    public Page<PostDto> getPosts(GetPostRequest request) {

        int page = request.getPage() >= 0 ? request.getPage() : 0;
        int size = request.getSize() > 0 ? request.getSize() : 10;
        String sortBy = request.getSortBy() != null ? request.getSortBy() : "created_at";

        Sort sort=Sort.by(request.getSortBy()).descending();

        Pageable pageable= PageRequest.of(page,size,sort);

        Page<Post> posts=postRepository.findAll(pageable);
        return posts.map(postMapper::mapFrom);
    }
}
