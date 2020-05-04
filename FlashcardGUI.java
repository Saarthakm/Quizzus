import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.io.File;


public class FlashcardGUI extends JPanel
{
    private FlashcardSet flashcardSet = new FlashcardSet();
    private JFileChooser fileChooser = new JFileChooser();
    private JFrame frame = new JFrame();
    private JLabel quizzus;
    private JButton add;
    private JButton save;
    private JButton open;
    private JButton saveAs;
    private JButton startQuiz;
    private JButton close;
    private JScrollPane scroll;
    private JTextArea question = new JTextArea();
    private JTextArea answer = new JTextArea();
    private QuizGUI quiz = new QuizGUI(flashcardSet);
    int val = 100;
    int percent = 100;
    int saveAsCount = 0;
    int saveCount = 0;
    int openCount = 0;


    /**
     * Builds entire GUI for
     */
    public FlashcardGUI()
    {
        QuizzusLabel();
        ButtonPanel();
        Label(new JLabel("Question: "));
        TextField(question);
        Label(new JLabel("Answer: "));
        TextField(answer);
    }
    /**
     * creates a flashcardGUI object
     * @param set a flashcard set
     */
    public FlashcardGUI(FlashcardSet set)
    {
        flashcardSet = set;
    }

    /**
     * creates all the buttons used in the FlashcardGUI
     */
    private void ButtonPanel()
    {
        add = new JButton("Add");
        save = new JButton("Save");
        open = new JButton("Open");
        close = new JButton("Close");
        saveAs = new JButton("Save as");
        startQuiz = new JButton("Start quiz");

        add.setBackground(Color.orange);
        save.setBackground(Color.green);
        open.setBackground(Color.red);
        close.setBackground(Color.pink);
        saveAs.setBackground(Color.MAGENTA);
        startQuiz.setBackground(Color.cyan);

        save.addActionListener(new saveListener());
        open.addActionListener(new openListener());
        close.addActionListener(new closeListener());
        add.addActionListener(new addListener());
        saveAs.addActionListener(new saveAsListener());
        startQuiz.addActionListener(new startQuizListener());

        add(add);
        add(save);
        add(open);
        add(saveAs);
        add(close);
        add(startQuiz);

    }

    /**
     * creates a label
     * @param label the label name
     */
    private void Label(JLabel label)
    {
        add(label);
    }

    /**
     * creates a specific label: the official quizzus label on the front of the home (flashcardGUI) screen
     */
    private void QuizzusLabel()
    {
        quizzus = new JLabel("Quizzus");
        quizzus.setForeground(Color.BLUE);
        quizzus.setFont(new Font("Harlow Solid Italic", Font.BOLD, 142));
        add(quizzus);
    }

    /**
     * creates a textfield where questions and answers are typed in to be added to flashcard set
     * @param text the the JTextArea object
     */
    private void TextField(JTextArea text)
    {
        text.setLineWrap(true);
        text.setPreferredSize(new Dimension(150, 230));
        text.setBackground(Color.YELLOW);
        add(text);
        scroll = new JScrollPane(text);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        add(scroll);
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


    /**
     * closes the program. It will check to see if the user would like to save their current flashcard
     * set before closing, and if it doesn't, it will exit the program.
     */
    private void close()
    {
        //the savecount is the number of times the save button has been clicked, so if it has never been
        //clicked, it would be 0, meaning that a question will be asked of whether or not the user
        //wants to save the set before closing
        if(saveCount == 0 && saveAsCount == 0)
        {
            int option = JOptionPane.showOptionDialog(frame,
                    "Would you like to save before exiting?", null,
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if(option == JOptionPane.YES_OPTION)
                save();
            else
                System.exit(0);
        }
        else
            System.exit(0);
    }

    /**
     * saves the flashcard set continually. If the set has not been saved-as (meaning it has not been
     * assigned to a file) then the save-as method will be invoked. If not, then the save method will
     * update the flashcard set based on it's absolute path continually.
     */
    private void save()
    {
        //saveAsCount is the number of times the save-as button has been clicked. If it hasn't been
        //clicked, it is 0, meaning the set has not been assigned to a file. If this is the case, the
        //save-as method will be invoked first, then the regular save method will be invoked.
        if(saveAsCount == 0)
            saveAs();
        else
            flashcardSet.save(flashcardSet.getFileLocation());
        saveCount++;
    }

    /**
     * Assigns the set to a specific file on the computer
     */
    private void saveAs()
    {
        if(fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION)
        {
            //this file object is the specific place where set will be saved
            File file = fileChooser.getSelectedFile();
            flashcardSet.save(file.getAbsolutePath());
            flashcardSet.setFileName(file.getName());
        }
        else
        {
            createWarningMessage("You chose to not save the flashcard set!");
        }
        saveAsCount++;
    }



    /**
     * opens a previously saved set
     */
    public void readFromFile()
    {
        if( fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
        {
            //creates a new flashcard set
            flashcardSet = new FlashcardSet();
            //invokes the readIntoSet method from flashcardSet and the saved information is derived
            //from the absolute path of the file
            flashcardSet.readIntoSet(fileChooser.getSelectedFile().getAbsolutePath());
            setName(flashcardSet.getFileName());
        }
        else
            createWarningMessage("You chose to not open a flashcard set!");
    }

    /**
     * adds a flashcard to the flashcard set
     */
    private void addFlashcard()
    {
        //adds a card to the set based on the text inside the question and answer textfields
        flashcardSet.addCard(question.getText(), answer.getText());
        //setting the textfield text back to nothing so that the user can put in another question
        //and answer.
        question.setText("");
        answer.setText("");
    }


    ////////////////////////////////////////////ACTION LISTENERS/////////////////////////////////////////


    /**
     * invokes the save method upon the save button
     */
    private class saveListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if(flashcardSet.getCardSet().size() == 0)
                createWarningMessage("You must add at least 1 flashcard before saving!");
            else
                save();
        }
    }

    /**
     * invokes the save-as method upon the save-as button
     */
    private class saveAsListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if(flashcardSet.getCardSet().size() == 0)
                createWarningMessage("You must add at least 1 flashcard before saving!");
            else
                saveAs();
        }
    }

    /**
     * invokes the close method upon the close button
     */
    private class closeListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            close();
        }
    }

    /**
     * invokes the build method in the quizGUI class to start the quiz. If no flashcards have been
     * added to the set, and the open method hasn't been invoked yet, then a warning message will display
     * stating that a flashcard set must be opened or created before starting a quiz. Otherwise
     * the quiz GUI will build and operate normally
     */
    private class startQuizListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {

            //If the size of the flashcardset is 0, meaning no cards have been added, and the open button
            //has not been clicked once, then a warning message will show
            if(flashcardSet.getCardSet().size() == 0 && openCount == 0)
                createWarningMessage("You must create or open a flashcard set before starting a quiz!");
            else
                quiz.buildQuizGUI();

        }
    }

    /**
     * invokes the addflashcard method upon the add button
     *
     */
    private class addListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if(question.getText().length() == 0 && answer.getText().length() == 0)
                createWarningMessage("You must type something into the question and answer box before adding to set!");
            else
                addFlashcard();
        }
    }

    /**
     * invokes the readfromFile method upon the open button
     */
    private class openListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            readFromFile();
            openCount++;
        }
    }


    public static void main(String [] args)
    {
        JFrame frame = new JFrame("Quizzus");
        frame.setSize(520, 560);
        frame.setLocation(750, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new FlashcardGUI());
        frame.setVisible(true);
    }
}