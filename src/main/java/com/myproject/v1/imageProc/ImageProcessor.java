package com.myproject.v1.imageProc;

import com.myproject.v1.viewmodel.ResultModel;
import org.opencv.imgproc.Imgproc;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;

public class ImageProcessor {

    public String classifyImage(String filePath) throws IOException {
        String scriptLocation = Paths.get(System.getProperty("user.dir")).toString()+ "\\python\\";

        String script = scriptLocation + "classify.py";
        String model = scriptLocation + "eMageSearch.model";
        String label = scriptLocation + "lb.pickle";

        Runtime rt = Runtime.getRuntime();
        Process proc = rt.exec("python37.exe " + script +
                " --model " + model
                + " --labelbin " + label + " --image " +  filePath );

        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(proc.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(proc.getErrorStream()));

// Read the output from the command
        System.out.println("Here is the standard output of the command:\n");
        String returnString = "";
        String output = null;
        while ((output = stdInput.readLine()) != null) {
            if(output != null)
                returnString = output;
            System.out.println(output);
        }

// Read any errors from the attempted command
        String error = null;
        System.out.println("Here is the standard error of the command (if any):\n");
        while ((error = stdError.readLine()) != null) {
            System.out.println(error);
        }

        return returnString;
    }

    public String getImageColor(String filePath) throws IOException {
        String scriptLocation = Paths.get(System.getProperty("user.dir")).toString()+ "\\python\\";

        String script = scriptLocation + "pixelValue.py";

        Runtime rt = Runtime.getRuntime();
        Process proc = rt.exec("python37.exe " + script +" --image " +  filePath );
        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(proc.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(proc.getErrorStream()));

// Read the output from the command
        System.out.println("Here is the standard output of the command:\n");
        String returnString = "";
        String output = null;
        while ((output = stdInput.readLine()) != null) {
            if(output != null)
                returnString = output;
            System.out.println(output);
        }

// Read any errors from the attempted command
        String error = null;
        System.out.println("Here is the standard error of the command (if any):\n");
        while ((error = stdError.readLine()) != null) {
            System.out.println(error);
        }

        String[] rgb = returnString.split(",");
        return getColorName(Integer.parseInt(rgb[0]),Integer.parseInt(rgb[1]),Integer.parseInt(rgb[2]));

    }

    private String getColorName(int red, int green, int blue){

        String color = String.format("Un_Identified_RGB(%d,%d,%d)", red, green,blue);
        if((red - 10) <= 0){
            if((green - 10) <= 0 && (blue - 10) <= 0)
                color = "black";
            else if((green - 10) <= 0){
                if(blue >= 118 && blue <= 138)
                    color = "navy";
                else
                    color =  "blue";
            }
        }
        else if(red <= 200 && (green - 10) > 0 && (blue - 10) > 0){
            if((green - 10) >= 118 && (green - 10) <= 138)
                color = "gray";
            else
                color =  "silver";
        }
        else {
            if((green - 10) <= 0 && (blue - 10) <= 0)
                color = "red";
            else if((blue - 10) <= 0)
                color = "yellow";
            else color = "white";
        }
        return color;
    }


    public void readTextFromImg(){
//        Imgproc.cvtColor(original, grey, Imgproc.COLOR_RGB2GRAY, 0);
//
//        Imgproc.GaussianBlur(grey, blur, new Size(0, 0), 3);
//
//        Core.addWeighted(blur, 1.5, unsharp, -0.5, 0, unsharp);
//
//        Imgproc.threshold(unsharp,binary,127,255,Imgproc.THRESH_BINARY);
//
//        MatOfInt params = new MatOfInt(Imgcodecs.CV_IMWRITE_PNG_COMPRESSION);
//        File ocrImage = new File("ocrImage.png");
//        Imgcodecs.imwrite(ocrImage,binary,params);
//
//        /*initialize OCR ...*/
//        lept.PIX image = pixRead(ocrImage);
//        api.SetImage(image);
//        String ocrOutput = api.GetUTF8Text();
    }

    private void dd4l(){


        //            String simpleMlp = new ClassPathResource(
//                    "eMageSearch.h5").getFile().getPath();
//            MultiLayerNetwork model = KerasModelImport.
//                    importKerasSequentialModelAndWeights(simpleMlp);

        //Mat image = Imgcodecs.imread("src/main/resources/public/images/" + fileUploadResult.getData());

        //                NativeImageLoader loader = new NativeImageLoader(96, 96);
//                INDArray img1 = loader.asMatrix(file);
//                img1.div(255);
//                Nd4j.concat(0, img1, Nd4j.create(96, 96));
//
//                int inputs = 10;
//                INDArray features = Nd4j.zeros(inputs);
//                for (int i=0; i<inputs; i++)
//                    features.putScalar(new int[] {i}, Math.random() < 0.5 ? 0 : 1);
//
//                Object prediction = model.output(features).getDouble(0);

    }
}
