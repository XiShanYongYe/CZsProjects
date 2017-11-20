package org.nust.heroine.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 计算两个字符串之间的编辑距离
 * @author Administrator
 *
 */
public class StringDistance {
	public static int editDistance(List<String> source,List<String> target)
	{
		
        Map<String,Integer> map=new HashMap<String,Integer>();

        int []a=new int[source.size()+1];
        int []b=new int[target.size()+1];
        		
        int now=0;
        for(int i=0;i<source.size();i++)
        {
        	if(map.get(source.get(i))==null)
        	{
        		now++;
        		map.put(source.get(i), now);
        	}
        	a[i+1]=map.get(source.get(i));
        }
        for(int j=0;j<target.size();j++)
        {
        	if(map.get(target.get(j))==null)
        	{
        		now++;
        		map.put(target.get(j), now);
        	}
        	b[j+1]=map.get(target.get(j));
        }
		
		
        int len1=a.length-1,len2=b.length-1;
        
        int[][] dis= new int[len1+1][len2+1];
        
        int []da=new int[now+1];
        for(int i=1;i<=now;i++)
        	da[i]=0;
        
        for(int i=0;i<=len1;i++)
        	dis[i][0]=i;
        for(int j=0;j<=len2;j++)
        	dis[0][j]=j;
        int db,k,l;
        
        for(int i=1;i<=len1;i++)
        {
        	db=0;
        	for(int j=1;j<=len2;j++)
        	{
        		k=da[b[j]];
        		l=db;
        		int Min=99999;
        		if(a[i]==b[j])
        		{
        			db=j;
        			if(dis[i-1][j-1]<Min)
        				Min=dis[i-1][j-1];
        		}
        		if(dis[i-1][j]+1<Min)
        		{
    				Min=dis[i-1][j]+1;
        		}
        		if(dis[i][j-1]+1<Min)
        		{
    				Min=dis[i][j-1]+1;
        		}
        		if(k>0&&l>0)
        		{
        			int tem=dis[k-1][l-1]+(i-k-1)+1+(j-l-1);
        			if(tem<Min)
        				Min=tem;
        		}
        		dis[i][j]=Min;
        	}
        	da[a[i]]=i;
        }        
        return dis[len1][len2];
	}
}
