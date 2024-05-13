package org.openbot.logging;


import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;


public class MyModel implements Serializable {

    private Map<String, double[][]> model;

    public MyModel() {
        model = new HashMap<>();
    }

    public void initializeWeights(int H, int D) {
        model.put("W1", new double[D][H]);
        Random random = new Random();
        for (int i = 0; i < D; i++) {
            for (int j = 0; j < H; j++) {
                double randomValue = 0 + random.nextGaussian()*0.3;
                model.get("W1")[i][j] = randomValue / Math.sqrt(D);
            }
        }


        model.put("W2", new double[H][3]); // Adjust dimensions to 1 x H
        for (int i = 0; i < H; i++) {
            for(int j =0; j < 3; j++) {
                double randomValue = 0 + random.nextGaussian()*0.3;
                model.get("W2")[i][j] = randomValue / Math.sqrt(H);
            }
        }
    }

    private double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    public double[] softmax(double[] logits) {
        double maxLogit = Double.NEGATIVE_INFINITY;
        for (double logit : logits) {
            maxLogit = Math.max(maxLogit, logit);
        }

        double sumExp = 0.0;
        for (double logit : logits) {
            sumExp += Math.exp(logit - maxLogit);
        }

        double[] softmaxProbs = new double[logits.length];
        for (int i = 0; i < logits.length; i++) {
            softmaxProbs[i] = Math.exp(logits[i] - maxLogit) / sumExp;
        }

        return softmaxProbs;
    }

    public Object[] policyForward(RealMatrix x) {


        RealMatrix w1Matrix = MatrixUtils.createRealMatrix(model.get("W1"));
        RealMatrix xTranspose = x.transpose();
        RealMatrix hMatrix = xTranspose.multiply(w1Matrix);

        double[][] h = hMatrix.getData(); // Calculate the dot product
        Log.d("SIZE OF: ", "POLICY FORWARD h rows: " + h.length);
        Log.d("SIZE OF: ", "POLICY FORWARD h columns: " + h[0].length);



        h = relu(h); // Apply ReLU activation function

        hMatrix = MatrixUtils.createRealMatrix(h);
        RealMatrix w2Matrix = MatrixUtils.createRealMatrix(model.get("W2"));
        RealMatrix logProbMatrix = hMatrix.multiply(w2Matrix);
        double[][] logProb = logProbMatrix.getData(); // Calculate log probability

        Log.d("logProb: ", "logProb: " + logProb[0][0]);
        Log.d("logProb: ", "logProb1: " + logProb[0][1]);
        Log.d("logProb: ", "logProb2: " + logProb[0][2]);
        double[] p = softmax(logProb[0]); // Calculate probability
        Log.d("logProb: ", "p: " + p[0]);
        Log.d("logProb: ", "p: " + p[1]);
        Log.d("logProb: ", "p: " + p[2]);
        // Return probability and hidden state
        return new Object[]{p, h}; // Assuming p is scalar, h is a 1xH array
    }


