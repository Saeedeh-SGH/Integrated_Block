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



public class CVTFast {
    
    public StatOnline statusL=new StatOnline();
    public StatOnline statusC=new StatOnline();
    public StatOnline statusP=new StatOnline();
    
    public void CVT_Iterate(ArrayDouble2D data, ArrayDouble2D samples){
   
        
        int i;//move on one data dimensions
        int j;//move on different data
        int j2;
        SpacialSearch ss=new SpacialSearch();
        
        //double energy=0;
        //double it_diff=0;

        int data_num=data.GetSize();
        int dimension=data.GetDimension();
        int SID1=data.GetStartIndex();
        int samples_num=samples.GetSize();
        int SIS1=samples.GetStartIndex();

        //copy the data  ////////////////////////////////////////////////////////////////////////////////////////////
        ArrayDouble2D data1=new ArrayDouble2D(data_num,dimension);

        for ( j = 0; j < data_num; j++ ){
            for ( i = 0; i < dimension; i++ ){
                data1.Arr[j][i] = data.Arr[SID1+j][i];
            }
        }

        //count
        int[] count=new int[data_num];
        for ( j = 0; j < data_num; j++ ){
            count[j] = 1;
        }

        //////////
        //pre process
        ss.Preprocess(data1);

        for (j=SIS1;j<SIS1+samples_num;j++){
            //  Find the index of the nearest cell generator to each sample point.
            j2 = ss.Find1NN(samples.Arr[j]);
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
        
        //print
        //System.out.println("L:"+ss.statusL.GetAverage()+"   C:"+ss.statusC.GetAverage()+"    P:"+ss.statusP.GetAverage());
        this.statusL.Update(ss.statusL.GetAverage());
        this.statusC.Update(ss.statusC.GetAverage());
        this.statusP.Update(ss.statusP.GetAverage());
        
        
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
    
    public void CVT_Iterate(ArrayDouble2D data, ERandom rand, int sample_size){
   
        
        int i;//move on one data dimensions
        int j;//move on different data
        int j2;
        SpacialSearch ss=new SpacialSearch();
        
        //double energy=0;
        //double it_diff=0;

        int data_num=data.GetSize();
        int dimension=data.GetDimension();
        int SID1=data.GetStartIndex();

        //copy the data  ////////////////////////////////////////////////////////////////////////////////////////////
        ArrayDouble2D data1=new ArrayDouble2D(data_num,dimension);

        for ( j = 0; j < data_num; j++ ){
            for ( i = 0; i < dimension; i++ ){
                data1.Arr[j][i] = data.Arr[SID1+j][i];
            }
        }

        //count
        int[] count=new int[data_num];
        for ( j = 0; j < data_num; j++ ){
            count[j] = 1;
        }

        //////////
        //pre process
        ss.Preprocess(data1);

        double temp[]=new double[dimension];
        
        for (j=0;j<sample_size;j++){
            //  Find the index of the nearest cell generator to each sample point.
            temp=rand.NextPoint();
            j2 = ss.Find1NN(temp);
            for ( i = 0; i < dimension; i++ ){
                data.Arr[SID1+j2][i] = data.Arr[SID1+j2][i] + temp[i];
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
        
        //print
        //System.out.println("L:"+ss.statusL.GetAverage()+"   C:"+ss.statusC.GetAverage()+"    P:"+ss.statusP.GetAverage());
        this.statusL.Update(ss.statusL.GetAverage());
        this.statusC.Update(ss.statusC.GetAverage());
        this.statusP.Update(ss.statusP.GetAverage());
        
        
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
    
    public void CVT(ArrayDouble2D data){
        int ItNum=10;

        ERandom ran=new ERandom(data.GetDimension(),ERandom.SOBOL);

        //ran.SetSeed(0);
        

        for(int i=0;i<ItNum;i++){
            ran.SetSeed(0);
            this.CVT_Iterate(data, ran, data.GetSize()*100);
        }
    }
    
    

}
