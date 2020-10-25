package com.webapp.notifier.config;

import org.springframework.stereotype.Component;

@Component
public class DoublyLinkedList {

    class Node{
        String data;
        Node previous;
        Node next;

        public Node(String data) {
            this.data = data;
        }
    }

    Node head, tail = null;

    public void addNode(String data) {
        Node newNode = new Node(data);

        if(head == null) {
            head = tail = newNode;

            head.previous = null;
            tail.next = null;
        }
        else {
            tail.next = newNode;

            newNode.previous = tail;
            tail = newNode;
            tail.next = null;
        }
    }

    public String getFirstNode() {
        Node current = head;
        if(head == null) {
            return null;
        }
        else{
            head = head.next;
            current.next=null;
            if(head!=null)
                head.previous = null;
            return current.data;
        }
    }

}
