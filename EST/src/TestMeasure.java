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

public class TestMeasure {//only for 2D

    //pattern types
    public static final int BLOCK = 1;
    public static final int POINT = 2;
    public static final int STRIP = 3;

    //measure types
    public static final int FMeasure = 1;//number of test cases required to detect the first failure.
    public static final int EMeasure = 2;//expected number of failures detected by a sequence of tests
    public static final int PMeasure = 3;//probability that at least one program failure is detected with a specified sequence of tests.

    //-----------------------------------------------
    // all Pattern
    //-----------------------------------------------
    public int TestPattern(ArrayDouble2D data, double failureRate, int measureType, int patternType){//return -1 if no fault detected)

        if(patternType== TestMeasure.BLOCK)
            return this.BlockPattern(data, failureRate, measureType);
        else if(patternType== TestMeasure.POINT)
            return this.PointPattern(data, failureRate, measureType);
        else if(patternType== TestMeasure.STRIP)
            return this.StripPattern(data, failureRate, measureType);
        else{
            System.out.println("Error: TestMeasure.TestPattern() Pattern type has not been defined");
            return 0;
        }
    }

    //-----------------------------------------------
    // Block Pattern
    //-----------------------------------------------
    public int BlockPattern(ArrayDouble2D data, double failureRate, int measureType){//return -1 if no fault detected
        //failureRate should be lower than 0.25

        Random rand=new Random();
        double length = Math.sqrt(failureRate);

        double x1,x2,y1,y2;
        x1=rand.nextDouble();
        y1=rand.nextDouble();

        if(x1 + length <= 1)
            x2 = x1 + length;
        else{
            x2 = x1;
            x1 = x2 - length;
        }

        if(y1 + length <= 1)
            y2 = y1 + length;
        else{
            y2 = y1;
            y1 = y2 - length;
        }

        //find fault
        if(measureType==TestMeasure.FMeasure){
            for(int i=data.GetStartIndex();i<(data.GetStartIndex()+data.GetSize());i++){
                if(data.Arr[i][0]>=x1 && data.Arr[i][1]>=y1 && data.Arr[i][0]<=x2 && data.Arr[i][1]<=y2)
                    return i - data.GetStartIndex();
            }
            return -1;
        }
        else if(measureType==TestMeasure.PMeasure){
            for(int i=data.GetStartIndex();i<(data.GetStartIndex()+data.GetSize());i++){
                if(data.Arr[i][0]>=x1 && data.Arr[i][1]>=y1 && data.Arr[i][0]<=x2 && data.Arr[i][1]<=y2)
                    return 1;
            }
            return 0;
        }
        else if(measureType==TestMeasure.EMeasure){
            int num=0;
            for(int i=data.GetStartIndex();i<(data.GetStartIndex()+data.GetSize());i++){
                if(data.Arr[i][0]>=x1 && data.Arr[i][1]>=y1 && data.Arr[i][0]<=x2 && data.Arr[i][1]<=y2)
                    num++;
            }
            return num;
        }
        
        return -1;
    }

    //-----------------------------------------------
    // Point Pattern
    //-----------------------------------------------
    private double Distance(double[] x, double[] y){
        return (Math.sqrt(  ((x[0]-y[0])*(x[0]-y[0])) + ((x[1]-y[1])*(x[1]-y[1]))  ));

    }

