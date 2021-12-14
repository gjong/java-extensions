package com.jongsoft.lang.collection;

import com.jongsoft.lang.Collections;
import com.jongsoft.lang.control.Optional;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TreeSetTest {

    @Test
    void rootOnly() {
        Tree<String> stringTree = Collections.Tree("Root node", "My first node");

        assertThat(stringTree.label()).isEqualTo("Root node");
        assertThat(stringTree.isLeaf()).isTrue();
        assertThat(stringTree.isSingleValued()).isTrue();
        assertThat(stringTree.children()).isEmpty();
    }

    @Test
    void nestedTree() {
        Tree<String> stringTree = Collections.Tree("Root node",
                 "Uber parent" ,
                 Collections.Set(Collections.Tree("Leaf 1", "Leaf node one"),
                         Collections.Tree("Leaf 2", "Leaf node two")));

        assertThat(stringTree.label()).isEqualTo("Root node");
        assertThat(stringTree.isSingleValued()).isFalse();
        assertThat(stringTree.isLeaf()).isFalse();
        assertThat(stringTree.children())
                .hasSize(2)
                .anySatisfy(child -> {
                    assertThat(child.label()).isEqualTo("Leaf 1");
                    assertThat(child.get()).isEqualTo("Leaf node one");
                })
                .anySatisfy(child -> {
                    assertThat(child.label()).isEqualTo("Leaf 2");
                    assertThat(child.get()).isEqualTo("Leaf node two");
                });
    }

    @Test
    void appendTree() {
        Tree<String> nodes = Collections.Tree("Root node", "Parent value")
           .appendChild("Child node 1", "First child node")
           .appendChild("Child node 2", "Second child node");

        assertThat(nodes.isRoot()).isTrue();
        assertThat(nodes.children())
                .hasSize(2)
                .anySatisfy(child -> {
                    assertThat(child.label()).isEqualTo("Child node 1");
                    assertThat(child.get()).isEqualTo("First child node");
                })
                .anySatisfy(child -> {
                    assertThat(child.label()).isEqualTo("Child node 2");
                    assertThat(child.get()).isEqualTo("Second child node");
                });
    }

    @Test
    void foldLeft() {
        String concat = Collections.Tree("Root node", "Parent value")
                .appendChild("Child node 1", "First child node")
                .appendChild("Child node 2", "Second child node")
                .foldLeft("start", (x, xs) -> xs + x);

        assertThat(concat).isEqualTo("Second child nodeFirst child nodeParent valuestart");
    }

    @Test
    void foldRight() {
        String concat = Collections.Tree("Root node", "Parent value")
                .appendChild("Child node 1", "First child node")
                .appendChild("Child node 2", "Second child node")
                .foldRight("start", (x, xs) -> xs + x);

        assertThat(concat).isEqualTo("startParent valueFirst child nodeSecond child node");
    }

    @Test
    void complexTree() {
        Tree<String> nodes = createTree();

        assertThat(nodes.isRoot()).isTrue();
        assertThat(nodes.children())
                .hasSize(2)
                .allSatisfy(child -> {
                    assertThat(child.children()).hasSize(2);
                });
        assertThat(nodes).hasToString("Tree: {value:Parent value, label: Root node, children:[Tree: {value:First child, label: ch-1-01, children:[Tree: {value:First sub child, label: ch-1-01-01, children:[Tree: {value:First sub sub child, label: ch-1-01-01-01, children:[]}, Tree: {value:Second sub sub child, label: ch-1-01-01-02, children:[]}]}, Tree: {value:Second sub child, label: ch-1-01-02, children:[]}]}, Tree: {value:Second child, label: ch-1-02, children:[Tree: {value:First sub child, label: ch-1-02-01, children:[]}, Tree: {value:Second sub child, label: ch-1-02-02, children:[]}]}]}");
    }

    @Test
    void first() {
        Optional<String> first_sub_child = createTree().first(t -> t.equalsIgnoreCase("First sub child"));

        assertThat(first_sub_child.isPresent()).isTrue();
        assertThat(first_sub_child.get()).isEqualTo("First sub child");
    }

    @Test
    void last() {
        Optional<String> first_sub_child = createTree().last(t -> t.equalsIgnoreCase("First sub child"));

        assertThat(first_sub_child.isPresent()).isTrue();
        assertThat(first_sub_child.get()).isEqualTo("First sub child");
    }

    @Test
    void map() {
        Tree<Integer> result = createTree().map(String::length);

        assertThat(result.isRoot()).isTrue();
        assertThat(result.label()).isEqualTo("Root node");
        assertThat(result.children())
                .hasSize(2)
                .anySatisfy(child -> {
                    assertThat(child.label()).isEqualTo("ch-1-01");
                    assertThat(child.get()).isEqualTo(11);
                })
                .anySatisfy(child -> {
                    assertThat(child.label()).isEqualTo("ch-1-02");
                    assertThat(child.get()).isEqualTo(12);
                });
    }

    private Tree<String> createTree() {
        Tree<String> nodes = Collections.Tree("Root node", "Parent value")
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

        return nodes;
    }
}
