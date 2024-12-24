package com.example.KaneStream.domain.comment;

import com.example.KaneStream.domain.comment.comment.Comment;
import com.example.KaneStream.domain.comment.comment.CommentDto;
import com.example.KaneStream.domain.comment.comment.CommentRepository;
import com.example.KaneStream.domain.comment.comment_like.CommentLike;
import com.example.KaneStream.domain.comment.comment_like.CommentLikeId;
import com.example.KaneStream.domain.comment.comment_like.CommentLikeRepository;
import com.example.KaneStream.domain.post.post.Post;
import com.example.KaneStream.domain.post.post.PostService;
import com.example.KaneStream.domain.user.entity.User;
import com.example.KaneStream.domain.user.service.UserService;
import com.example.KaneStream.exeption.CustomAccessDeniedException;
import com.example.KaneStream.exeption.ResourceNotFoundException;
import com.example.KaneStream.exeption.UserNotAuthenticatedException;
import com.example.KaneStream.mapper.Mapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final Mapper<Comment, CommentDto> mapper;
    private final UserService userService;
    private final PostService postService;

    public Page<CommentDto> getComments(int page,UUID postId) {


        Sort sort=Sort.by("likedCount").descending();
        Post post= postService.getPostById(postId)
                .orElseThrow(
                        ()-> new ResourceNotFoundException("Post not found")
                );

        Pageable pageable = PageRequest.of(page, 10, sort);
        Page<Comment> comments= commentRepository.findByPost(post, pageable);

        return comments.map(comment -> {
            CommentDto commentDto=mapper.mapFrom(comment);
            commentDto.setAvatar(comment.getUser().getAvatar());
            commentDto.setUsername(comment.getUser().getUsername());
            return commentDto;
        });

    }

    @Transactional
    public CommentDto postComment(UUID postId, String content, String username) {
        User user=userService.getUserByUsername(username)
                .orElseThrow(
                        ()->new UserNotAuthenticatedException("User not logged in")
                );
        Post post= postService.getPostById(postId)
                .orElseThrow(
                        ()-> new ResourceNotFoundException("Post not found")
                );

        Comment comment= new Comment();
        comment.setContent(content);
        comment.setUser(user);
        comment.setPost(post);
        comment.setStatus(CommentStatus.active);

        postService.updateCommentCount(postId);

        return mapper.mapFrom(commentRepository.save(comment));
    }

    @Transactional
    public String deleteComment(UUID postId) {
        User user= userService.getCurrentUser()
                .orElseThrow(()-> new UserNotAuthenticatedException("User not logged in"));

        Post post= postService.getPostById(postId)
                .orElseThrow(
                        ()-> new ResourceNotFoundException("Post not found")
                );

        if(post.getAuthor().equals(user)) {
            commentRepository.deleteById(postId);
            return "Comment deleted";
        }
        throw new CustomAccessDeniedException("This comment cannot be deleted");
    }

    @Transactional
    public CommentDto updateCommentLike(UUID commentId) {
        User user= userService.getCurrentUser()
                .orElseThrow(()-> new UserNotAuthenticatedException("User not logged in"));

        Comment comment=commentRepository.findById(commentId)
                .orElseThrow(
                        ()-> new ResourceNotFoundException("Comment not found")
                );

        CommentLike commentLike= commentLikeRepository.findById(new CommentLikeId(commentId,user.getId())).orElse(null);

        if(Objects.isNull(commentLike)) {
            comment.setLikedCount(comment.getLikedCount()+1);
            commentLike=new CommentLike();
            commentLike.setId(new CommentLikeId(commentId,user.getId()));
            commentLikeRepository.save(commentLike);
        }
        else{
            comment.setLikedCount(comment.getLikedCount()-1);
            commentLikeRepository.findById(new CommentLikeId(commentId,user.getId())).ifPresent(commentLikeRepository::delete);
        }


        return mapper.mapFrom(commentRepository.save(comment));
    }

    @Transactional
    public CommentDto postCommentReply(UUID commentId, String content) {
        Comment comment=commentRepository.findById(commentId)
                .orElseThrow(
                        ()-> new ResourceNotFoundException("Comment not found")
                );

        Post post= comment.getPost();
        if(Objects.isNull(post)) {
            throw new ResourceNotFoundException("Post not found");
        }

        Comment commentReply= new Comment();
        commentReply.setContent(content);
        commentReply.setUser(comment.getUser());
        commentReply.setPost(post);
        commentReply.setStatus(CommentStatus.active);

        postService.updateCommentCount(post.getId());

        return mapper.mapFrom(commentRepository.save(commentReply));



    }
}
