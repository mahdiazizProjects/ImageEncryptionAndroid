package com.RobinCompany.imageenc;

import java.util.ArrayList;
import java.util.Random;

public class encryption
{
    public double[] domains;
    private double l1, l2, l3, m, gamma;
    public double domain;
    private double xzero;private double yzero;private double zzero;
    public encryption(double L1, double L2, double L3, double Xzero, double Yzero, double Zzero, double Gamma, double M)
    {
        domain = 4.0 / (65536-1);
        this.l1 = L1;
        this.l2 = L2;
        this.l3 = L3;
        this.m = M;
        this.gamma = Gamma;
        this.xzero = Xzero;
        this.yzero = Yzero;
        this.zzero = Zzero;
        this.domains = new double[65536];
    }
    public encryption()
    {
    	domains=new double[256];
    }
   	public int[] sq_rand_gen(int width, Random rr)
	{

		int[] sq_rand = new int[width];
		ArrayList<Integer> squence = new ArrayList<Integer>();
		for (int ii = 0; ii < width; ii++)
			squence.add(ii);
		for (int ii = 0; ii < sq_rand.length; ii++)
		{
			int count = squence.size();
			int rand = rr.nextInt(count);
			Object _fetch = squence.get(rand);
			int index=squence.indexOf(_fetch);
			squence.remove(index);
			sq_rand[ii] =(Integer)_fetch;
		}
		return sq_rand;
	}
    public double turning(double x, double y, double z)
    {
        double select;
        double temp1 = Math.abs(x);
        double temp2 = Math.abs(y);
        double temp3 = Math.abs(z);
        if (temp1 > temp2 && temp1 > temp3)
            select = x;
        else if (temp2 > temp1 && temp2 > temp3)
            select = y;
        else select = z;
        return (select);
    }
    public void restrict(double first, double domain)
    {
        domain = domain / 150;
        domains[0] = first;
        for (int i = 1; i < 256; i++)
        {
            domains[i] = domains[i - 1] + domain;
        }
    }
    public double key_maker(int key)
    {
       return domains[key];
    }
}