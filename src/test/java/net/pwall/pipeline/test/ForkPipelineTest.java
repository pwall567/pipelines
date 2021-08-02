package net.pwall.pipeline.test;

import java.util.List;

import org.junit.Test;

import net.pwall.pipeline.Filter;
import net.pwall.pipeline.ForkPipeline;
import net.pwall.pipeline.ListAcceptor;
import static org.junit.Assert.assertEquals;

public class ForkPipelineTest {

    @Test
    public void shouldForkPipeline() throws Exception {
        ListAcceptor<String> listAcceptor1 = new ListAcceptor<>();
        Filter<String, List<String>> filter1 = new Filter<>(listAcceptor1, a -> a.startsWith("A"));
        ListAcceptor<String> listAcceptor2 = new ListAcceptor<>();
        Filter<String, List<String>> filter2 = new Filter<>(listAcceptor2, a -> !a.startsWith("A"));
        ForkPipeline<String, List<String>> fork = new ForkPipeline<>(filter1, filter2);
        fork.accept("Abc");
        fork.accept("def");
        fork.accept("AAA");
        fork.accept("BBB");
        List<String> list1 = filter1.getResult();
        assertEquals(2, list1.size());
        assertEquals("Abc", list1.get(0));
        assertEquals("AAA", list1.get(1));
        List<String> list2 = filter2.getResult();
        assertEquals(2, list2.size());
        assertEquals("def", list2.get(0));
        assertEquals("BBB", list2.get(1));
    }

}
