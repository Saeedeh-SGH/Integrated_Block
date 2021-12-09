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

public class DART {//for every dimention

    private Random rand;

    private void NextPoint(ArrayDouble2D seeds, int n, int k){ //n: number of filled points, k:number of new random candidates

        int i,j;
        int Dim=seeds.GetDimension();

        double[][] candidates=new double[k][Dim];
        ArrayDouble1D fitness=new ArrayDouble1D(k);

        //Random rand=new Random();

        //generate candidate points
        for (i=0;i<k;i++) {
            for(j=0;j<Dim;j++){
		candidates[i][j]= this.rand.nextDouble();
            }
            fitness.Arr[i] = 2.0; // MAXIMUM-MINIMUM DISTANCE ~= Infinity for [0,1],[0,1] problem.
	}

        //find MAXIMUM-MINIMUM DISTANCE
        //A.MINIMUM DISTANCE
        double dist;

	for(i=0;i<n;i++){
            for(j=0;j<k;j++) {
		dist = this.Distance(candidates[j], seeds.Arr[i+seeds.GetStartIndex()]);
        	if (dist < fitness.Arr[j])
                    fitness.Arr[j]=dist;
            }
        }
        //B.MAXIMUM DISTANCE
        int winnerIndex=fitness.GetMaxIndex();

        //insert new point into array
        for(j=0;j<Dim;j++){
            seeds.Arr[n+seeds.GetStartIndex()][j]=candidates[winnerIndex][j];
        }

    }

    private double Distance(double[] x, double[] y){//distance in n dimension
        int dim=x.length;
        double dist=0;
        for(int i=0;i<dim;i++){
            dist+=((x[i]-y[i])*(x[i]-y[i]));
        }

        return (Math.sqrt(dist));

    }

    public void SetPoints(ArrayDouble2D data, int k){//k:number of new random candidates
        int i,j;
        int Dim=data.GetDimension();
        this.rand=new Random();
        
        //set first random point
        for(j=0;j<Dim;j++){
            data.Arr[data.GetStartIndex()][j]= this.rand.nextDouble();
        }

        //
        for(i=1;i<data.GetSize();i++)
            this.NextPoint(data, i, k);


    }

    public void SetPoints(ArrayDouble2D data){//k:number of new random candidates
        this.SetPoints(data, 10);//k=10 Andrew paper, Chen et al. recommended a candidate set size of k = 10

    }

}
