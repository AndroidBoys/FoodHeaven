package drunkcoder.com.foodheaven.Models;

public class Faq {
    String question,answer;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Faq() {

    }

    public Faq(String question, String answer) {

        this.question = question;
        this.answer = answer;
    }
}
