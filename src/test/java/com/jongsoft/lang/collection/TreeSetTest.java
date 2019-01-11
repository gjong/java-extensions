package com.jongsoft.lang.collection;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import com.jongsoft.lang.API;

public class TreeSetTest {

    @Test
    public void rootOnly() {
        Tree<String> stringTree = API.Tree("Root node", "My first node");

        assertThat(stringTree.label(), equalTo("Root node"));
        assertThat(stringTree.leaf(), equalTo(true));
        assertThat(stringTree.children().isEmpty(), equalTo(true));
    }

    @Test
    public void nestedTree() {
        Tree<String> stringTree = API.Tree("Root node",
                 "Uber parent" ,
                 API.Set(API.Tree("Leaf 1", "Leaf node one"),
                         API.Tree("Leaf 2", "Leaf node two")));

        assertThat(stringTree.label(), equalTo("Root node"));
        assertThat(stringTree.leaf(), equalTo(false));
        assertThat(stringTree.children().size(), equalTo(2));
        assertThat(stringTree.children().get(0).label(), equalTo("Leaf 1"));
        assertThat(stringTree.children().get(0).get(), equalTo("Leaf node one"));
        assertThat(stringTree.children().get(1).label(), equalTo("Leaf 2"));
        assertThat(stringTree.children().get(1).get(), equalTo("Leaf node two"));
    }

}
