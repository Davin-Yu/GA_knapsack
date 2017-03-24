package main;

public class DP_Solution 
{
    private long getMax (long a, long b)
    {
        if (a>b)
        {
            return a;
        }else
        {
            return b;
        }
    }
    
    public long DP_Ans(DataGenerator Data)
    {
        long cost[][] = new long [Data.n][Data.T+1];
        for (int i=0;i<Data.n;i++)
            cost[i][0]=0;
        for (int j=Data.weight[0];j<=Data.T;j++)
            cost[0][j] = Data.value[0];
        for (int i=1;i<Data.n;i++)
            for (int j=1;j<=Data.T;j++)
            {
                if (j-Data.weight[i]<0)
                {
                    cost[i][j] = cost[i-1][j];
                }else
                {
                    cost[i][j] = getMax(cost[i-1][j-Data.weight[i]]+Data.value[i],cost[i-1][j]);
                }
            }
        return cost[Data.n-1][Data.T];
    }
}
