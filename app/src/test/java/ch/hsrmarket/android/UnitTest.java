package ch.hsrmarket.android;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTest {

    @Test
    public void book_id(){
        Book b = new Book(3,"BB AA 99","Bob Marley");
        assertEquals(3,b.getId());
    }

    @Test
    public void book_isbn(){
        Book b = new Book(3,"BB AA 99","Bob Marley");
        b.setISBN("MM 99 33");

        assertEquals("MM 99 33",b.getISBN());
    }

    @Test
    public void book_author(){
        Book b = new Book(3,"BB AA 99","Bob Marley");
        b.setAuthor("P.Enis");

        assertEquals("P.Enis",b.getAuthor());
    }

}