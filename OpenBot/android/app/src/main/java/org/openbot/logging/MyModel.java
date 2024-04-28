package org.openbot.logging;


import android.graphics.Bitmap;
import android.util.Log;

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
                model.get("W1")[i][j] = random.nextDouble() / Math.sqrt(D);
            }
        }

        model.put("W2", new double[H][3]); // Adjust dimensions to 1 x H
        for (int i = 0; i < H; i++) {
            for(int j =0; j < 3; j++) {
                model.get("W2")[i][j] = random.nextDouble() / Math.sqrt(H);
            }
        }
    }

    private double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    private double[] softmax(double[] scores) {
        double[] probabilities = new double[scores.length];
        double sum = 0.0;

        // Compute exponentials and sum
        for (double score : scores) {
            sum += Math.exp(score);
        }

        // Compute probabilities
        for (int i = 0; i < scores.length; i++) {
            probabilities[i] = Math.exp(scores[i]) / sum;
        }

        return probabilities;
    }

    public Object[] policyForward(double[][] x) {

        RealMatrix w1Matrix = MatrixUtils.createRealMatrix(model.get("W1"));
        RealMatrix xMatrix = MatrixUtils.createRealMatrix(x);
        RealMatrix xTranspose = xMatrix.transpose();
        RealMatrix hMatrix = xTranspose.multiply(w1Matrix);

        double[][] h = hMatrix.getData(); // Calculate the dot product
        Log.d("SIZE OF: ", "POLICY FORWARD h rows: " + h.length);
        Log.d("SIZE OF: ", "POLICY FORWARD h columns: " + h[0].length);



        relu(h); // Apply ReLU activation function

        hMatrix = MatrixUtils.createRealMatrix(h);
        RealMatrix w2Matrix = MatrixUtils.createRealMatrix(model.get("W2"));
        RealMatrix logProbMatrix = hMatrix.multiply(w2Matrix);
        double[][] logProb = logProbMatrix.getData(); // Calculate log probability
        Log.d("logProb: ", "logProb: " + logProb[0][0]);
        double[] p = softmax(logProb[0]); // Calculate probability
        Log.d("logProb: ", "p: " + p);
        // Return probability and hidden state
        return new Object[]{p, h}; // Assuming p is scalar, h is a 1xH array
    }



    private double[][] multiply(double[][] a, double[][] b) {
        int m = a.length;
        int n = b[0].length;
        int p = b.length;
        double[][] result = new double[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                double sum = 0;
                for (int k = 0; k < p; k++) {
                    sum += a[i][k] * b[k][j];
                }
                result[i][j] = sum;
            }
        }
        return result;
    }

    private void relu(double[][] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                if (a[i][j] < 0) {
                    a[i][j] = 0; // Apply ReLU function
                }
            }
        }
    }

    public Map<String, double[][]> policyBackward(double[][] eph, double[][] epx, double[][] epdLogProb) {
        /*  eph : nbObs*hiddenState x 1 -> NH x 1  Number of Observation/Steps times number of Hidden state (200),
            epx : nbObs*128*30 x 1 -> NImage x 1 Number of Observation/Steps times imageInfo,
            epdLogProb : nbObs * proba x 1-> N x 1 Number of Observations times the proba (1),

            I modify eph to become: nbObs x 200 -> N x 200 (H)

            dW2Matrix = ephTranspose * epdLogProb: 200 x N  * N x 1 -> (H) 200 x 1
            dW2Array = NH * N x 1

            dhMatrix = dh = np.outer(epdlogp, model['W2']) and epdLogProb is N x 1, model['W2'] H we get N x H (need to check with episode 2...)


            I modify epx to become: nbObs x 128*30 -> N x 3840

            so when I get dW1 = np.dot(dh.T, epx) I want H x N * N x 3840 (D) to get back to H x D




        Log.d("SIZE OF", "eph POLICY BACKWARDS ROWS: " + eph.length);
        Log.d("SIZE OF", "eph POLICY BACKWARDS Columns: " + eph[0].length);
        Log.d("SIZE OF", "epx POLICY BACKWARDS ROWS: " + epx.length);
        Log.d("SIZE OF", "epx POLICY BACKWARDS Columns: " + epx[0].length);
        Log.d("SIZE OF", "epdLogProb POLICY BACKWARDS ROWS: " + epdLogProb.length);
        Log.d("SIZE OF", "epdLogProb POLICY BACKWARDS VALUE 0: " + epdLogProb[0]);
        */
        Log.d("SIZE OF", "eph POLICY BACKWARDS ROWS: " + eph.length);
        Log.d("SIZE OF", "eph POLICY BACKWARDS Columns: " + eph[0].length);
        RealMatrix ephMatrix = MatrixUtils.createRealMatrix(eph);
        RealMatrix ephTranspose = ephMatrix.transpose();
        RealMatrix epdLogProbMatrix = MatrixUtils.createRealMatrix(epdLogProb);



        // Calculate gradients for W2
        RealMatrix dW2Matrix = ephTranspose.multiply(epdLogProbMatrix);
        int numRows = dW2Matrix.getRowDimension();
        int numCols = dW2Matrix.getColumnDimension();
        Log.d("SIZE OF", "dW2Matrix POLICY BACKWARDS ROWS: " + numRows);
        Log.d("SIZE OF", "dW2Matrix POLICY BACKWARDS Columns: " + numCols);

        // Trying tu manually flatten/ravel() the matrix:
        double[][] dW2 = dW2Matrix.getData();

        // Calculate the total number of elements in the data array
        /*int totalElements = data.length * data[0].length;

        // Create a one-dimensional array to store the flattened data
        double[] dW2 = new double[totalElements];

        // Flatten the data array using a nested loop
        int index = 0;
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                dW2[index++] = data[i][j];
            }
        }*/
        //double[] dW2 = dW2Matrix.getColumn(0);
        Log.d("SIZE OF", "dW2 POLICY BACKWARDS ROWS: " + dW2.length);
        Log.d("SIZE OF", "dW2 POLICY BACKWARDS Value: " + dW2[0]);

        // Calculate gradients for dh
        RealMatrix dhMatrix = epdLogProbMatrix.multiply(MatrixUtils.createRealMatrix(model.get("W2")).transpose());
        double[][] dh = dhMatrix.getData();
        Log.d("SIZE OF: ", "CALCULATED dh rows: " + dh.length);
        Log.d("SIZE OF: ", "CALCULATED dh columns: " + dh[0].length);

        // Apply backpropagation for ReLU activation
        for (int i = 0; i < eph.length; i++) {
            for (int j = 0; j < eph[i].length; j++) {
                if (eph[i][j] <= 0) {
                    dh[i][j] = 0; // Apply ReLU function
                }
            }
        }

        RealMatrix dhOriginal= MatrixUtils.createRealMatrix(dh);
        RealMatrix dhTranspose = dhOriginal.transpose();
        RealMatrix epxMatrix = reshape(epx, 128*30);
        numRows = epxMatrix.getRowDimension();
        numCols = epxMatrix.getColumnDimension();
        Log.d("SIZE OF", "epxMatrix POLICY BACKWARDS ROWS: " + numRows);
        Log.d("SIZE OF", "epxMatrix POLICY BACKWARDS Value: " + numCols);
        RealMatrix epxTranspose = epxMatrix.transpose();

        // Calculate gradients for W1
        RealMatrix dW1Matrix = dhTranspose.multiply(epxMatrix.transpose());
        double[][] dW1 = dW1Matrix.transpose().getData();

        // Return gradients
        Map<String, double[][]> gradients = new HashMap<>();
        gradients.put("W1", dW1);
        gradients.put("W2", dW2);



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
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(model);
            System.out.println("Model saved successfully.");
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
