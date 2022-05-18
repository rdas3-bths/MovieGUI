// a subclass of Movie, in which additional details are added
public class DetailedMovie extends Movie
{
    private String overview;
    private String tagline;
    private double popularity;
    private String releaseDate;
    private String posterPath;

    public DetailedMovie(String title, int id, String overview,
                         String tagline, double popularity,
                         String releaseDate, String posterPath)
    {
        super(title, id);
        this.overview = overview;
        this.tagline = tagline;
        this.popularity = popularity;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
    }

    public String getOverview() { return overview; }
    public String getTagline() { return tagline; }
    public double getPopularity() { return popularity; }
    public String getReleaseDate() { return releaseDate; }
    public String getPosterPath() { return posterPath; }
}