    public int PointPattern(ArrayDouble2D data, double failureRate, int measureType){//return -1 if no fault detected
        return this.PointPattern(data, failureRate, measureType, 10);//pointNumber=10 Andrew paper
    }
    public int PointPattern(ArrayDouble2D data, double failureRate, int measureType, int pointNumber){//return -1 if no fault detected
        //pointNumber=10 Andrew paper

        Random rand=new Random();
        double radius = Math.sqrt( failureRate / Math.PI / pointNumber);

        double[][] points=new double[pointNumber][2];
        double[] newPoint=new double[2];
        int pointNumberFound=0;
        boolean found;

        // find pointNumber point
        while(pointNumberFound<pointNumber){
            newPoint[0]=rand.nextDouble();
            newPoint[1]=rand.nextDouble();

            //find new point
            found=false;
            if(newPoint[0]+radius<=1 && newPoint[0]+radius>=0 && newPoint[1]+radius<=1 && newPoint[1]+radius>=0){
                found=true;
                for(int i=0;i<pointNumberFound;i++){
                    if(this.Distance(newPoint, points[i])<=radius){
                        found=false;
                        break;
                    }//end if
                }//end for
            }//end if

            //add new point
            if(found==true){
                points[pointNumberFound][0]=newPoint[0];
                points[pointNumberFound][1]=newPoint[1];
                pointNumberFound++;
            }
        }//end while


        //find fault
        if(measureType==TestMeasure.FMeasure){
            for(int i=data.GetStartIndex();i<(data.GetStartIndex()+data.GetSize());i++){
                for(int j=0;j<pointNumber;j++){
                    if(this.Distance(data.Arr[i], points[j])<= radius)
                        return i - data.GetStartIndex();
                }

            }
            return -1;
        }
        else if(measureType==TestMeasure.PMeasure){
            for(int i=data.GetStartIndex();i<(data.GetStartIndex()+data.GetSize());i++){
                for(int j=0;j<pointNumber;j++){
                    if(this.Distance(data.Arr[i], points[j])<= radius)
                        return 1;
                }

            }
            return 0;
        }
        else if(measureType==TestMeasure.EMeasure){
            int num=0;
            for(int i=data.GetStartIndex();i<(data.GetStartIndex()+data.GetSize());i++){
                for(int j=0;j<pointNumber;j++){
                    if(this.Distance(data.Arr[i], points[j])<= radius)
                        num++;
                }

            }
            return num;
        }

        return -1;

        
    }

    //-----------------------------------------------
    // Strip Pattern
    //-----------------------------------------------
    public int StripPattern(ArrayDouble2D data, double failureRate, int measureType){//return -1 if no fault detected
        //Gen strip pattern
        this.GenStripPattern(failureRate);

        //find fault
        if(measureType==TestMeasure.FMeasure){
            for(int i=data.GetStartIndex();i<data.GetEndIndex();i++){
                if( this.IsInStripPattern(data.Arr[i]) )
                    return i - data.GetStartIndex();
            }
            return -1;
        }
        else if(measureType==TestMeasure.PMeasure){
            for(int i=data.GetStartIndex();i<data.GetEndIndex();i++){
                if( this.IsInStripPattern(data.Arr[i]) )
                    return 1;
            }
            return 0;
        }
        else if(measureType==TestMeasure.EMeasure){
            int num=0;
            for(int i=data.GetStartIndex();i<data.GetEndIndex();i++){
                if( this.IsInStripPattern(data.Arr[i]) )
                    num++;
            }
            return num;
        }

        return -1;

    }

    double Strip_slopeG;
    double[] Strip_interceptG = new double[2];
    boolean Strip_90DegreeRotate;//false: normal,H  true:reverse,V

    

    public boolean GenStripPattern(double failureRate, double[] p1, double[] p2){

        if((p1[0]==0 && p2[0]==1) || (p1[0]==1 && p2[0]==0) || (p1[1]==0 && p2[1]==1) || (p1[1]==1 && p2[1]==0)){
            return GenStripPattern_VV_HH(failureRate, p1, p2);
        }
        else{
            return GenStripPattern_VH(failureRate, p1, p2);
        }


    }

