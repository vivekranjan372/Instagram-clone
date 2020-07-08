package com.example.instagrame.Model;


public class Comment
{
   private String comment;
   private String commentId;
   private String publisher;
public Comment()
{

}

   public Comment(String comment, String commentId, String publisher) {
      this.comment = comment;
      this.commentId = commentId;
      this.publisher = publisher;
   }

   public String getCommentId() {
      return commentId;
   }

   public void setCommentId(String commentId) {
      this.commentId = commentId;
   }

   public String getPublisher() {
      return publisher;
   }

   public void setPublisher(String publisher) {
      this.publisher = publisher;
   }

   public String getComment()
   {
      return comment;
   }
   public void setComment(String comment)
   {
      this.comment=comment;
   }

}
