package K2.ex24;

import java.util.*;
import java.util.stream.Collectors;

public class MoviesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MoviesList moviesList = new MoviesList();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int x = scanner.nextInt();
            int[] ratings = new int[x];
            for (int j = 0; j < x; ++j) {
                ratings[j] = scanner.nextInt();
            }
            scanner.nextLine();
            moviesList.addMovie(title, ratings);
        }
        scanner.close();
        List<Movie> movies = moviesList.top10ByAvgRating();
        System.out.println("=== TOP 10 BY AVERAGE RATING ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        movies = moviesList.top10ByRatingCoef();
        System.out.println("=== TOP 10 BY RATING COEFFICIENT ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }
}

// vashiot kod ovde

class Movie {
    private String name;
    private int[] ratings;

    public Movie(String name, int[] ratings) {
        this.name = name;
        this.ratings = ratings;
    }

    public String getName() {
        return name;
    }

    public int[] getRatings() {
        return ratings;
    }

    public int getNumberOfRatings() {
        return ratings.length;
    }

    public float getAverageRating() {
        if (ratings.length == 0) {
            return 0;
        }
        return (float) Arrays.stream(ratings).sum() / ratings.length;
    }

    @Override
    public String toString() {
        return String.format("%s (%.2f) of %d ratings",
                name,
                getAverageRating(),
                ratings.length);
    }

}

class MoviesList {
    ArrayList<Movie> moviesList;

    public MoviesList() {
        this.moviesList = new ArrayList<>();
    }

    public void addMovie(String title, int[] ratings) {
        moviesList.add(new Movie(title, ratings));
    }

    public List<Movie> top10ByAvgRating() {
        return moviesList.stream()
                .sorted(
                        Comparator.comparing(Movie::getAverageRating)
                                .reversed()
                                .thenComparing(Movie::getName)
                )
                .limit(10)
                .collect(Collectors.toList());
    }


    public List<Movie> top10ByRatingCoef() {
        int maxRatings = moviesList.stream()
                .mapToInt(Movie::getNumberOfRatings)
                .max()
                .orElse(0);


        return moviesList.stream()
                .sorted(
                        Comparator.comparingDouble(
                                (Movie m) -> m.getAverageRating() * m.getNumberOfRatings() / (double) maxRatings)
                                .reversed()
                                .thenComparing(Movie::getName)
                )
                .limit(10)
                .collect(Collectors.toList());
    }
}

