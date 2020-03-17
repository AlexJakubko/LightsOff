package sk.tuke.gamestudio.game.service;

import sk.tuke.gamestudio.game.entity.Comment;

import java.util.List;

public interface CommentService {
    void addComment(Comment comment) throws CommentException;
    List<Comment> getComments(String game) throws CommentException;
}