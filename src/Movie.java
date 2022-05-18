// a simple class to represent a movie with movie ID and title
public class Movie {

    private String title;
    private int id;

    public Movie(String title, int id)
    {
        this.title = title;
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public int getID()
    {
        return id;
    }
}
