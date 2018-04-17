
public class Feedback {
  int feedbackId;
  String feedbackText;
  String feedbackUserName;

  void setFeedbackId(int feedbackId) {
    this.feedbackId = feedbackId;
  }

  void setFeedbackText(String feedbackText) {
    this.feedbackText = feedbackText;
  }

  void setFeedbackUserName(String feedbackUserName) {
    this.feedbackUserName = feedbackUserName;
  }

  int getFeedbackId() {
    return feedbackId;
  }

  String getFeedbackText() {
    return feedbackText;
  }

  String getFeedbackUserName() {
    return feedbackUserName;
  }
}