    private double[][] relu(double[][] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                if (a[i][j] < 0) {
                    a[i][j] = 0; // Apply ReLU function
                }
            }
        }
        return a;
    }

    private double maxValue(RealMatrix matrix){
        double maxValue = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                double value = matrix.getEntry(i, j);
                if (value > maxValue) {
                    maxValue = value;
                }
            }
        }
        return maxValue;
    }
    private double minValue(RealMatrix matrix){
        double minValue = Double.POSITIVE_INFINITY;
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                double value = matrix.getEntry(i, j);
                if (value < minValue) {
                    minValue = value;
                }
            }
        }
        return minValue;
    }
    public Map<String, double[][]> policyBackward(RealMatrix eph, RealMatrix epx, RealMatrix epdLogProb) {

        RealMatrix ephTranspose = eph.transpose();
        Log.d("logProb", "eph: " + maxValue(eph));
        Log.d("logProb", "eph: " + minValue(eph));
        Log.d("logProb", "epx: " + maxValue(epx));
        Log.d("logProb", "logProb: " + maxValue(epdLogProb));


        // Calculate gradients for W2
        RealMatrix dW2Matrix = ephTranspose.multiply(epdLogProb);
        int numRows = dW2Matrix.getRowDimension();
        int numCols = dW2Matrix.getColumnDimension();
        Log.d("SIZE OF", "dW2Matrix POLICY BACKWARDS ROWS: " + numRows);
        Log.d("SIZE OF", "dW2Matrix POLICY BACKWARDS Columns: " + numCols);

        // Trying tu manually flatten/ravel() the matrix:
        double[][] dW2 = dW2Matrix.getData();


        Log.d("SIZE OF", "dW2 POLICY BACKWARDS ROWS: " + dW2.length);
        Log.d("SIZE OF", "dW2 POLICY BACKWARDS Value: " + dW2[5][1]);

        // Calculate gradients for dh
        RealMatrix dhMatrix = epdLogProb.multiply(MatrixUtils.createRealMatrix(model.get("W2")).transpose());
        double[][] dh = dhMatrix.getData();
        Log.d("SIZE OF: ", "CALCULATED dh rows: " + dh.length);
        Log.d("SIZE OF: ", "CALCULATED dh columns: " + dh[0].length);

        // Apply backpropagation for ReLU activation
        for (int i = 0; i < eph.getRowDimension(); i++) {
            for (int j = 0; j < eph.getColumnDimension(); j++) {
                if (eph.getEntry(i,j) <= 0) {
                    dh[i][j] = 0; // Apply ReLU function
                }
            }
        }

        RealMatrix dhOriginal= MatrixUtils.createRealMatrix(dh);
        RealMatrix dhTranspose = dhOriginal.transpose();
        numRows = epx.getRowDimension();
        numCols = epx.getColumnDimension();
        Log.d("SIZE OF", "epxMatrix POLICY BACKWARDS ROWS: " + numRows);
        Log.d("SIZE OF", "epxMatrix POLICY BACKWARDS Value: " + numCols);
        RealMatrix epxTranspose = epx.transpose();

        // Calculate gradients for W1
        RealMatrix dW1Matrix = dhTranspose.multiply(epx);
        double[][] dW1 = dW1Matrix.transpose().getData();

        // Return gradients
        Map<String, double[][]> gradients = new HashMap<>();
        gradients.put("W1", dW1);
        gradients.put("W2", dW2);
        Log.d("logProb", "dW1 " + dW1Matrix.getEntry(20, 20));
        Log.d("logProb", "dW2 " + dW2Matrix.getEntry(20, 1));



        return gradients;
    }

    private RealMatrix reshape(double[][] matrix, int numRows) {
        int numColumns = matrix.length / numRows;
        double[][] newMatrixArray = new double[numRows][numColumns];

        int index = 0;
        for (int col = 0; col < numColumns; col++) {
            for (int row = 0; row < numRows; row++) {
                newMatrixArray[row][col] = matrix[index++][0];
            }
        }

        return MatrixUtils.createRealMatrix(newMatrixArray);
    }
    public static void main(String[] args) {
        MyModel model = new MyModel();
        int H = 200; // Number of hidden units
        int D = 128*30; // Input dimension
        model.initializeWeights(H, D);
        System.out.println(model.getModel());
    }
    public void setModel(Map<String, double[][]> updatedModel) {
        this.model = updatedModel;
    }

    public Map<String, double[][]> updateModel(Map<String, double[][]> gradBuffer, Map<String, double[][]> rmsPropCache, double decayRate, double learningRate) {
        Map<String, double[][]> updatedModel = new HashMap<>();
        for (String key : model.keySet()) {
            double[][] g = gradBuffer.get(key); // Gradient

            // Update rmsProp_cache
            double[][] rmsPropCacheArray = rmsPropCache.get(key);
            for (int i = 0; i < g.length; i++) {
                for (int j = 0; j < g[0].length; j++) {
                    rmsPropCacheArray[i][j] = decayRate * rmsPropCacheArray[i][j] + (1 - decayRate) * Math.pow(g[i][j], 2);
                }
            }

            // Update model
            double[][] modelArray = model.get(key);
            for (int i = 0; i < g.length; i++) {
                for (int j = 0; j < g[0].length; j++) {
                    modelArray[i][j] += learningRate * g[i][j] / (Math.sqrt(rmsPropCacheArray[i][j]) + 1e-5);
                }
            }

            // Reset batch gradient buffer
            gradBuffer.put(key, new double[modelArray.length][modelArray[0].length]);

            // Store updated model
            updatedModel.put(key, modelArray);
        }
        return updatedModel;
    }

    public Map<String, double[][]> getModel() {
        return model;
    }

    public void saveModel(String filePath) {
        try {
            // Create parent directories if they don't exist
            File file = new File(filePath);
            File parentDir = file.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }

            // Write the object to file
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(model);
            out.close();

            Log.d("Model", "Model Saved successfully");
        } catch (IOException e) {
            System.err.println("Error saving model: " + e.getMessage());
        }
    }

    // Function to load the model from a file
    public void loadModel(String filePath) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
            model = (Map<String, double[][]>) in.readObject();
            System.out.println("Model loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading model: " + e.getMessage());
        }
    }
}