    public boolean GenStripPattern_VH(double failureRate, double[] p1, double[] p2){//one point is in vertical line and another on horizontal line
        double[] intercept = new double[2];
        double[] y1intercept = new double[2];
        double[] y0intercept = new double[2];
        double[] x1intercept = new double[2];
        double slope;

        double dist = Math.sqrt( ((p1[0]-p2[0])*(p1[0]-p2[0])) + ((p1[1]-p2[1])*(p1[1]-p2[1])) );
        slope = (p1[1]-p2[1]) / (p1[0]-p2[0]);
        double height = failureRate/dist;
        double padding = height* Math.sqrt(1+ (slope*slope));

        intercept[1] = p2[1]-slope*p2[0] + padding/2;
        intercept[0] = intercept[1] - padding;//lower line

        y1intercept[0] = (1 - intercept[0])/slope;
        y1intercept[1] = (1 - intercept[1])/slope;

        y0intercept[0] = (0 - intercept[0])/slope;
        y0intercept[1] = (0 - intercept[1])/slope;

        x1intercept[0] = slope+intercept[0];
        x1intercept[1] = slope+intercept[1];

        boolean result=false;
        if((intercept[0] <= 1) && (intercept[0] >= 0) && (intercept[1] <= 1) && (intercept[1] >= 0) && (y1intercept[0] <= 1) && (y1intercept[0] >= 0) && (y1intercept[1] <= 1) && (y1intercept[1] >= 0))
            result=true;
        else if((intercept[0] <= 1) && (intercept[0] >= 0) && (intercept[1] <= 1) && (intercept[1] >= 0) && (y0intercept[0] <= 1) && (y0intercept[0] >= 0) && (y0intercept[1] <= 1) && (y0intercept[1] >= 0))
            result=true;
        else if((x1intercept[0] <= 1) && (x1intercept[0] >= 0) && (x1intercept[1] <= 1) && (x1intercept[1] >= 0) && (y1intercept[0] <= 1) && (y1intercept[0] >= 0) && (y1intercept[1] <= 1) && (y1intercept[1] >= 0))
            result=true;
        else if((x1intercept[0] <= 1) && (x1intercept[0] >= 0) && (x1intercept[1] <= 1) && (x1intercept[1] >= 0) && (y0intercept[0] <= 1) && (y0intercept[0] >= 0) && (y0intercept[1] <= 1) && (y0intercept[1] >= 0))
            result=true;

        //save in global param
        this.Strip_slopeG=slope;
        this.Strip_interceptG[0]=intercept[0];
        this.Strip_interceptG[1]=intercept[1];
        this.Strip_90DegreeRotate=false;

        return result;

    }

    public boolean GenStripPattern_VV_HH(double failureRate, double[] p1, double[] p2){//both points are in vertical or horizontal line
        this.Strip_90DegreeRotate=false;
        //check if HH
        if((p1[1]==0 && p2[1]==1) || (p1[1]==1 && p2[1]==0)){
            this.Strip_90DegreeRotate=true;
            double x;
            x=p1[0];
            p1[0]=p1[1];
            p1[1]=x;

            x=p2[0];
            p2[0]=p2[1];
            p2[1]=x;
        }
        //p1[0] should be 0
        if(p1[0]==1){
            //replace points
            double x,y;
            x=p1[0];
            y=p1[1];
            p1[0]=p2[0];
            p1[1]=p2[1];
            p2[0]=x;
            p2[1]=y;
        }
        //gen strip
        double[] intercept = new double[2];
        double slope;
        
        p1[1]=p1[1]*(1-failureRate);
        p2[1]=p2[1]*(1-failureRate);

        slope = (p2[1]-p1[1]) / (p2[0]-p1[0]);

        intercept[0] = p1[1];
        intercept[1] = intercept[0] +failureRate;



        //save in global param
        this.Strip_slopeG=slope;
        this.Strip_interceptG[0]=intercept[0];
        this.Strip_interceptG[1]=intercept[1];

        return true;
    }

    public boolean IsInStripPattern(double[] data){

        if(Strip_90DegreeRotate==false){
            if( (data[1] >= Strip_slopeG*data[0]+Strip_interceptG[0]) && (data[1] <= Strip_slopeG*data[0]+Strip_interceptG[1]) ){
                return true;
            }
            else
                return false;
        }
        else {
            if( (data[0] >= Strip_slopeG*data[1]+Strip_interceptG[0]) && (data[0] <= Strip_slopeG*data[1]+Strip_interceptG[1]) ){
                return true;
            }
            else
                return false;
        }
    }

    int StripPatternNum=10000;
    int StripPatternCounter=StripPatternNum;
    ArrayDouble2D StripPatternXY=new ArrayDouble2D(StripPatternNum,2);
    ArrayDouble2D StripPatternAngle=new ArrayDouble2D(StripPatternNum,1);

