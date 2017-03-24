package main;

public class DoubleLinkedList {
    
    private DoubleLinkedList _pre = null;
    private DoubleLinkedList _next = null;
    private int _value = 0;
    DoubleLinkedList()
    {
    }
    
    public void setPre(DoubleLinkedList pre)
    {
        _pre = pre;
    }
    
    public DoubleLinkedList getPre()
    {
        return this._pre;
    }
    
    public void setNext(DoubleLinkedList next)
    {
        _next = next;
    }
    
    public DoubleLinkedList getNext()
    {
        return this._next;
    }
    
    public void setX(int value)
    {
        _value = value;
    }
    
    public int getX()
    {
        return this._value;
    }
    
}
