package ru.webrelab.layout_testing;

import java.util.*;

public class Tree<T> {
    private final T value;
    private Tree<T> parent;
    private final Set<Tree<T>> children = new HashSet<>();

    public Tree(T value) {
        this.value = value;
    }

    public Tree(T value, Tree<T> parent) {
        this.value = value;
        if (parent != null) {
            this.parent = parent;
            parent.addChild(this);
        }
    }

    /**
     * Метод выполняет очистку на каждом уровне вложенности от элементов, которые есть
     * на более глубоком уровне
     */
    public void removeDuplicateChildren() {
        children.removeIf(ch -> {
            for (Tree<T> e : ch.children) {
                if (e.findChild(ch.value) != null) return true;
            }
            return false;
        });
        children.forEach(Tree::removeDuplicateChildren);
    }

    public Tree<T> findChild(T value) {
        if (this.value.equals(value)) {
            return this;
        }
        for (Tree<T> child : children) {
            Tree<T> result = findChild(child.value);
            if (result != null) return result;
        }
        return null;
    }

    public void addChild(Tree<T> child) {
        children.add(child);
    }

    public T getValue() {
        return value;
    }

    public Tree<T> getParent() {
        return parent;
    }

    public Set<Tree<T>> getChildren() {
        return children;
    }

    public boolean hasParent() {
        return parent != null;
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }
}
