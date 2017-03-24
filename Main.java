package main;

public class Main 
{  
    public static void main(String[] args) 
    {
        DataGenerator _Data = new DataGenerator();
        _Data.Generator();
        System.out.println("n="+_Data.n);
        System.out.println("T="+_Data.T);
        
        DP_Solution dp = new DP_Solution();
        long dp_Ans = dp.DP_Ans(_Data);
        System.out.println("DP Solution = "+dp_Ans);
        
        GA_Improve ga = new GA_Improve();
        long ga_Ans = ga._main(_Data);
        System.out.println("GA Solution = "+ga_Ans);
    }   
}
