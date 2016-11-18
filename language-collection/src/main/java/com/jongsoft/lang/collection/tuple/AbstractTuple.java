package com.jongsoft.lang.collection.tuple;

import com.jongsoft.lang.collection.Array;
import com.jongsoft.lang.collection.List;

class AbstractTuple implements Tuple {

    private List elements;

    AbstractTuple(Object...elements) {
        this.elements = Array.of(elements);
    }

    Object element(int index) {
        return elements.get(index);
    }

}
