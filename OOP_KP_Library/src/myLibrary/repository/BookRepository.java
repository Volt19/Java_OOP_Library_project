package myLibrary.repository;

import myLibrary.models.Book;
import myLibrary.models.SearchCriteria;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Repository pattern for book data management.
 * Encapsulates all data access operations for books.
 */
public class BookRepository implements Serializable {
    private final Map<String, Book> booksByIsbn;

    public BookRepository() {
        this.booksByIsbn = new HashMap<>();
    }

    public void add(Book book) {
        if (booksByIsbn.containsKey(book.getIsbn())) {
            throw new IllegalArgumentException("Book with ISBN " + book.getIsbn() + " already exists");
        }
        booksByIsbn.put(book.getIsbn(), book);
    }

    public void remove(String isbn) {
        if (!booksByIsbn.containsKey(isbn)) {
            throw new IllegalArgumentException("Book with ISBN " + isbn + " not found");
        }
        booksByIsbn.remove(isbn);
    }

    public Optional<Book> findByIsbn(String isbn) {
        return Optional.ofNullable(booksByIsbn.get(isbn));
    }

    public List<Book> findAll() {
        return new ArrayList<>(booksByIsbn.values());
    }

    public List<Book> search(SearchCriteria criteria) {
        return booksByIsbn.values().stream()
                .filter(criteria)
                .collect(Collectors.toList());
    }

    public List<Book> sort(Comparator<Book> comparator) {
        return booksByIsbn.values().stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    public boolean isEmpty() {
        return booksByIsbn.isEmpty();
    }

    public int count() {
        return booksByIsbn.size();
    }

    /**
     * Adds a collection of default books to the repository.
     * Useful for testing and demonstration purposes.
     */
    public void addDefaultBooks() {
        List<Book> defaultBooks = createDefaultBooks();
        for (Book book : defaultBooks) {
            try {
                add(book);
            } catch (IllegalArgumentException e) {
                // Book already exists, skip it
            }
        }
    }

    /**
     * Creates a list of default books for testing and demonstration.
     *
     * @return List of default books
     */
    private List<Book> createDefaultBooks() {
        List<Book> defaultBooks = new ArrayList<>();

        // 1. Classic Fiction
        defaultBooks.add(new Book.Builder("978-0-06-112008-4", "To Kill a Mockingbird", "Harper Lee")
                .genre("Classic Fiction")
                .description("A novel about racial injustice and moral growth in the American South.")
                .publicationYear(1960)
                .tags(Arrays.asList("classic", "race", "justice", "coming-of-age"))
                .rating(4.3)
                .build());

        // 2. Science Fiction
        defaultBooks.add(new Book.Builder("978-0-345-39180-3", "Dune", "Frank Herbert")
                .genre("Science Fiction")
                .description("Epic science fiction novel set in the distant future amidst a feudal interstellar society.")
                .publicationYear(1965)
                .tags(Arrays.asList("sci-fi", "epic", "politics", "ecology", "adventure"))
                .rating(4.2)
                .build());

        // 3. Fantasy
        defaultBooks.add(new Book.Builder("978-0-261-10201-5", "The Hobbit", "J.R.R. Tolkien")
                .genre("Fantasy")
                .description("A fantasy novel about the adventures of hobbit Bilbo Baggins.")
                .publicationYear(1937)
                .tags(Arrays.asList("fantasy", "adventure", "middle-earth", "dragons", "dwarves"))
                .rating(4.3)
                .build());

        // 4. Mystery
        defaultBooks.add(new Book.Builder("978-0-06-207356-5", "Murder on the Orient Express", "Agatha Christie")
                .genre("Mystery")
                .description("A detective novel featuring Hercule Poirot investigating a murder on a train.")
                .publicationYear(1934)
                .tags(Arrays.asList("mystery", "detective", "crime", "train", "whodunit"))
                .rating(4.2)
                .build());

        // 5. Programming/Technical
        defaultBooks.add(new Book.Builder("978-0-13-468599-1", "Effective Java", "Joshua Bloch")
                .genre("Programming")
                .description("A comprehensive guide to Java programming best practices.")
                .publicationYear(2018)
                .tags(Arrays.asList("java", "programming", "software", "development", "best-practices"))
                .rating(4.7)
                .build());

        // 6. Historical Fiction
        defaultBooks.add(new Book.Builder("978-0-7475-3269-9", "The Book Thief", "Markus Zusak")
                .genre("Historical Fiction")
                .description("A novel set in Nazi Germany, narrated by Death, about a girl who steals books.")
                .publicationYear(2005)
                .tags(Arrays.asList("historical", "ww2", "germany", "death", "books"))
                .rating(4.4)
                .build());

        // 7. Young Adult
        defaultBooks.add(new Book.Builder("978-0-439-02348-1", "The Hunger Games", "Suzanne Collins")
                .genre("Young Adult")
                .description("A dystopian novel about a televised fight to the death among teenagers.")
                .publicationYear(2008)
                .tags(Arrays.asList("dystopian", "young-adult", "survival", "rebellion", "trilogy"))
                .rating(4.3)
                .build());

        // 8. Biography
        defaultBooks.add(new Book.Builder("978-1-5011-0986-4", "Becoming", "Michelle Obama")
                .genre("Biography")
                .description("Memoir by former First Lady Michelle Obama.")
                .publicationYear(2018)
                .tags(Arrays.asList("biography", "memoir", "politics", "inspiration", "autobiography"))
                .rating(4.6)
                .build());

        // 9. Horror
        defaultBooks.add(new Book.Builder("978-0-385-12167-5", "The Shining", "Stephen King")
                .genre("Horror")
                .description("A horror novel about a family's winter at an isolated hotel where the father becomes influenced by supernatural forces.")
                .publicationYear(1977)
                .tags(Arrays.asList("horror", "supernatural", "hotel", "psychology", "isolation"))
                .rating(4.2)
                .build());

        // 10. Business
        defaultBooks.add(new Book.Builder("978-0-06-231500-7", "The Lean Startup", "Eric Ries")
                .genre("Business")
                .description("A book about lean manufacturing principles applied to startup companies.")
                .publicationYear(2011)
                .tags(Arrays.asList("business", "startup", "entrepreneurship", "innovation", "management"))
                .rating(4.1)
                .build());

        // 11. Philosophy
        defaultBooks.add(new Book.Builder("978-0-140-44327-6", "Meditations", "Marcus Aurelius")
                .genre("Philosophy")
                .description("A series of personal writings by the Roman Emperor Marcus Aurelius, recording his private notes and ideas on Stoic philosophy.")
                .publicationYear(180)
                .tags(Arrays.asList("philosophy", "stoicism", "self-help", "classic", "wisdom"))
                .rating(4.3)
                .build());

        // 12. Romance
        defaultBooks.add(new Book.Builder("978-1-5011-7379-4", "The Notebook", "Nicholas Sparks")
                .genre("Romance")
                .description("A love story set in the 1940s about a poor young man who falls in love with a rich young woman.")
                .publicationYear(1996)
                .tags(Arrays.asList("romance", "love-story", "drama", "heartwarming", "tearjerker"))
                .rating(4.1)
                .build());

        return defaultBooks;
    }

    /**
     * Clears all books and adds default books.
     * Useful for resetting to a known state.
     */
    public void resetToDefaultBooks() {
        booksByIsbn.clear();
        addDefaultBooks();
    }

    /**
     * Checks if the repository has any books.
     * If empty, adds default books.
     */
    public void ensureDefaultBooks() {
        if (isEmpty()) {
            addDefaultBooks();
        }
    }
}