package main;

public class GA_Improve 
{
    private final int No_chro = 16;
    private final int loopMax = 2000;
    private DoubleLinkedList _chro[] = new DoubleLinkedList[No_chro];           //First element is empty
        
    private long cacuValue(DoubleLinkedList a,DataGenerator Data)
    {
        long ans = 0;
        while (a.getNext() != null)
        {
            ans = ans + Data.value[a.getNext().getX()];
            a = a.getNext();
        }
        return ans;
    }
    
    private long cacuWeight(DoubleLinkedList a,DataGenerator Data)
    {
        long ans = 0;
        while (a.getNext() != null)
        {
            ans = ans + Data.weight[a.getNext().getX()];
            a = a.getNext();
        }
        return ans;
    }
    
    private boolean conTains(DoubleLinkedList a, int num)
    {     
        while (a.getNext() != null)
        {
            if (a.getNext().getX() == num)
            {
                return true;
            }
            a = a.getNext();
        }
        return false;
    }
        
    private int chroLength(DoubleLinkedList a)
    {
        int num = 0;
        while (a.getNext() != null)
        {
            num++;
            a = a.getNext();
        }
        return num;
    }
    
    private void Initialize(DataGenerator Data)
    {
        for (int i=0;i<No_chro;i++)
        {
            _chro[i] = new DoubleLinkedList();
            boolean flag = true;
            int count = 0;
            while (flag)
            {
                int randomX = (int) (Math.random()*Data.n);
                if (!conTains(_chro[i],randomX))
                {
                    if (cacuWeight(_chro[i],Data) + Data.weight[randomX] > Data.T)
                    {
                        flag = false;
                    }else
                    {
                        while (_chro[i].getNext() !=null)
                        {
                            _chro[i] = _chro[i].getNext();
                        }
                        DoubleLinkedList newElement = new DoubleLinkedList();
                        newElement.setX(randomX);
                        newElement.setPre(_chro[i]);
                        _chro[i].setNext(newElement);
                        _chro[i] = _chro[i].getNext();
                        while (_chro[i].getPre() != null)
                        {
                            _chro[i] = _chro[i].getPre();
                        }
                    }
                }else if (chroLength(_chro[i]) == Data.n)
                {
                    flag = false;
                }
                
                if (count > 2*Data.n)
                {
                    flag =false;
                }
                count++;
            }
        }
    }
    
    private DoubleLinkedList Elitism(DataGenerator Data)
    {
        int firstPosition = 0;
        long firstfx = cacuValue(_chro[0],Data);
        for (int i=1;i<No_chro;i++)
        {
            if (firstfx<cacuValue(_chro[i],Data))
            {
                firstfx = cacuValue(_chro[i],Data);
                firstPosition = i;
            }
        }
        return _chro[firstPosition];
    }
    
    private int RankSelection(DataGenerator Data)
    {
        long chroT[] = new long[No_chro];
        int chroX[] = new int[No_chro];
        for (int i=0;i<No_chro;i++)
        {
            chroT[i] = cacuValue(_chro[i],Data);
            chroX[i] = i;
        }
        for (int i=0;i<No_chro;i++)
            for (int j=No_chro-1;j>i;j--)
            {
                if (chroT[j]>chroT[j-1])
                {
                    long tempt;
                    int tempx;
                    tempt = chroT[j]; chroT[j] = chroT[j-1]; chroT[j-1] = tempt;
                    tempx = chroX[j]; chroX[j] = chroX[j-1]; chroX[j-1] = tempx;
                }
            }
        
        int countRou = 0;
        int rouWheel[] = new int [25+15*2+5*3+No_chro-6];
        int k = 0;
        while (k<No_chro)
        {
            switch (k)
            {
                case 0: for (int i=countRou;i<countRou+25;i++) { rouWheel[i] = chroX[k]; }
                        countRou += 25;
                        break;
                case 1: case 2: for (int i=countRou;i<countRou+15;i++) { rouWheel[i] = chroX[k]; }
                                countRou += 15;
                                break;
                case 3: case 4: case 5: for (int i=countRou;i<countRou+5;i++) { rouWheel[i] = chroX[k]; }
                                        countRou += 5;
                                        break;
                default: rouWheel[countRou] = chroX[k];
                         countRou ++;
                         break;
            }
            k++;
        }
        int positionX = (int) (Math.random()*countRou);
        int AnsX = rouWheel[positionX];
        return AnsX;
    }
    
