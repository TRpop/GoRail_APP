package com.idiots.gorail;

/**
 * Created by MYHOME on 2018-04-09.
 */

public class Fifo<T> {

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public Fifo(){
        this.head = new Node<T>();
        this.tail = this.head;
        this.size = 0;
    }

    public boolean isEmpty(){
        return (this.size == 0);
    }

    public int getSize(){
        return this.size;
    }

    public boolean append(T value){
        this.tail.setNext(new Node<T>(value));
        this.tail = this.tail.getNext();
        this.size++;
        return true;
    }

    public T front(){
        if(isEmpty()){
            return null;
        } else {
            return this.head.getNext().getValue();
        }

    }

    public void clear(){
        this.head.setNext(null);
        this.tail = this.head;
    }

    public boolean popFront(){
        if(!isEmpty()){
            this.head.setNext(this.head.getNext().getNext());
            this.size--;
            return true;
        }else{
            return false;
        }
    }

    private class Node<T>{
        private Node<T> next;
        private T value;

        public Node(){
            setNext(null);
            setValue(null);
        }

        public Node(T value){
            setNext(null);
            setValue(value);
        }

        public Node<T> getNext(){
            return this.next;
        }

        public boolean setNext(Node<T> node){
            this.next = node;
            return true;
        }

        public boolean setValue(T value){
            this.value = value;
            return true;
        }

        public T getValue(){
            return this.value;
        }

    }
}
