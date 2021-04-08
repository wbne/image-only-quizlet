import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.Background;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import java.util.Scanner;
import java.util.Random;
import java.io.File;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import javafx.geometry.Pos;
import javafx.scene.text.Font;

public class Window extends Application
{
    private static String dir, filename;
    private static ArrayList<String> images = new ArrayList<String>();
    private static ArrayList<String> questions = new ArrayList<String>();
    private static Random rand = new Random();
    private static Label question;
    private static int showingQ, currentIndex, invert;
    private static VBox imageWrap;
    private static BackgroundImage myBI;

    public static void main(String []args)
    {
        launch(args);
    }

    public void start(Stage stage)
    {
        stage.setTitle("scuffed quizlet java edition");
        Group root = new Group();
        Scene scene = new Scene(root, 600, 600);
        stage.setScene(scene);
        dir = FileSystems.getDefault().getPath(".").toAbsolutePath().toString();
        File local = new File(dir);
        for(File file : local.listFiles())
        {
            if(file.isDirectory() && file.getName().equals("Images")) //checks for the Images folder
            {
                File sub = file;
                for(File subFile : sub.listFiles())
                {
                    String[] temp = subFile.toString().split("\\\\");
                    images.add(temp[temp.length - 1]);
                }
            }
        }

        try{
            Scanner readQuestions = new Scanner(new File("Questions.txt"));
            while(readQuestions.hasNext())
            {
                questions.add(readQuestions.nextLine());
            }
        }
        catch(Exception e){}

        scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
                @Override
                public void handle(KeyEvent e)
                {
                    if(e.getCode() == KeyCode.SPACE)
                    {
                        flip();
                    }
                    else if(e.getCode() == KeyCode.ENTER)
                    {
                        currentIndex = rand.nextInt(questions.size());
                        loadFlashcard();
                    }
                    else if(e.getCode() == KeyCode.I)
                    {
                        invert();
                    }
                }
            });

        showingQ = 1;
        invert = 0;

        question = new Label(questions.get(0));
        question.setMinWidth(600);
        question.setAlignment(Pos.CENTER);
        question.setFont(new Font("Barlow", 20));
        
        VBox vb = new VBox(question);
        vb.setMinHeight(500);
        vb.setAlignment(Pos.CENTER);
        
        root.getChildren().add(vb);
        imageWrap = new VBox();
        imageWrap.setMinWidth(600);
        imageWrap.setMinHeight(600);
        root.getChildren().add(imageWrap);
        
        loadFlashcard();

        stage.show();
    }

    private static void loadFlashcard()
    {
        showingQ = 1;
        question.setText(questions.get(currentIndex));
        myBI = new BackgroundImage(new Image("./Images/"+images.get(currentIndex),600,600,false,true),
            BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
            BackgroundSize.DEFAULT);
        imageWrap.setBackground(new Background(myBI));
        show();
    }
    
    private static void show()
    {
        if(showingQ + invert == 1)
        {   
            imageWrap.setVisible(false);
            question.setText(questions.get(currentIndex));
        }   
        else
        {
            imageWrap.setVisible(true);
            question.setText("");
        }
    }
    
    private static void flip()
    {
        if(showingQ + invert == 1)
        {   
            imageWrap.setVisible(true);
            question.setText("");
            showingQ = (showingQ + 1) % 2;
        }   
        else
        {
            imageWrap.setVisible(false);
            question.setText(questions.get(currentIndex));
            showingQ = (showingQ + 1) % 2;
        }
    }
    
    private static void invert()
    {
        invert = (invert + 1) % 2;
        show();
    }
}
