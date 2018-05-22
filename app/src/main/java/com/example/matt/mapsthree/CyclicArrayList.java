package com.example.matt.mapsthree;

import java.util.ArrayList;

/**
 * Created by matt on 5/22/18.
 *
 * CyclicArrayList is a class that acts exactly like a normal ArrayList except numbers outside of
 * it's index range access the index of that number in the mod of whatever size the list is. In
 * other words, the list index wraps so that list.get(-1) is the same thing as
 * list.get(list.size()-1) and list.get(list.size()) is the same thing as list.get(0)
 */

public class CyclicArrayList<E> extends ArrayList<E> {
    private static final long serialVersionUID = 1L;

    public E get(int index)
    {
        index = modulus(index, size());
        return super.get(index);
    }

    private int modulus(int number, int mod)
    {
        return ((number % mod) + mod) % mod;
    }
}
