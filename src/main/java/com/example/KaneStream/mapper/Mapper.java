package com.example.KaneStream.mapper;

public interface Mapper<A,B> {
    B mapFrom(A a);
    A mapTo(B b);
}
