package MainClass; 

import MyPackage.*;
import SoftwareTesting.*;


public class MainClass {

    public static void main(String[] args) {  
        
        int PointNum=69;//69, 693, 6931, 69314
        int ItNum=10000;
        int PointSetNum=100;
        double failureRate=0.01;
        int SDSectionSize=PointSetNum;
        int MeasureType=TestMeasure.PMeasure;
        boolean CVTEnable=false;

        
        int i,j;
        String ResFileB="resB_0.01.txt";
        String ResFileP="resP_0.01.txt";
        String ResFileS="resS_0.01.txt";
        String DataFile="D:\\Ali\\AnyCode\\Java\\Paper_TSE_RBCVT\\TestSet\\TestSets2\\";

        TestMeasure fm=new TestMeasure();
//        DARTFast dart=new DARTFast();
//        RRTFast rrt=new RRTFast();
//        EARFast ear=new EARFast();
//        CVTFast cvt=new CVTFast();
        RBCVTFast cvt=new RBCVTFast();
        
        TimemSec time=new TimemSec();

        ArrayDouble2D points=new ArrayDouble2D(PointNum,2);
        ArrayDouble1D BP_result=new ArrayDouble1D(PointSetNum*ItNum);
        ArrayDouble1D PP_result=new ArrayDouble1D(PointSetNum*ItNum);
        ArrayDouble1D SP_result=new ArrayDouble1D(PointSetNum*ItNum);

        ArrayFile file=new ArrayFile();
        //////////////////////////////////////////////////////////////////////
        file.AddLine("\r\nAvg\tSD", ResFileB);
        file.AddLine("\r\nAvg\tSD", ResFileP);
        file.AddLine("\r\nAvg\tSD", ResFileS);
        

        //
        for(i=0;i<PointSetNum;i++){
            time.Start();
            file.FileToArrayDouble2D(points, DataFile+"Pseudo\\"+i+".txt");
            if(CVTEnable==true){
                cvt.CVT(points);
            }
            for(j=0;j<ItNum;j++){
                BP_result.Arr[j+i*ItNum]=fm.TestPattern(points, failureRate, MeasureType, TestMeasure.BLOCK);
                PP_result.Arr[j+i*ItNum]=fm.TestPattern(points, failureRate, MeasureType, TestMeasure.POINT);
                SP_result.Arr[j+i*ItNum]=fm.TestPattern(points, failureRate, MeasureType, TestMeasure.STRIP);
                
            }
            System.out.print(time.GetTime()/1000+" Sec   ");
        }
        file.AddLine(BP_result.GetAverage()+"\t"+BP_result.GetSDSection(SDSectionSize), ResFileB);
        file.AddLine(PP_result.GetAverage()+"\t"+PP_result.GetSDSection(SDSectionSize), ResFileP);
        file.AddLine(SP_result.GetAverage()+"\t"+SP_result.GetSDSection(SDSectionSize), ResFileS);

        //
        ERandom sobol =new ERandom(2, ERandom.SOBOL);
        for(i=0;i<PointSetNum;i++){
            time.Start();
            //file.FileToArrayDouble2D(points, DataFile+"Sobol\\"+0+".txt");
            sobol.NextPoints(points);
            if(CVTEnable==true){
                cvt.CVT(points);
            }
            for(j=0;j<ItNum;j++){
                BP_result.Arr[j+i*ItNum]=fm.TestPattern(points, failureRate, MeasureType, TestMeasure.BLOCK);
                PP_result.Arr[j+i*ItNum]=fm.TestPattern(points, failureRate, MeasureType, TestMeasure.POINT);
                SP_result.Arr[j+i*ItNum]=fm.TestPattern(points, failureRate, MeasureType, TestMeasure.STRIP);

            }
            System.out.print(time.GetTime()/1000+" Sec   ");
        }
        file.AddLine(BP_result.GetAverage()+"\t"+BP_result.GetSDSection(SDSectionSize), ResFileB);
        file.AddLine(PP_result.GetAverage()+"\t"+PP_result.GetSDSection(SDSectionSize), ResFileP);
        file.AddLine(SP_result.GetAverage()+"\t"+SP_result.GetSDSection(SDSectionSize), ResFileS);

        //
        ERandom NX =new ERandom(2, ERandom.NIEDERREITER);
        for(i=0;i<PointSetNum;i++){
            time.Start();
            //file.FileToArrayDouble2D(points, DataFile+"NX\\"+0+".txt");
            NX.NextPoints(points);
            if(CVTEnable==true){
                cvt.CVT(points);
            }
            for(j=0;j<ItNum;j++){
                BP_result.Arr[j+i*ItNum]=fm.TestPattern(points, failureRate, MeasureType, TestMeasure.BLOCK);
                PP_result.Arr[j+i*ItNum]=fm.TestPattern(points, failureRate, MeasureType, TestMeasure.POINT);
                SP_result.Arr[j+i*ItNum]=fm.TestPattern(points, failureRate, MeasureType, TestMeasure.STRIP);

            }
            System.out.print(time.GetTime()/1000+" Sec   ");
        }
        file.AddLine(BP_result.GetAverage()+"\t"+BP_result.GetSDSection(SDSectionSize), ResFileB);
        file.AddLine(PP_result.GetAverage()+"\t"+PP_result.GetSDSection(SDSectionSize), ResFileP);
        file.AddLine(SP_result.GetAverage()+"\t"+SP_result.GetSDSection(SDSectionSize), ResFileS);

        //
        ERandom halton =new ERandom(2, ERandom.HALTON);
        for(i=0;i<PointSetNum;i++){
            time.Start();
            //file.FileToArrayDouble2D(points, DataFile+"Halton\\"+0+".txt");
            halton.NextPoints(points);
            if(CVTEnable==true){
                cvt.CVT(points);
            }
            for(j=0;j<ItNum;j++){
                BP_result.Arr[j+i*ItNum]=fm.TestPattern(points, failureRate, MeasureType, TestMeasure.BLOCK);
                PP_result.Arr[j+i*ItNum]=fm.TestPattern(points, failureRate, MeasureType, TestMeasure.POINT);
                SP_result.Arr[j+i*ItNum]=fm.TestPattern(points, failureRate, MeasureType, TestMeasure.STRIP);

            }
            System.out.print(time.GetTime()/1000+" Sec   ");
        }
        file.AddLine(BP_result.GetAverage()+"\t"+BP_result.GetSDSection(SDSectionSize), ResFileB);
        file.AddLine(PP_result.GetAverage()+"\t"+PP_result.GetSDSection(SDSectionSize), ResFileP);
        file.AddLine(SP_result.GetAverage()+"\t"+SP_result.GetSDSection(SDSectionSize), ResFileS);

        //
        for(i=0;i<PointSetNum;i++){
            time.Start();
            file.FileToArrayDouble2D(points, DataFile+"DART\\"+i+".txt");
            if(CVTEnable==true){
                cvt.CVT(points);
            }
            for(j=0;j<ItNum;j++){
                BP_result.Arr[j+i*ItNum]=fm.TestPattern(points, failureRate, MeasureType, TestMeasure.BLOCK);
                PP_result.Arr[j+i*ItNum]=fm.TestPattern(points, failureRate, MeasureType, TestMeasure.POINT);
                SP_result.Arr[j+i*ItNum]=fm.TestPattern(points, failureRate, MeasureType, TestMeasure.STRIP);

            }
            System.out.print(time.GetTime()/1000+" Sec   ");
        }
        file.AddLine(BP_result.GetAverage()+"\t"+BP_result.GetSDSection(SDSectionSize), ResFileB);
        file.AddLine(PP_result.GetAverage()+"\t"+PP_result.GetSDSection(SDSectionSize), ResFileP);
        file.AddLine(SP_result.GetAverage()+"\t"+SP_result.GetSDSection(SDSectionSize), ResFileS);

        //
        for(i=0;i<PointSetNum;i++){
            time.Start();
            file.FileToArrayDouble2D(points, DataFile+"RRT\\"+i+".txt");
            if(CVTEnable==true){
                cvt.CVT(points);
            }
            for(j=0;j<ItNum;j++){
                BP_result.Arr[j+i*ItNum]=fm.TestPattern(points, failureRate, MeasureType, TestMeasure.BLOCK);
                PP_result.Arr[j+i*ItNum]=fm.TestPattern(points, failureRate, MeasureType, TestMeasure.POINT);
                SP_result.Arr[j+i*ItNum]=fm.TestPattern(points, failureRate, MeasureType, TestMeasure.STRIP);

            }
            System.out.print(time.GetTime()/1000+" Sec   ");
        }
        file.AddLine(BP_result.GetAverage()+"\t"+BP_result.GetSDSection(SDSectionSize), ResFileB);
        file.AddLine(PP_result.GetAverage()+"\t"+PP_result.GetSDSection(SDSectionSize), ResFileP);
        file.AddLine(SP_result.GetAverage()+"\t"+SP_result.GetSDSection(SDSectionSize), ResFileS);

        //
        for(i=0;i<PointSetNum;i++){
            time.Start();
            file.FileToArrayDouble2D(points, DataFile+"EAR\\"+i+".txt");
            if(CVTEnable==true){
                cvt.CVT(points);
            }
            for(j=0;j<ItNum;j++){
                BP_result.Arr[j+i*ItNum]=fm.TestPattern(points, failureRate, MeasureType, TestMeasure.BLOCK);
                PP_result.Arr[j+i*ItNum]=fm.TestPattern(points, failureRate, MeasureType, TestMeasure.POINT);
                SP_result.Arr[j+i*ItNum]=fm.TestPattern(points, failureRate, MeasureType, TestMeasure.STRIP);

            }
            System.out.print(time.GetTime()/1000+" Sec   ");
        }
        file.AddLine(BP_result.GetAverage()+"\t"+BP_result.GetSDSection(SDSectionSize), ResFileB);
        file.AddLine(PP_result.GetAverage()+"\t"+PP_result.GetSDSection(SDSectionSize), ResFileP);
        file.AddLine(SP_result.GetAverage()+"\t"+SP_result.GetSDSection(SDSectionSize), ResFileS);

    }
    
    
}