    public void GenStripPattern(double failureRate){
        Random rand=new Random();

        double[] p1=new double[2];
        double[] p2=new double[2];

        while(true){
            //-----------------------------------------------
            // both points are in vertical or horizontal line
            //-----------------------------------------------
            /*
            if(rand.nextDouble()<0.5){
                p1[0]=0;
                p1[1]=rand.nextDouble();

                p2[0]=1;
                p2[1]=rand.nextDouble();
            }
            else{
                p1[1]=0;
                p1[0]=rand.nextDouble();

                p2[1]=1;
                p2[0]=rand.nextDouble();
            }
             *
             */
            

            //-------------------------------------------------------------
            //one point is in vertical line and another on horizontal line
            //-------------------------------------------------------------
            /*
            double xB = rand.nextDouble()*2-1;
            double yB = rand.nextDouble()*2-1;

            if (xB>=0){
                p1[0]=xB;
                p1[1]=0.0;
            }
            else{
                p1[0]=xB+1;
                p1[1]=1.0;
            }

            if (yB>=0){
                p2[1]=yB;
                p2[0]=0.0;
            }
            else{
                p2[1]=yB+1;
                p2[0]=1.0;
            }
             *
             */

            //-----------------------------------------------
            // random point in squre and random angle
            //-----------------------------------------------

            //LHS
            /*
            RandomNum rn2=new RandomNum(2);
            RandomNum rn1=new RandomNum(1);
            if(StripPatternCounter==StripPatternNum){
                StripPatternCounter=0;
                rn2.NextPoints(StripPatternXY, rn2.LHS);
                rn1.NextPoints(StripPatternAngle, rn1.LHS);
            }

            double x=StripPatternXY.Arr[StripPatternCounter][0];
            double y=StripPatternXY.Arr[StripPatternCounter][1];
            double angle=StripPatternAngle.Arr[StripPatternCounter][0]*Math.PI;
            StripPatternCounter++;

             *
             */
            //normal
            double x=rand.nextDouble();
            double y=rand.nextDouble();
            double angle=rand.nextDouble()*Math.PI;

            int counter=0;
            //
            if(counter == 0){
                p1[0]=0;
                p1[1]=y-x*Math.tan(angle);

                if(p1[1]>=0 && p1[1]<=1)
                    counter=1;
            }
            else if(counter == 1)
            {
                p2[0]=0;
                p2[1]=y-x*Math.tan(angle);

                if(p2[1]>=0 && p2[1]<=1)
                    counter=2;
            }
            //
            if(counter == 0){
                p1[0]=1;
                p1[1]=y+(1-x)*Math.tan(angle);

                if(p1[1]>=0 && p1[1]<=1)
                    counter=1;
            }
            else if(counter == 1)
            {
                p2[0]=1;
                p2[1]=y+(1-x)*Math.tan(angle);

                if(p2[1]>=0 && p2[1]<=1)
                    counter=2;
            }
            //
            if(counter == 0){
                p1[1]=0;
                p1[0]=x-y/Math.tan(angle);

                if(p1[0]>=0 && p1[0]<=1)
                    counter=1;
            }
            else if(counter == 1)
            {
                p2[1]=0;
                p2[0]=x-y/Math.tan(angle);

                if(p2[0]>=0 && p2[0]<=1)
                    counter=2;
            }
            //
            if(counter == 0){
                p1[1]=1;
                p1[0]=x+(1-y)/Math.tan(angle);

                if(p1[0]>=0 && p1[0]<=1)
                    counter=1;
            }
            else if(counter == 1)
            {
                p2[1]=1;
                p2[0]=x+(1-y)/Math.tan(angle);

                if(p2[0]>=0 && p2[0]<=1)
                    counter=2;
            }

            if(counter!=2)
                System.out.println("Strip gen error");

            


            //--------------------------------------------
            // gen strip
            //--------------------------------------------
            if(GenStripPattern(failureRate, p1, p2)==true)
                break;

        }//end of while

    }
    
    
    //////////////////////////////////////////////////////////////
    /*
    public void GenStripPattern1(double failureRate){

        Random rand=new Random();

        double[] p1=new double[2];
        double[] p2=new double[2];
        double[] intercept = new double[2];
        double[] y1intercept = new double[2];
        double[] y0intercept = new double[2];
        double[] x1intercept = new double[2];
        double slope;

        while(true){
            double xB = rand.nextDouble()*2-1;
            double yB = rand.nextDouble()*2-1;

            if (xB>=0){
                p1[0]=xB;
                p1[1]=0.0;
            }
            else{
                p1[0]=xB+1;
                p1[1]=1.0;
            }

            if (yB>=0){
                p2[1]=yB;
                p2[0]=0.0;
            }
            else{
                p2[1]=yB+1;
                p2[0]=1.0;
            }

            double dist = Math.sqrt( ((p1[0]-p2[0])*(p1[0]-p2[0])) + ((p1[1]-p2[1])*(p1[1]-p2[1])) );
            slope = (p1[1]-p2[1]) / (p1[0]-p2[0]);
            double height = failureRate/dist;
            double padding = height* Math.sqrt(1+ (slope*slope));

            intercept[1] = p2[1]-slope*p2[0] + padding/2;
            intercept[0] = intercept[1] - padding;//lower line

            y1intercept[0] = (1 - intercept[0])/slope;
            y1intercept[1] = (1 - intercept[1])/slope;

            y0intercept[0] = (0 - intercept[0])/slope;
            y0intercept[1] = (0 - intercept[1])/slope;

            x1intercept[0] = slope+intercept[0];
            x1intercept[1] = slope+intercept[1];

            if((intercept[0] <= 1) && (intercept[0] >= 0) && (intercept[1] <= 1) && (intercept[1] >= 0) && (y1intercept[0] <= 1) && (y1intercept[0] >= 0) && (y1intercept[1] <= 1) && (y1intercept[1] >= 0))
                break;
            else if((intercept[0] <= 1) && (intercept[0] >= 0) && (intercept[1] <= 1) && (intercept[1] >= 0) && (y0intercept[0] <= 1) && (y0intercept[0] >= 0) && (y0intercept[1] <= 1) && (y0intercept[1] >= 0))
                break;
            else if((x1intercept[0] <= 1) && (x1intercept[0] >= 0) && (x1intercept[1] <= 1) && (x1intercept[1] >= 0) && (y1intercept[0] <= 1) && (y1intercept[0] >= 0) && (y1intercept[1] <= 1) && (y1intercept[1] >= 0))
                break;
            else if((x1intercept[0] <= 1) && (x1intercept[0] >= 0) && (x1intercept[1] <= 1) && (x1intercept[1] >= 0) && (y0intercept[0] <= 1) && (y0intercept[0] >= 0) && (y0intercept[1] <= 1) && (y0intercept[1] >= 0))
                break;
        }

        //save in global param
        this.Strip_slopeG=slope;
        this.Strip_interceptG[0]=intercept[0];
        this.Strip_interceptG[1]=intercept[1];
        this.Strip_90DegreeRotate=false;
    }
    public void GenStripPattern2(double failureRate){

        Random rand=new Random();

        double[] p1=new double[2];
        double[] p2=new double[2];
        double[] intercept = new double[2];
        double slope;

        p1[0]=0;
        p1[1]=rand.nextDouble()*(1-failureRate);

        p2[0]=1;
        p2[1]=rand.nextDouble()*(1-failureRate);

        if(rand.nextDouble()<0.5)
            this.Strip_90DegreeRotate=true;
        else
            this.Strip_90DegreeRotate=false;

        slope = (p2[1]-p1[1]) / (p2[0]-p1[0]);

        intercept[0] = p1[1];
        intercept[1] = intercept[0] +failureRate;



        //save in global param
        this.Strip_slopeG=slope;
        this.Strip_interceptG[0]=intercept[0];
        this.Strip_interceptG[1]=intercept[1];
    }

    public void GenStripPattern3(double failureRate){//random point in the squre and random degree

        Random rand=new Random();

        double[] p1=new double[2];
        double[] p2=new double[2];
        double[] intercept = new double[2];
        double slope;

        p1[0]=0;
        p1[1]=rand.nextDouble()*(1-failureRate);

        p2[0]=1;
        p2[1]=rand.nextDouble()*(1-failureRate);

        if(rand.nextDouble()<0.5)
            this.Strip_90DegreeRotate=true;
        else
            this.Strip_90DegreeRotate=false;

        slope = (p2[1]-p1[1]) / (p2[0]-p1[0]);

        intercept[0] = p1[1];
        intercept[1] = intercept[0] +failureRate;



        //save in global param
        this.Strip_slopeG=slope;
        this.Strip_interceptG[0]=intercept[0];
        this.Strip_interceptG[1]=intercept[1];
    }

     * 
     */
    
}
