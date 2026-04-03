package myLibrary.models;

import java.util.function.Predicate;

/**
 * Strategy pattern for book search criteria.
 */
public class SearchCriteria implements Predicate<Book> {
    public enum SearchType {
        TITLE,
        AUTHOR,
        TAG,
        GENRE
    }

    private final SearchType type;
    private final String value;

    public SearchCriteria(SearchType type, String value) {
        this.type = type;
        this.value = value.toLowerCase();
    }

    @Override
    public boolean test(Book book) {
        switch (type) {
            case TITLE:
                return book.getTitle().toLowerCase().contains(value);
            case AUTHOR:
                return book.getAuthor().toLowerCase().contains(value);
            case TAG:
                return book.getTags().stream()
                        .anyMatch(tag -> tag.toLowerCase().contains(value));
            case GENRE:
                return book.getGenre().toLowerCase().contains(value);
            default:
                return false;
        }
    }
}
