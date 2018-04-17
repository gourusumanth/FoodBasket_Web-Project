
public class Comment {
  int commentId;

  int commentFeedbackId;
  String commentText;
  String commentUserName;

  void setCommentId(int commentId) {
    this.commentId = commentId;
  }

  void setCommentFeedbackId(int commentFeedbackId) {
    this.commentFeedbackId = commentFeedbackId;
  }

  void setCommentText(String commentText) {
    this.commentText = commentText;
  }

  void setCommentUserName(String commentUserName) {
    this.commentUserName = commentUserName;
  }

  int getCommentId() {
    return commentId;
  }

  int getCommentFeedbackId() {
    return commentFeedbackId;
  }

  String getCommentText() {
    return commentText;
  }

  String getCommentUserName() {
    return commentUserName;
  }
}
