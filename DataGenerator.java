package main;

public class DataGenerator 
{
    public int n, T;
    public int weight[],value[];
    public int maxn = 1000;
    public int maxT = 2000;
    private int maxWeight = 100;
    private int maxValue = 100;
    
    public void Generator()
    {
        n = (int) (Math.random()*maxn) + 1;
        T = (int) (Math.random()*maxT) + 1;
        weight = new int[n];
        value = new int[n];
        for (int i=0;i<n;i++)
        {
            weight[i] = (int) (Math.random()*maxWeight) + 1;
            value[i] = (int) (Math.random()*maxValue) + 1;
        }
             
        for (int i=0; i<n; i++) { System.out.print(value[i] + " "); }
        System.out.println();
        for (int i=0; i<n; i++) { System.out.print(weight[i] + " "); }
        System.out.println();
        
    }
}
