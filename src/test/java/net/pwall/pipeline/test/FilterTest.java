package net.pwall.pipeline.test;

import java.util.List;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import net.pwall.pipeline.Filter;
import net.pwall.pipeline.ListAcceptor;

public class FilterTest {

    @Test
    public void shouldFilterObjectsByPredicate() throws Exception {
        ListAcceptor<String> listAcceptor = new ListAcceptor<>();
        Filter<String, List<String>> filter = new Filter<>(listAcceptor, a -> a.startsWith("A"));
        filter.accept("Abc");
        filter.accept("def");
        filter.accept("AAA");
        filter.accept("BBB");
        List<String> list = filter.getResult();
        assertEquals(2, list.size());
        assertEquals("Abc", list.get(0));
        assertEquals("AAA", list.get(1));
    }

}
