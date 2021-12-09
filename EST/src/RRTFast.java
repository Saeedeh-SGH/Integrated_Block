package SoftwareTesting;

import MyPackage.*;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ali shahbazi
 */

import java.util.Random;

public class RRTFast {//for every dimention

    private SpacialSearch ss=new SpacialSearch();
    private Random rand;

    private boolean NextPoint(ArrayDouble2D seeds, double radius){

        int i,j;
        int Dim=seeds.GetDimension();
        long counter = 0;
        double[] candidate=new double[Dim];
        boolean found=false;
        


        //find new point
        while(counter<(100*seeds.GetSize())){

            for(j=0;j<Dim;j++){
		candidate[j]= rand.nextDouble();
            }

            found=false;
            if (ss.Find1NNDist(candidate) >= radius){
                found=true;
                break;
            }

            counter++;
        }

        //insert new point into array
        if(found==true){
            for(j=0;j<Dim;j++){
                seeds.Arr[seeds.GetEndIndex()][j]=candidate[j];
            }
            seeds.SetSize(seeds.GetSize()+1);
        }
        return found;

    }

    private double Distance(double[] x, double[] y){//distance in n dimension
        int dim=x.length;
        double dist=0;
        for(int i=0;i<dim;i++){
            dist+=((x[i]-y[i])*(x[i]-y[i]));
        }

        return (Math.sqrt(dist));

    }

    public void SetPoints(ArrayDouble2D data, double coverage_ratio){//
//A coverage ratio R = 1.5 has been recommended.
//coverage ratio: the sum of the areas of all exclusion zones related to the area of the input domain (1)—disregarding overlapping

        int i,j;
        int Dim=data.GetDimension();
        int Size=data.GetSize();
        rand=new Random();
        //set first random point
        for(j=0;j<Dim;j++){
            data.Arr[data.GetStartIndex()][j]= rand.nextDouble();
        }
        data.SetSize(1);

        //preprocess
        ss.Preprocess(data,ss.CalcStepNum(Size/2, data.GetDimension()));
        //
        int k=1;
        for(i=1;i<Size;i++){
            double radius =  Math.sqrt(coverage_ratio/(Math.PI*(k+1)));
            //Andrew code:
            //double radius =  Math.sqrt(coverage_ratio/(2*Math.PI*(k+1)));
            k++;
            if(this.NextPoint(data, radius)==false)
                i--;
            else
                ss.Add();
        }

    }

    public void SetPoints(ArrayDouble2D data){
        this.SetPoints(data, 1.5);///A coverage ratio R = 1.5 has been recommended.
    }

}
