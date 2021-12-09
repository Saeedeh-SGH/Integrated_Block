


package SoftwareTesting;

import MyPackage.*; 

/**
 *
 * @author ali shahbazi
 */


public class RBCVT {//Random border CVT
    private final static double epsilon=0.0000000000000001;
    
    public StatOnline statusL=new StatOnline();
    public StatOnline statusC=new StatOnline();
    public StatOnline statusP=new StatOnline();
    
    private int getEachDimensionNum(ArrayDouble2D dataGenerator){
        
        final double NumEachDimensionCof=2;//over1: increase the num of cells in each dim --> reduce border area width and increase unm of border points.
        
        double Num=Math.pow(dataGenerator.GetSize(), 1.0/dataGenerator.GetDimension()); //number of points in each dimention
        int NumEachDimension=(int)Math.round(Num*NumEachDimensionCof);
        return NumEachDimension;
    }
   
    
    private ArrayDouble2D getBorderPoints(ArrayDouble2D dataGenerator){//uniform random
        

        int NumEachDimension=getEachDimensionNum(dataGenerator);

        int TotalNum=(int)Math.round(Math.pow(NumEachDimension+2, dataGenerator.GetDimension()));
        
        double extraBorderWidth=1.0/NumEachDimension;
        
        ArrayDouble2D rUniformPoint=new ArrayDouble2D(TotalNum, dataGenerator.GetDimension());

        ERandom random=new ERandom(dataGenerator.GetDimension(), ERandom.RANDOMIZED_UNIFORM);
        random.SetBound(0-extraBorderWidth, 1+extraBorderWidth);
        random.NextPoints(rUniformPoint);
        
        
        //add points
        ArrayDouble2D extraPoint=new ArrayDouble2D(getBorderPointsNum(rUniformPoint), dataGenerator.GetDimension());
        extraPoint.SetSize(0);
        
        for(int i=0;i<rUniformPoint.GetSize();i++){
            
            //add point
            if(isPointOutsideOfBorder(rUniformPoint.Arr[i])==true){
                for (int d = 0; d < extraPoint.GetDimension(); d++) {
                    extraPoint.Arr[extraPoint.GetSize()][d] = rUniformPoint.Arr[i][d];
                    
                }
                extraPoint.SetSize(extraPoint.GetSize() + 1);
                
            }
        }
        
        extraPoint.SetBound(0-extraBorderWidth, 1+extraBorderWidth);
        
        return extraPoint;
        
    }
    
    
    
    private int getBorderPointsNum(ArrayDouble2D potentialPoints){
        int extraPointsNum=0;
        for(int i=0;i<potentialPoints.GetSize();i++){
            if(isPointOutsideOfBorder(potentialPoints.Arr[i])==true)
                extraPointsNum++;
        }
        return extraPointsNum;
    }
    private boolean isPointOutsideOfBorder(double[] point) {
        int outBorder = 0;
        for (int d = 0; d < point.length; d++) {
            if (point[d] >= 1 || point[d] < 0) {
                outBorder++;
            }
        }
        if (outBorder == 1) {
            return true;
        }
        return false;
    }
    
    
    
    public void CVT_Iterate(ArrayDouble2D data, ERandom rand, int sample_size, ArrayDouble2D borderPoints){
   
        SpacialSearchNotOpt ss=new SpacialSearchNotOpt();

        //copy the data  ////////////////////////////////////////////////////////////////////////////////////////////
        ArrayDouble2D data1=new ArrayDouble2D(data.GetSize()+borderPoints.GetSize(),data.GetDimension());

        for (int i = 0; i < data.GetSize(); i++ ){
            for (int d = 0; d < data.GetDimension(); d++ ){
                data1.Arr[i][d] = data.Arr[i][d];
            }
        }
        for (int i = 0; i < borderPoints.GetSize(); i++ ){
            for (int d = 0; d < data.GetDimension(); d++ ){
                data1.Arr[i+data.GetSize()][d] = borderPoints.Arr[i][d];
            }
        }
        
        data1.dataLowerBound=borderPoints.dataLowerBound;
        data1.dataHigherBound=borderPoints.dataHigherBound;
        
        //count
        int[] count=new int[data.GetSize()];
        for (int i = 0; i < data.GetSize(); i++ ){
            count[i] = 1;
        }

        //////////
        //pre process
        ss.Preprocess(data1);

        double temp[]=new double[data.GetDimension()];
        
        for (int i=0;i<sample_size;i++){
            //  Find the index of the nearest cell generator to each sample point.
            temp=rand.NextPoint();
            int index = ss.Find1NN(temp);
            if(index < data.GetSize()) {
                for (int d = 0; d < data.GetDimension(); d++) {
                    data.Arr[index][d] = data.Arr[index][d] + temp[d];
                }

                count[index] = count[index] + 1;
            }
        }


        //  Estimate the centroids.
        for (int i = 0; i < data.GetSize(); i++ ){
            for (int d = 0; d < data.GetDimension(); d++ )
                data.Arr[i][d] = data.Arr[i][d] /count[i];
        }
        
        //  replace out of bound points
        for (int i = 0; i < data.GetSize(); i++ ){
            for (int d = 0; d < data.GetDimension(); d++ ){
                if(data.Arr[i][d]>=data.dataHigherBound[d])
                   data.Arr[i][d]=data.dataHigherBound[d]-epsilon;
                else if(data.Arr[i][d]<data.dataLowerBound[d])
                    data.Arr[i][d]=data.dataLowerBound[d];
            }
        }
        
        //print
        //System.out.println("L:"+ss.statusL.GetAverage()+"   C:"+ss.statusC.GetAverage()+"    P:"+ss.statusP.GetAverage());
//        this.statusL.Update(ss.statusL.GetAverage());
//        this.statusC.Update(ss.statusC.GetAverage());
//        this.statusP.Update(ss.statusP.GetAverage());
        
        
    }
    
    public void CVT(ArrayDouble2D data){
        int ItNum=10;
        int backgroundCof=100;
        
        ArrayDouble2D borderPoints=getBorderPoints(data);
        
        ERandom ran=new ERandom(data.GetDimension(),ERandom.PSEUDO);
        ran.SetBound(borderPoints.dataLowerBound[0], borderPoints.dataHigherBound[0]);
        ran.SetSeed(0);
     
        for(int i=0;i<ItNum;i++){
            //ran.SetSeed(0);
            this.CVT_Iterate(data, ran, data.GetSize()*backgroundCof, borderPoints);
        }
        ////////////////////////////////////////////////////////////////////clear
//        for (int i = 0; i < borderPoints.GetSize(); i++ ){
//            for (int d = 0; d < data.GetDimension(); d++ ){
//                data.Arr[i+data.GetSize()][d] = borderPoints.Arr[i][d];
//            }
//        }
//        data.SetSize(data.GetSize()+borderPoints.GetSize());
    }
    
    

}

