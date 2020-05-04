import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class QuizGUI
{
    private FlashcardSet set;
    private JFrame frame;
    private JProgressBar pb;
    private JLabel queAnsLabel;
    private JButton showAnswer;
    private JButton nextQuestion;
    private JLabel label;
    private JScrollPane scroll;
    private JPanel panel;
    private JButton correct;
    private JButton reset;
    private JButton wrong;
    private JTextArea area = new JTextArea();
    int percent = 100;
    int answerCount = 0;
    int questionCount = 1;
    int rightCount = 0;
    int wrongCount = 0;

    /**
     * creates a quiz player
     * @param set the flashcard set used to make a quiz player
     */

    public QuizGUI(FlashcardSet set)
    {
        this.set = set;
    }

    /**
     * Method that builds entire Quiz GUI
     */
    public void buildQuizGUI()
    {
        frame();
        contentPane();
        queLabel();
        textField();
        buttonPanel();
        barAndPercent();
    }

    /**
     * Building all the buttons in the GUI, adding them to panel, and adding
     * respective action listeners
     */
    private void buttonPanel()
    {
        //show answer button added to panel and listener
        showAnswer = new JButton("Show Answer");
        showAnswer.addActionListener(new showAnswerListener());
        panel.add(showAnswer);

        //next question button added to panel and listener

        nextQuestion = new JButton("Next Question");
        nextQuestion.addActionListener(new nextQuestionListener());
        panel.add(nextQuestion);

        //correct button added to panel and listener

        correct = new JButton("Correct");
        correct.addActionListener(new correctListener());
        panel.add(correct);

        //wrong button added to panel and listener

        wrong = new JButton("Wrong");
        wrong.addActionListener(new wrongListener());
        panel.add(wrong);

        //reset button added to panel and listener

        reset = new JButton("Reset Quiz");
        reset.addActionListener(new resetListener());
        panel.add(reset);
    }

    /**
     * Builds visual JBar to keep track of percent of questions in quiz that are correct
     */
    private void barAndPercent()
    {
        pb = new JProgressBar();
        pb.setValue(100);
        pb.setForeground(Color.GREEN);
        panel.add(pb);
        label = new JLabel("Percent Questions Correct: 100");
        panel.add(label);
    }

    /**
     * Builds frame for GUI
     */
    private void frame()
    {
        frame = new JFrame("Quiz");
        frame.setSize(290, 480 );
        frame.setLocation(870, 370);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * Builds content pane for GUI
     */
    private void contentPane()
    {
        panel = new JPanel();
        panel.setBackground(Color.cyan);
        frame.setContentPane(panel);
    }

    /**
     * Builds yellow text areas with vertical scroll-bar that displays questions and answers
     */
    private void textField()
    {

        area = new JTextArea();
        area.setLineWrap(true);
        area.setPreferredSize(new Dimension(230, 250));
        area.setBackground(Color.YELLOW);
        //As soon as the quiz starts, the first question is already pre-loaded into the textfield
        area.setText(set.getCardSet().get(0).getQuestion());
        panel.add(area);
        //Adding a scrollbar
        scroll = new JScrollPane(area);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(scroll);
    }

    /**
     * Builds question label
     */
    private void queLabel()
    {
        queAnsLabel = new JLabel("Question:");
        panel.add(queAnsLabel);
    }

    /**
     * Creates a warning message based on a given string
     * @param str the warning message
     */
    private void createWarningMessage(String str)
    {
        JOptionPane.showOptionDialog(frame, str, null,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
    }
    ///////////////////////////////////////////////ACTION LISTENERS/////////////////////////////////////////////


    /**
     * When showanswer button is clicked, if the user tries to click the showanswer button twice in a row
     * to try to cheat and find the answer to the next question, a popup is created that states that they
     * must reveal next question before revealing next answer. Otherwise, the textarea is set to the next
     * answer in
     *
     */
    private class showAnswerListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            //If the answer button is being clicked twice in a row, then this warning message shows up
            if(answerCount >= questionCount)
                createWarningMessage("You must move on to the next question before revealing the answer!");
            else if(answerCount <= set.getCardSet().size())
            {
                area.setText(set.getCardSet().get(answerCount).getAnswer());
                queAnsLabel.setText("Answer: ");
                answerCount++;
            }


        }
    }
    /**
     * If the next question button is clicked two times in a row, a warning message will appear,
     * stating you reveal the answer first. Otherwise, if the user hasn't navigated through all the
     * flashcards, then the quiz will continue. If all the flashcards are done, a message will pop up and tell
     * you your score
     */
    private class nextQuestionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {

            //If the next question button is clicked two times in a row, a warning message will appear
            if(questionCount == answerCount + 1)
                createWarningMessage("You must reveal the answer before moving to the next question!");
                //navigating through rest of flashcards
            else if(questionCount <= set.getCardSet().size() - 1)
            {
                area.setText(set.getCardSet().get(questionCount).getQuestion());
                queAnsLabel.setText("Question: ");
                questionCount++;
            }
            //if all the cards are done, the score will show if the next question button is clicked
            else
                createWarningMessage("The quiz is done!!! Your score was " + percent + "%");

        }
    }
    /**
     * Resets the entire quiz by changing the text area back to the first question, changing the
     * JBar back to 100%, the label to 100%, the upper label back to "question:", and the question count
     * back to 0 and the answer count to 1.
     */
    private class resetListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            area.setText(set.getCardSet().get(0).getQuestion());
            label.setText("Percent Questions Correct: 100");
            queAnsLabel.setText("Question: ");
            pb.setValue(100);
            percent = 100;
            answerCount = 0;
            questionCount = 1;
            wrongCount = 0;
            rightCount = 0;
        }
    }

    /**
     * Counts the number of wrong answers on the quiz. It'll take this number and determine the amount
     * of percent the JBar must go down to, and the value to change the overall score to. Also, if a user
     * tries to go under 0% correct, a warning message shows up
     */
    private class wrongListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            //this value represents the amount of points that must be taken off the quiz for each
            //wrong answer.
            int percentValueOfEachQuestion = 100 / set.getCardSet().size();
            if(pb.getValue() - percentValueOfEachQuestion  < 0)
                createWarningMessage("The score can't go under 0!");
            else if(wrongCount + rightCount >= answerCount)
                createWarningMessage("You must go to next question before marking wrong!");
            else
            {
                pb.setValue(percent - percentValueOfEachQuestion);
                label.setText("Percent Questions Correct: " + (percent - percentValueOfEachQuestion));
                percent = percent - percentValueOfEachQuestion;
                wrongCount++;
            }

        }
    }

    private class correctListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if(rightCount + wrongCount>= answerCount)
                createWarningMessage("You must go to the next question before marking correct!");
            else
                rightCount++;
        }
    }
}