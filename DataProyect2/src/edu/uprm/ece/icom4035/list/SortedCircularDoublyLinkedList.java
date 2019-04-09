package edu.uprm.ece.icom4035.list;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class SortedCircularDoublyLinkedList<E extends Comparable<E>> implements SortedList<E> {
	private int size;
	private Node<E> header;
	
	public SortedCircularDoublyLinkedList () {
		size = 0;
		header = new Node(null,header,header);
	}
	private static class Node<E> 
	{
		private E element;
		private Node<E> next;
		private Node<E> prev;
		
		public Node(E e, Node<E> n) {element = e; setNext(n); setPrev(null);}
		
		public Node(E e, Node<E> p, Node<E> n) {element = e; setPrev(p); setNext(n);}
		
		public Node() {element = null; next = null;}
		public Node(E element) {this.element = element;next = null;prev = null;}
		
		public boolean hasNext() {return this.getNext().getElement() != null;}
		public boolean hasPrev() {return this.getPrev().getElement() != null;}
		
		public E getElement() {return element;}
		public void setElement(E e) {element = e;}
		
		public Node<E> getNext() {return next;}
		public void setNext(Node<E> n) {next = n;}
	
		public void clear() {next = null;element = null; prev = null;}
		
		public Node<E> getPrev() {return prev;}
		public void setPrev(Node<E> prev) {this.prev = prev;}
	}
	
	
	

	public boolean add(E obj) {

		Node<E> newNode = new Node<E>(obj);
		if (this.isEmpty()) {
			newNode.setNext(header);
			newNode.setPrev(header);
			header.setNext(newNode);
			header.setPrev(newNode);
			size++;
			return true;

		} else {
			Node<E> curr = header.getNext();
			E elementNewNode = newNode.getElement();
			while (curr != header) {
				E currNodeElement = curr.getElement();
				if (currNodeElement.compareTo(elementNewNode) > 0)
					break;
				curr = curr.getNext();
				;
			}

			if (curr == header) {
				newNode.setNext(header);
				newNode.setPrev(header.getPrev());
				header.getPrev().setNext(newNode);
				header.setPrev(newNode);
			}

			else if (curr.getPrev() == header) {
				newNode.setPrev(header);
				newNode.setNext(curr);
				header.setNext(newNode);
				curr.setPrev(newNode);

			}

			else {
				newNode.setNext(curr);
				newNode.setPrev(curr.getPrev());
				curr.getPrev().setNext(newNode);
				curr.setPrev(newNode);
			}

		}

		size++;
		return true;
	}	
	
	@Override
	public int size() {return this.size;}

	@Override
	public boolean remove(E obj) {
		Node<E> currentNode = this.header.getNext();

		while (!currentNode.equals(header)) {
			if (currentNode.getElement().equals(obj)) {
				Node<E> prev = currentNode.getPrev();
				Node<E> next = currentNode.getNext();
				prev.setNext(next);
				next.setPrev(prev);
				currentNode.clear();
				size--;
				return true;
			} else {
				currentNode = currentNode.getNext();
			}
		}
		return false;
	}
	

	@Override
	public boolean remove(int index) {return this.remove(this.get(index));}

	@Override
	public int removeAll(E obj) {
		Node<E> currentNode = this.header.getNext();
		int count = 0;
		while (!currentNode.equals(header)) {
			if (currentNode.getElement().equals(obj)) {
				Node<E> prev = currentNode.getPrev();
				Node<E> next = currentNode.getNext();
				prev.setNext(next);
				next.setPrev(prev);
				currentNode = next;
				size--;
				count ++;
			} else {
				currentNode = currentNode.getNext();
			}
		}
		return count;
	}

	@Override
	public E first() { return header.getNext().getElement();}

	@Override
	public E last() {return header.getPrev().getElement();}

	@Override
	public E get(int index) throws IndexOutOfBoundsException {
		if (index < 0 || index >= this.size) {
			throw new IndexOutOfBoundsException("Index out of bounds");
		}

		Node<E> curr = header;
		for (int i = 0; i <= index; i++)
			curr = curr.getNext();
		return curr.getElement();
	}

	@Override
	public void clear() {
		while (!this.isEmpty())
			this.remove(0);
	}

	@Override
	public boolean contains(E e) {
		Node<E> currentNode = this.header.getNext();
		while (!currentNode.equals(header)) {
			if (currentNode.getElement().equals(e)) {
				return true;
			} else {
				currentNode = currentNode.getNext();
			}
		}
		return false;
	}

	@Override
	public boolean isEmpty() {return this.size == 0;}



	@Override
	public int firstIndex(E e) {
		int count = 0;
		Node<E> currentNode = this.header.getNext();
		while (!currentNode.equals(this.header)) {
			if (currentNode.getElement().equals(e)) {
				return count;
			} else {
				currentNode = currentNode.getNext();
				count++;
			}
		}
		return -1;
	}

	@Override
	public int lastIndex(E e) {
		int count = this.size-1;
		Node<E> currentNode = this.header.getPrev();
		while (!currentNode.equals(this.header)) {
			if (currentNode.getElement().equals(e)) {
				return count;
			} else {
				currentNode = currentNode.getPrev();
				count--;
			}
		}
		return -1;
	}
	
	@Override
	public Iterator<E> iterator() {return new ForwardListIterator();}
	
	@Override
	public Iterator<E> iterator(int index) {return new ForwardListIterator(index);}

	@Override
	public ReverseIterator<E> reverseIterator() {return new BackwardListIterator();}

	@Override
	public ReverseIterator<E> reverseIterator(int index) {return new BackwardListIterator(index);}
	
	
	
	// Forward Iterator without index
		public class ForwardListIterator implements Iterator<E> {

			private Node<E> currentNode;

			ForwardListIterator() {
				this.currentNode = header.getNext();
			}
			ForwardListIterator(int index){
				this.currentNode = header.getNext();
				for(int i = 0; i < index; i++) {
					this.currentNode = this.currentNode.getNext();
				}
			}

			@Override
			public boolean hasNext() {
				return this.currentNode != header;
			}

			@Override
			public E next() throws NoSuchElementException {
				if (!hasNext()) {
					throw new NoSuchElementException("No more elements to iterate over.");
				}
				E res = currentNode.getElement();
				this.currentNode = this.currentNode.getNext();
				return res;

			}

		}

		public class BackwardListIterator implements ReverseIterator<E> {

			private Node<E> currentNode;

			BackwardListIterator() {
				this.currentNode = header.getPrev();
			}
			BackwardListIterator(int index) {
				this.currentNode = header.getPrev();
				for(int i = 0; i < index; i++) {
					this.currentNode = this.currentNode.getPrev();
				}
			}

			@Override
			public boolean hasPrevious() {
				return currentNode != header;
			
			}

			@Override
			public E previous() {
				if (!hasPrevious()) {
					throw new NoSuchElementException("No more elements");
				}
				E res = currentNode.getElement();
				this.currentNode = this.currentNode.getPrev();
			return res;
			}
		}

		
		
	}
