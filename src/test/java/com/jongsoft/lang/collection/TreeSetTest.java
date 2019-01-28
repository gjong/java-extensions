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
        assertThat(stringTree.isLeaf(), equalTo(true));
        assertThat(stringTree.children().isEmpty(), equalTo(true));
    }

    @Test
    public void nestedTree() {
        Tree<String> stringTree = API.Tree("Root node",
                 "Uber parent" ,
                 API.Set(API.Tree("Leaf 1", "Leaf node one"),
                         API.Tree("Leaf 2", "Leaf node two")));

        assertThat(stringTree.label(), equalTo("Root node"));
        assertThat(stringTree.isLeaf(), equalTo(false));
        assertThat(stringTree.children().size(), equalTo(2));
        assertThat(stringTree.children().get(0).label(), equalTo("Leaf 1"));
        assertThat(stringTree.children().get(0).get(), equalTo("Leaf node one"));
        assertThat(stringTree.children().get(1).label(), equalTo("Leaf 2"));
        assertThat(stringTree.children().get(1).get(), equalTo("Leaf node two"));
    }

    @Test
    public void appendTree() {
        Tree<String> nodes = API.Tree("Root node", "Parent value")
           .appendChild("Child node 1", "First child node")
           .appendChild("Child node 2", "Second child node");

        assertThat(nodes.isRoot(), equalTo(true));
        assertThat(nodes.children().size(), equalTo(2));
        assertThat(nodes.children().get(0).label(), equalTo("Child node 1"));
        assertThat(nodes.children().get(0).get(), equalTo("First child node"));
        assertThat(nodes.children().get(1).label(), equalTo("Child node 2"));
        assertThat(nodes.children().get(1).get(), equalTo("Second child node"));
    }

    @Test
    public void complexTree() {
        Tree<String> nodes = API.Tree("Root node", "Parent value")
           .appendChild("ch-1-01", "First child")
           .appendChild("ch-1-02", "Second child");

        nodes.getChild("ch-1-01")
             .getOrThrow(IllegalStateException::new)
             .appendChild("ch-1-01-01", "First sub child")
             .appendChild("ch-1-01-02", "Second sub child");

        nodes.getChild("ch-1-02")
             .getOrThrow(IllegalStateException::new)
             .appendChild("ch-1-02-01", "First sub child")
             .appendChild("ch-1-02-02", "Second sub child");

        nodes.getChild("ch-1-01")
             .map(c -> c.getChild("ch-1-01-01").get())
             .getOrThrow(IllegalStateException::new)
             .appendChild("ch-1-01-01-01", "First sub sub child")
             .appendChild("ch-1-01-01-02", "Second sub sub child");

        assertThat(nodes.isRoot(), equalTo(true));
        assertThat(nodes.children().size(), equalTo(2));
        assertThat(nodes.children().get(0).children().size(), equalTo(2));
        assertThat(nodes.children().get(1).children().size(), equalTo(2));
        assertThat(nodes.children().get(0).children().get(0).children().size(), equalTo(2));
        assertThat(nodes.toString(), equalTo("Tree: {value:Parent value, label: Root node, children:[Tree: {value:First child, label: ch-1-01, children:[Tree: {value:First sub child, label: ch-1-01-01, children:[Tree: {value:First sub sub child, label: ch-1-01-01-01, children:[]}, Tree: {value:Second sub sub child, label: ch-1-01-01-02, children:[]}]}, Tree: {value:Second sub child, label: ch-1-01-02, children:[]}]}, Tree: {value:Second child, label: ch-1-02, children:[Tree: {value:First sub child, label: ch-1-02-01, children:[]}, Tree: {value:Second sub child, label: ch-1-02-02, children:[]}]}]}"));
    }

}
