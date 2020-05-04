public class Flashcard
{
    private String question;
    private String answer;

    /**
     * creates one flashcard with question and answer
     * @param ans the answer
     * @param qu the question
     */
    public Flashcard(String qu, String ans)
    {
        question = qu;
        answer = ans;
    }

    /**
     * return answer
     * @return answer
     */

    String getAnswer()
    {
        return answer;
    }
    /**
     * return question
     * @return question
     */
    String getQuestion()
    {
        return question;
    }
    /**
     * sets answer for question
     * @param ans the answer
     */
    public void setAnswer(String ans)
    {
        ans = answer;
    }
    /**
     * sets question for answer
     * @param qu the question
     */
    public void setQuestion(String qu)
    {
        qu = question;
    }


}