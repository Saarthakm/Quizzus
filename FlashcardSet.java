import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FlashcardSet
{
    private File file;
    private List <Flashcard> cardSet = new ArrayList<>();
    private String name = "none";
    private static final String GAP1 = "    ";
    private static final String GAP2 = "        ";


    /**
     * Adds a flashcard to a flashcard set array list
     * @param que the question
     * @param ans the answer
     */
    public void addCard(String que, String ans)
    {
        cardSet.add(new Flashcard(que, ans));
    }


    /**
     * saves the set at a specific file location
     * @param fileLocation the file name
     */
    void save(String filename)
    {
        file = new File(filename);
        try(BufferedWriter out = new BufferedWriter(new FileWriter(file)))
        {
            for(int x = 0; x < cardSet.size(); x++)
            {
                out.write(cardSet.get(x).getQuestion() + GAP2 + cardSet.get(x).getAnswer() + GAP1);
            }

        } catch(IOException ioEx){
            ioEx.printStackTrace();


        }
    }

    /**
     * Takes the string of data and separates the question and answer based on the gaps
     * @param nonparsed the string that hasn't been parsed
     */
    public void parseData(String nonparsed)
    {
        String part1 = nonparsed.substring(0, nonparsed.indexOf(GAP2));
        String part2 = nonparsed.substring(nonparsed.indexOf(GAP2));
        addCard(part1, part2);
    }

    /**
     * takes data from a previously saved deck and puts it into a set
     * @param fileLoc location of file (file name)
     * @throws IOException
     */
    public void readIntoSet(String fileLoc)
    {
        file = new File(fileLoc);
        setFileName(file.getName());
        try(BufferedReader input = new BufferedReader(new FileReader(file)))
        {
            String str = new String();
            str = input.readLine();
            parseData(str);
        }
        catch(IOException ioEx)
        {
            ioEx.printStackTrace();
        }
    }

    public String getFileName()
    {
        return name;
    }

    public List <Flashcard> getCardSet()
    {
        return cardSet;
    }


    public String getFileLocation()
    {
        return file.getAbsolutePath();
    }


    public void setFileName(String name) {
        int index = name.indexOf(".");
        if(name.contains("."))
        {
            name = name.substring(0, index);
        }
    }
}
