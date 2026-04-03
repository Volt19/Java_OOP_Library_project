package myLibrary.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


/**
 * Value object representing a book in the library.
 * Immutable after creation.
 */
public class Book implements Serializable {
    private final String isbn;
    private final String title;
    private final String author;
    private final String genre;
    private final String description;
    private final int publicationYear;
    private final List<String> tags;
    private final double rating;

    /**
     * Builder pattern for Book creation.
     */
    public static class Builder {
        private final String isbn;
        private final String title;
        private final String author;
        private String genre = "";
        private String description = "";
        private int publicationYear = 0;
        private List<String> tags = new ArrayList<>();
        private double rating = 0.0;

        public Builder(String isbn, String title, String author) {
            this.isbn = Objects.requireNonNull(isbn, "ISBN cannot be null");
            this.title = Objects.requireNonNull(title, "Title cannot be null");
            this.author = Objects.requireNonNull(author, "Author cannot be null");
        }

        public Builder genre(String genre) {
            this.genre = genre;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder publicationYear(int year) {
            this.publicationYear = year;
            return this;
        }

        public Builder tags(List<String> tags) {
            this.tags = new ArrayList<>(tags);
            return this;
        }

        public Builder rating(double rating) {
            if (rating < 0.0 || rating > 5.0) {
                throw new IllegalArgumentException("Rating must be between 0.0 and 5.0");
            }
            this.rating = rating;
            return this;
        }

        public Book build() {
            return new Book(this);
        }
    }

    private Book(Builder builder) {
        this.isbn = builder.isbn;
        this.title = builder.title;
        this.author = builder.author;
        this.genre = builder.genre;
        this.description = builder.description;
        this.publicationYear = builder.publicationYear;
        this.tags = Collections.unmodifiableList(new ArrayList<>(builder.tags));
        this.rating = builder.rating;
    }

    // Getters
    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public String getDescription() {
        return description;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public List<String> getTags() {
        return tags;
    }

    public double getRating() {
        return rating;
    }

    /**
     * Checks if the book matches the given search criteria.
     */
    public boolean matches(SearchCriteria criteria) {
        return criteria.test(this);
    }

    @Override
    public String toString() {
        return String.format("ISBN: %s, Title: %s, Author: %s, Genre: %s, Year: %d, Rating: %.1f",
                isbn, title, author, genre, publicationYear, rating);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return isbn.equals(book.isbn);
    }

    @Override
    public int hashCode() {
        return isbn.hashCode();
    }
}
