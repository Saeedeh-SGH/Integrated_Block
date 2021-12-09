package SoftwareTesting;

import MyPackage.*;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ali Shahbazi
 */
//other name: D-ART , FSCS: Fixed Size Candidate Set

import java.util.Random;

public class DARTFast {//for every dimention
    private SpacialSearch ss=new SpacialSearch();
    private Random rand=new Random();

    private void NextPoint(ArrayDouble2D seeds, int k){ //k:number of new random candidates

        int i,j;
        int Dim=seeds.GetDimension();

        double[][] candidates=new double[k][Dim];
        ArrayDouble1D fitness=new ArrayDouble1D(k);

        //rand=new Random();

        //generate candidate points
        for (i=0;i<k;i++) {
            for(j=0;j<Dim;j++){
		candidates[i][j]= this.rand.nextDouble();
            }
            fitness.Arr[i] = 2.0; // MAXIMUM-MINIMUM DISTANCE ~= Infinity for [0,1],[0,1] problem.
	}

        //find MAXIMUM-MINIMUM DISTANCE
        //A.MINIMUM DISTANCE
        for(j=0;j<k;j++) {
            fitness.Arr[j]=ss.Find1NNDist(candidates[j]);
        }
        
        //B.MAXIMUM DISTANCE
        int winnerIndex=fitness.GetMaxIndex();

        //insert new point into array
        for(j=0;j<Dim;j++){
            seeds.Arr[seeds.GetEndIndex()][j]=candidates[winnerIndex][j];
            
        }
        seeds.SetSize(seeds.GetSize()+1);
    }

    private double Distance(double[] x, double[] y){//distance in n dimension
        int dim=x.length;
        double dist=0;
        for(int i=0;i<dim;i++){
            dist+=((x[i]-y[i])*(x[i]-y[i]));
        }

        return (Math.sqrt(dist));

    }
    private double DistanceP2(double[] x, double[] y){//distance in n dimension
        int dim=x.length;
        double dist=0;
        for(int i=0;i<dim;i++){
            dist+=((x[i]-y[i])*(x[i]-y[i]));
        }

        return dist;

    }

    public void SetPoints(ArrayDouble2D data, int k){//k:number of new random candidates
        int i,j;
        int Dim=data.GetDimension();
        int Size=data.GetSize();

        this.rand=new Random();
        
        //set first random point
        for(j=0;j<Dim;j++){
            data.Arr[data.GetStartIndex()][j]= this.rand.nextDouble();
        }
        data.SetSize(1);

        //preprocess
        ss.Preprocess(data,ss.CalcStepNum(Size/2,data.GetDimension()));
        //
        for(i=1;i<Size;i++){
            this.NextPoint(data, k);
            ss.Add();
        }


    }

    public void SetPoints(ArrayDouble2D data){//k:number of new random candidates
        this.SetPoints(data, 10);//k=10 Andrew paper, Chen et al. recommended a candidate set size of k = 10

    }

}
