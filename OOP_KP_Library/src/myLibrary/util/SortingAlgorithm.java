package myLibrary.util;

import myLibrary.models.Book;

import java.util.Comparator;
import java.util.List;

/**
 * Strategy pattern for sorting algorithms.
 * Implements Merge Sort as required.
 */
public class SortingAlgorithm {

    public static void mergeSort(List<Book> books, Comparator<Book> comparator) {
        if (books.size() <= 1) return;

        int mid = books.size() / 2;
        List<Book> left = new java.util.ArrayList<>(books.subList(0, mid));
        List<Book> right = new java.util.ArrayList<>(books.subList(mid, books.size()));

        mergeSort(left, comparator);
        mergeSort(right, comparator);

        merge(books, left, right, comparator);
    }

    private static void merge(List<Book> result, List<Book> left,
                              List<Book> right, Comparator<Book> comparator) {
        int i = 0, j = 0, k = 0;

        while (i < left.size() && j < right.size()) {
            if (comparator.compare(left.get(i), right.get(j)) <= 0) {
                result.set(k++, left.get(i++));
            } else {
                result.set(k++, right.get(j++));
            }
        }

        while (i < left.size()) {
            result.set(k++, left.get(i++));
        }

        while (j < right.size()) {
            result.set(k++, right.get(j++));
        }
    }
}
