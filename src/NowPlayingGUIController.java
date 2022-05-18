// lots of classes get used here!
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

// this class implements ActionListener interface, which allows for interactivity with JButtons
public class NowPlayingGUIController implements ActionListener
{
    // we set and use these across different methods
    // so we add them as instance variables
    private JTextArea movieInfo;
    private JTextField movieEntryField;
    private ArrayList<Movie> nowPlaying;
    private MovieNetworkingClient client;

    // constructor, which calls helper methods
    // to setup the GUI then load the now playing list
    public NowPlayingGUIController()
    {
        movieInfo = new JTextArea(20, 35);
        movieEntryField = new JTextField();
        nowPlaying = new ArrayList<>();
        client = new MovieNetworkingClient();  // our "networking client"

        // setup GUI and load Now Playing list
        setupGui();
        loadNowPlaying();
    }

    // private helper method, called by constructor
    // to set up the GUI and display it
    private void setupGui()
    {
        //Creating a Frame
        JFrame frame = new JFrame("My Now Playing Networking App!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // ends program when you hit the X


        // Creating an image from a jpg file stored in the src directory
        ImageIcon image = new ImageIcon("src/tmdblogo.jpg");
        Image imageData = image.getImage(); // transform it
        Image scaledImage = imageData.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        image = new ImageIcon(scaledImage);  // transform it back
        JLabel pictureLabel = new JLabel(image);
        JLabel welcomeLabel = new JLabel("   Movies Now Playing!");
        welcomeLabel.setFont(new Font("Helvetica", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.blue);

        JPanel logoWelcomePanel = new JPanel(); // the panel is not visible in output
        logoWelcomePanel.add(pictureLabel);
        logoWelcomePanel.add(welcomeLabel);

        // middle panel with movie list
        JPanel movieListPanel = new JPanel();
        movieInfo.setText("movies loading...");
        movieInfo.setFont(new Font("Helvetica", Font.PLAIN, 16));
        movieInfo.setWrapStyleWord(true);
        movieInfo.setLineWrap(true);
        movieListPanel.add(movieInfo);

        //bottom panel with text field and buttons
        JPanel entryPanel = new JPanel(); // the panel is not visible in output
        JLabel movieLabel = new JLabel("Which Movie? (Enter 1-20): ");
        movieEntryField = new JTextField(10); // accepts up to 10 characters
        JButton sendButton = new JButton("Send");
        JButton resetButton = new JButton("Reset");
        entryPanel.add(movieLabel);
        entryPanel.add(movieEntryField);
        entryPanel.add(sendButton);
        entryPanel.add(resetButton);

        //Adding Components to the frame
        frame.add(logoWelcomePanel, BorderLayout.NORTH);
        frame.add(movieListPanel, BorderLayout.CENTER);
        frame.add(entryPanel, BorderLayout.SOUTH);

        // PART 2 -- SET UP EVENT HANDLING
        //setting up buttons to use ActionListener interface and actionPerformed method
        sendButton.addActionListener(this);
        resetButton.addActionListener(this);

        // showing the frame
        frame.pack();
        frame.setVisible(true);
    }

    // private helper method to load the Now Playing
    // movie list into the GUI by making a network call,
    // obtaining an arraylist of Movie objects, then
    // creating a string that gets displayed in a GUI label;
    // this method gets called by the constructor as part of
    // the initial set up of the GUI, and also when the user
    // clicks the "Reset" button
    private void loadNowPlaying()
    {
        // use client to make network call to Now Playing, which returns an arraylist
        // which gets assigned to the nowPlaying instance variable
        nowPlaying = client.getNowPlaying();

        // build the string to display in the movieInfo label
        String labelStr = "";
        int count = 1;
        for (Movie movie : nowPlaying)
        {
            labelStr += count + ". " + movie.getTitle() + "\n";
            count++;
        }
        movieInfo.setText(labelStr);
    }

    // private helper method to load the details for
    // a particular movie into the GUI by making a network call,
    // obtaining a DetailedMovie, then
    // creating a string that gets displayed in a GUI label;
    // this method gets called when the user clicks the "Send" button
    private void loadMovieInfo(Movie movie)
    {
        // make network call to Movie Details, which returns a DetailedMovie object
        DetailedMovie detail = client.getMovieDetails(movie.getID());

        // build the string with movie details
        String info = "Title: " + detail.getTitle() +
                "\n\nOverview: " + detail.getOverview() +
                "\n\nTagline: " + detail.getTagline() +
                "\n\nPopularity: " + detail.getPopularity() +
                "\n\nReleased on: " + detail.getReleaseDate();

        movieInfo.setText(info);

        // download and display poster image in a new window
        try {
            URL imageURL = new URL(detail.getPosterPath());
            BufferedImage image = ImageIO.read(imageURL);
            JFrame frame = new JFrame("Poster for " + movie.getTitle());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            JLabel movieImage = new JLabel(new ImageIcon(image));
            frame.add(movieImage);
            frame.pack();
            frame.setLocation(490, 0);
            frame.setVisible(true);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    // implement ActionListener interface method
    // this method gets invoked anytime either button
    // gets clicked; we need code to differentiate which
    // button sent was clicked, so we use the text of the
    // button ("Send" or "Reset") to determine this
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) (e.getSource());  // cast source to JButton
        String text = button.getText();

        if (text.equals("Send"))
        {
            // obtain the numerical value that the user typed into the text field
            // (getTest() returns a string) and convert it to an int
            String selectedMovieNum = movieEntryField.getText();
            int movieNumInt = Integer.parseInt(selectedMovieNum);

            // obtain the movie in the nowPlaying arraylist that the number they
            // typed in corresponds to
            int movieIdx = movieNumInt - 1;
            Movie selectedMovie = nowPlaying.get(movieIdx);

            // call private method to load movie info for that Movie object
            loadMovieInfo(selectedMovie);
        }

        // if user clicked "Reset" button, set the text field back to empty string
        // and load the Now Playing list again
        else if (text.equals("Reset"))
        {
            movieEntryField.setText("");
            loadNowPlaying();
        }
    }
}