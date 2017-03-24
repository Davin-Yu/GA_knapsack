package main;
import java.util.*;

public class GA_Solution 
{
    int No_pop = 8;
    int StCountLoop = 1000;
    
    public long Cacu_fx(boolean a[], int value[])
    {
        long Ans = 0;
        for (int i=0;i<a.length;i++)
        {
            if (a[i])
            {
                Ans = Ans + value[i];
            }
        }            
        return Ans;
    }
    
    public boolean All_True (boolean a[])
    {
        for (int i=0;i<a.length;i++)
        {
            if (! a[i])
            {
                return false;
            }
        }
        return true;
    }
    
    public void Random_Initialize (boolean popu[][], DataGenerator Data)
    {
        int randn = 0;
        for (int i=0;i<No_pop;i++)
            for (int j=0;j<Data.n;j++)
            {
                popu[i][j] = false;
            }
        
        for (int i=0;i<No_pop;i++)
        {
            boolean flag = true;
            while (flag)
            {
                randn = (int) (Math.random()*Data.n);
                if (popu[i][randn])
                {
                    flag = false;
                }
                popu[i][randn]=true;
                if (Cacu_fx(popu[i],Data.weight)> Data.T)
                {
                    popu[i][randn]=false;
                    flag = false;
                }
                
                if (All_True(popu[i]))
                {
                    flag = false;
                }
            }
        }
    }
     
    public void CrossOver (boolean p1[], boolean p2[], boolean offSpring[],DataGenerator Data)
    {
        for (int i=0;i<offSpring.length;i++)
        {
            if (((int) (Math.random()*2)) !=1)
            {
                offSpring[i] = p1[i];
            }else
            {
                offSpring[i] = p2[i];
            }
                        
            if (((int) (Math.random()*100)) <3)
            {
                offSpring[i] = ! offSpring[i];
            }
            
            if (Cacu_fx(offSpring,Data.weight)>Data.T)
            {
                offSpring[i] = false;
            }
        }
    }
    
    public void Selection (boolean popu[][], DataGenerator Data)
    {
        boolean offSpring[][] = new boolean [No_pop][Data.n];
        for (int i=0; i<No_pop; i++)
            for (int j=0; j<Data.n; j++)
            {
                offSpring[i][j] = false;
            }
        
        //Elitism
        long BigFx = Cacu_fx(popu[0],Data.value);
        int BigNum = 0;
        for (int i=1;i<No_pop;i++)
        {
            if (Cacu_fx(popu[i],Data.value)>BigFx);
            {
                BigFx = Cacu_fx(popu[i],Data.value);
                BigNum = i;
            }
        }
        offSpring[0] = Arrays.copyOf(popu[BigNum], popu[BigNum].length);
        
        //Rank Selection
        long rankFx[] = new long [No_pop];
        int rankNum[] = new int [No_pop];
        for (int i=0;i<No_pop;i++)
        {
            rankFx[i] = -1;
        }
        
        for (int i=0;i<No_pop;i++)
        {
            long temp = Cacu_fx (popu[i],Data.value);
            int j = 0;
            while (temp < rankFx[j])
            {
                j++;
            }
            rankFx[j] = temp;
            rankNum[j] = i;
        }
        int countRou = 0;
        int rouWheel[] = new int [25+15*2+5*3+No_pop-6];
        int k = 0;
        while (k<No_pop)
        {
            switch (k)
            {
                case 0: for (int i=countRou;i<countRou+25;i++) { rouWheel[i] = rankNum[k]; }
                        countRou += 25;
                        break;
                case 1: case 2: for (int i=countRou;i<countRou+15;i++) { rouWheel[i] = rankNum[k]; }
                                countRou += 15;
                                break;
                case 3: case 4: case 5: for (int i=countRou;i<countRou+5;i++) { rouWheel[i] = rankNum[k]; }
                                        countRou += 5;
                                        break;
                default: rouWheel[countRou] = rankNum[k];
                         countRou ++;
                         break;
            }
            k++;
        }
        
        int startPoint = 0;
        for (int i=0;i<No_pop-1;i++)
        {
            startPoint = (startPoint + (int) (Math.random()*countRou)) % countRou;
            int _p1 = rouWheel[startPoint];
            startPoint = (startPoint + (int) (Math.random()*countRou)) % countRou;
            int _p2 = rouWheel[startPoint];
            
            for (int j=1;j<No_pop;j++)
            {
                CrossOver(popu[_p1],popu[_p2],offSpring[j], Data);
            }            
        }
        
        popu = offSpring;
    }
    
    public long GA_Ans(DataGenerator Data)
    {
        boolean popu[][] = new boolean [No_pop][Data.n];
        Random_Initialize(popu, Data);
        int countLoop = 0;
        long BestFx = Cacu_fx(popu[1], Data.value);
        while (countLoop<StCountLoop)
        {
            Selection (popu, Data);
            for (int i=0; i<No_pop; i++)
            {
                if (BestFx < Cacu_fx(popu[i], Data.value))
                {
                    countLoop = 0;
                    BestFx = Cacu_fx(popu[i], Data.value);
                }
            }
            countLoop++;
        }        
        return BestFx;
    }
}