    private void Mutation(DoubleLinkedList a, DataGenerator Data)
    {
        int hah = chroLength(a);
        int pickPoint = (int) (Math.random()*chroLength(a));
        int changeTo = (int) (Math.random()*Data.n);
        if (conTains(a,changeTo))                             
        {
            while (a.getNext().getX() != changeTo)
            {
                a = a.getNext();
            }
            a.setNext(a.getNext().getNext());
            if (a.getNext() != null)
            {
                a.getNext().setPre(a);
            }
        }else if (((cacuWeight(a,Data))+Data.weight[changeTo])>Data.T)
        {
            int x = 0;
            if (a.getNext() != null)
            {
                while (x<pickPoint)
                {
                    a = a.getNext();
                    x++;
                }
                a.setNext(a.getNext().getNext());
                if (a.getNext() !=null)
                {
                    a.getNext().setPre(a);
                }
            }
        }else
        {
            for (int i=0;i<pickPoint;i++)
            {
                a = a.getNext();
            }
            if (a.getNext() !=null)
            {
                a.getNext().setX(changeTo);
            }
        }
    }
    
    private boolean CrossOver(DoubleLinkedList firParent,int cutP1, 
                                DoubleLinkedList secParent,int cutP2,DoubleLinkedList offSpring, DataGenerator Data)
    {
        boolean ifSelected[] = new boolean[Data.n];
        for (int i=0;i<Data.n;i++)
        {
            ifSelected[i] = false;
        }
        
        int count = 0;
        while (count<cutP1)
        {
            ifSelected[firParent.getNext().getX()] = true;
            firParent = firParent.getNext();
            count++;
        } 
        
        count = 0;
        int secPartLong = chroLength(secParent) - cutP2;
        while (secParent.getNext() != null)
        {
            secParent = secParent.getNext();
        }
        while (count<secPartLong)
        {
            ifSelected[secParent.getX()] = true;
            secParent = secParent.getPre();
            count++;
        }
        
        long totleWeight = 0;
        for (int i=0;i<Data.n;i++)
        {
            if (ifSelected[i])
            {
                totleWeight = totleWeight + Data.weight[i];
            }
        }
        
        if (totleWeight <= Data.T)
        {
            for (int i=0;i<Data.n;i++)
            {
                if (ifSelected[i])
                {
                    DoubleLinkedList newElement  = new DoubleLinkedList();
                    newElement.setX(i);
                    offSpring.setNext(newElement);
                    newElement.setPre(offSpring);
                    offSpring = offSpring.getNext();
                }
            }
            return true;
        }else
        {
            return false;
        }
    }
    
    public long _main(DataGenerator Data)
    {
        long Ans = 0;
        Initialize(Data);
        int loopNum = 0;
        while (loopNum < loopMax)
        {
            DoubleLinkedList offSpring[] = new DoubleLinkedList[No_chro];
            for (int i=0;i<No_chro;i++)
            {
                offSpring[i] = new DoubleLinkedList();
            }
            int dealWith = 0;
            offSpring[dealWith] = Elitism(Data);
            dealWith++;
            while (dealWith<No_chro)
            {
                int Choose = (int) (Math.random()*100);
                if (Choose<17)
                {
                    offSpring[dealWith] = _chro[RankSelection(Data)];
                    dealWith++;
                }else if (Choose<20)
                {
                    offSpring[dealWith] = _chro[RankSelection(Data)];
                    Mutation(offSpring[dealWith],Data);     
                    dealWith++;
                }else 
                {
                    int chroPar1 = RankSelection(Data);
                    int chroPar2 = RankSelection(Data);
                    while ((chroPar2 == chroPar1)&&(Data.n !=1))
                    {
                        chroPar2 = RankSelection(Data);
                    }                             
                    int cutPoint1 = (int) (Math.random()*chroLength(_chro[chroPar1]));
                    int cutPoint2 = (int) (Math.random()*chroLength(_chro[chroPar2]));
                    
                    if (CrossOver(_chro[chroPar1],cutPoint1,_chro[chroPar2],cutPoint2,offSpring[dealWith],Data))
                    {
                        dealWith++;
                    }
                    if (dealWith<No_chro)
                    {
                        if (CrossOver(_chro[chroPar2],cutPoint2,_chro[chroPar1],cutPoint1,offSpring[dealWith],Data))
                        {
                            dealWith++;
                        }
                    }
                }
            }
            _chro = offSpring;
            for (int i=0;i<No_chro;i++)
            {
                if (cacuValue(_chro[i],Data)>Ans)
                {
                    Ans = cacuValue(_chro[i],Data);
                }
            }
            loopNum++;
        }
        return Ans;
    }
}
