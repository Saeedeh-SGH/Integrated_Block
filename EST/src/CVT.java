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



public class CVT {
    
    public void CVT_Iterate(ArrayDouble2D data, ArrayDouble2D samples){
        int i;//move on one data dimensions
        int j;//move on different data
        int j2;
        //double energy=0;
        //double it_diff=0;

        int data_num=data.GetSize();
        int dimension=data.GetDimension();
        int SID1=data.GetStartIndex();
        int samples_num=samples.GetSize();
        int SIS1=samples.GetStartIndex();

        //copy the data  ////////////////////////////////////////////////////////////////////////////////////////////
        double[][] data1=new double[data_num][dimension];
        for ( j = 0; j < data_num; j++ ){
            for ( i = 0; i < dimension; i++ ){
                data1[j][i] = data.Arr[SID1+j][i];
            }
        }

        //count
        int[] count=new int[data_num];
        for ( j = 0; j < data_num; j++ ){
            count[j] = 1;
        }

        //////////
        for (j=SIS1;j<SIS1+samples_num;j++){
            //  Find the index of the nearest cell generator to each sample point.
            j2 = this.FindNearest(data1, data_num, dimension, samples.Arr[j]);
            for ( i = 0; i < dimension; i++ ){
                data.Arr[SID1+j2][i] = data.Arr[SID1+j2][i] + samples.Arr[j][i];
                //energy = energy +((data1[j2][i]-samples.Arr[j][i])*(data1[j2][i]-samples.Arr[j][i]));
            }

            count[j2] = count[j2] + 1;
        }
        //energy = energy / samples_num;

        //  Estimate the centroids.
        for ( j = SID1; j < SID1+data_num; j++ ){
            for ( i = 0; i < dimension; i++ )
                data.Arr[j][i] = data.Arr[j][i] /count[j-SID1];
        }

        //  Determine the sum of the distances between generators and centroids.
        /*
        it_diff = 0.0;
        for ( j = 0; j < data_num; j++ ){
            double term = 0.0;
            for ( i = 0; i < dimension; i++ ){
                term = term + ((data.Arr[j+SID1][i]-data1[j][i]) * (data.Arr[j+SID1][i]-data1[j][i]));
            }
            it_diff = it_diff + java.lang.Math.sqrt(term);
        }

         */

    }

    private int FindNearest(double[][] data, int data_num, int dimension, double[] sample){
        double dist_sq;
        double dist_sq_min=9999999999.9;
        int i,j;
        int nearest=0;

        for ( j = 0; j < data_num; j++ ){
            dist_sq = 0.0;
            for ( i = 0; i < dimension; i++ )
                dist_sq = dist_sq + (sample[i]-data[j][i]) * (sample[i]-data[j][i]);

            if ( j == 0 || dist_sq < dist_sq_min ){
                dist_sq_min = dist_sq;
                nearest = j;
            }
        }
        return nearest;
    }

    public void CVT(ArrayDouble2D data){
        int ItNum=10;
        ArrayDouble2D samples=new ArrayDouble2D(data.GetSize()*100, data.GetDimension());
        RandomNum ran=new RandomNum(data.GetDimension());

        for(int i=0;i<ItNum;i++){
            ran.NextPoints(samples, ran.PSEUDO);
            this.CVT_Iterate(data, samples);
        }
    }

}
