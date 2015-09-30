package postprocess.spark;

import machinelearning.ScatterPlot;
import org.apache.spark.Accumulator;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.DataFrame;
import preprocess.spark.ConfigRead;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class PostProcessParquet implements Serializable {
    private static String outputCSVPath;
    private static ConfigRead configRead;
    private static boolean findTopMiddle = false;
    public  static String[] topics = {"Politics"};
    public  static String[] features = {"From"};//, "Hashtag", "Term", "Mention"};
    public  static String[] subAlgs = {"CE_Suvash"};//"JP", "CE", "CP", "MI"};
    public static String ceName = "CE_Suvash";
    public static String clusterResultsPath = "/Volumes/SocSensor/Zahra/Sept16/ClusterResults/";
    public static int topFeatureNum = 1000;

    public static void loadConfig() throws IOException {
        configRead = new ConfigRead();
    }

    public static void main(String args[]) throws IOException, InterruptedException {
        writeHeader();
        loadConfig();
        int itNum = configRead.getSensorEvalItNum();
        int hashtagNum = configRead.getSensorEvalHashtagNum();
        outputCSVPath = configRead.getOutputCSVPath();
        String scriptPath = configRead.getScriptPath();
        boolean local = configRead.isLocal();
        boolean calcNoZero = false;
        boolean convertParquet = true;
        boolean fixNumbers = false;
        boolean runScript = false;
        boolean makeScatterFiles = false;
        boolean cleanTerms = false;
        boolean buildLists = false;

        //if(local)
        //    clusterResultsPath = outputCSVPath;

        if(buildLists)
            getLists();
        if(makeScatterFiles)
            makeScatterFiles();
        if(cleanTerms)
            cleanTerms();

        if(convertParquet) {
            //writeHeader();
            SparkConf sparkConfig;
            if (local) {
                outputCSVPath = "ClusterResults/TestTrain/";
                //outputCSVPath = "ClusterResults/DisasterTerm/disaster_term/";
                //outputCSVPath = "TestSet/Data/";
                sparkConfig = new SparkConf().setAppName("PostProcessParquet").setMaster("local[2]");
            } else
                sparkConfig = new SparkConf().setAppName("PostProcessParquet");
            JavaSparkContext sparkContext = new JavaSparkContext(sparkConfig);
            SQLContext sqlContext = new SQLContext(sparkContext);

            // Read all parquet part by part results files and combine them into 1 csv file for each iteration per group
            File folder = new File(outputCSVPath);
            ArrayList<String> fileNames = listFilesForFolder(folder);
            DataFrame res;
            int ind = -1;
            int[] lineNumbers = new int[fileNames.size()];
            for (String filename : fileNames) {
                ind++;
                //if(ind > 53) continue;
                if (filename.contains(".csv"))
                    continue;
                System.out.println(outputCSVPath +"/"+ filename);
                res = sqlContext.read().parquet(outputCSVPath + "/" + filename);
                lineNumbers[ind] = readResults2(res, sparkContext, ind, filename);
                //res = sqlContext.read().parquet(outputCSVPath);
                //lineNumbers[ind] = readResults1(res, sqlContext, ind, filename);
            }
            ind = 0;
            for (String filename : fileNames) {
                System.out.println("FileName: " + filename + " #lines: " + lineNumbers[ind]);
                ind++;
            }
        }

        if(runScript) {
            // Combine all CSV files into one file for each group
            printForumla(itNum, hashtagNum);
            runScript("cp " + scriptPath + "mergeFiles.sh " + outputCSVPath + "mergeFiles.sh");
            runScript("chmod +x " + outputCSVPath + "mergeFiles.sh");
            runScript("./" + outputCSVPath + "mergeFiles.sh");
        }
    }

    public static void writeHeader() throws IOException {
        double featureNum = 1006133;
        FileWriter fw = new FileWriter("ClusterResults/TestTrain_Arff/header.arff");
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("@RELATION Name1\n");
        bw.write("@ATTRIBUTE topical integer\n");
        for(double i = 1; i <= featureNum; i++){
            bw.write("@ATTRIBUTE feature"+new BigDecimal(i).toPlainString()+" integer\n");
        }
        bw.close();
    }

    public static int readResults2(DataFrame results, JavaSparkContext sc, int index, String filename) throws IOException, InterruptedException {
        /*
        * testTrain_data
         |-- user: string (nullable = true)
         |-- term: string (nullable = true)
         |-- hashtag: string (nullable = true)
         |-- mentionee: string (nullable = true)
         |-- time: long (nullable = true)
         |-- tid: long (nullable = true)
         |-- topical: integer (nullable = true
        */
        String outputCSVPath2 = "ClusterResults/TestTrain/";

        final Accumulator<Integer> accumulator = sc.accumulator(0);
        JavaRDD strRes = results.javaRDD().map(new Function<Row, String>() {
            @Override
            public String call(Row row) throws Exception {
                String topical = ""; String[] features;
                String out = "", time = "";
                if(row.get(6) != null) {
                    topical = row.get(6).toString();
                }
                if(row.get(0) != null) { // FROM Feature
                    out += row.get(0).toString() + " ";
                }
                if(row.get(1) != null && !row.get(1).toString().equals("null")) // TERM Feature
                    out += row.getString(1) + " ";
                if(row.get(2) != null && !row.get(2).toString().equals("null")) { // HASHTAG Feature
                    out += row.getString(2) + " ";
                }
                if(row.get(3) != null && !row.get(3).toString().equals("null")) { // MENTION Feature
                    out += row.getString(3) + " ";
                }
                if(row.get(4) != null && !row.get(4).toString().equals("null")) { // TIME Feature
                    //time += " " + format.format(row.getLong(4));
                    time = row.get(4).toString();
                }
                //if(row.get(5) != null && !row.get(5).toString().equals("null")) { // TWEETID Feature
                //    out += " " + row.getString(5);
                //}
                if(topical.equals("1"))
                    accumulator.add(1);
                if(out.length() > 0) {
                    out = out.substring(0, out.length() - 1);
                    features = out.split(" ");
                    Set<String> set = new HashSet<String>(features.length);
                    Collections.addAll(set, features);
                    double[] tmp = new double[set.size()];
                    int i = 0;
                    for (String s : set) {
                        tmp[i] = Double.valueOf(s);
                        i++;
                    }
                    Arrays.sort(tmp);
                    out = topical;
                    for (double st : tmp)
                        out += " " + new BigDecimal(st).toPlainString() + ":1";
                }else{
                    out = topical;
                }
                out += " " + time;

                /*out = "{0 " + topical;
                for(String st: features)
                    out += "," + st + " 1";
                out += "}";*/
                return out;
            }
        });
        strRes.coalesce(1).saveAsTextFile(outputCSVPath2 + "out_" + filename + "_csv");
        System.out.println("Count: " + strRes.count());
        /*FileReader fileReaderA = new FileReader(outputCSVPath +"out_"+filename+"_csv/part-00000");
        BufferedReader bufferedReaderA = new BufferedReader(fileReaderA);
        String line;
        String name = "";
        if(filename.contains("Term"))
            name = "Term";
        else if(filename.contains("Hashtag"))
            name = "Hashtag";
        else if(filename.contains("Mention"))
            name = "Mention";
        else if(filename.contains("From"))
            name = "From";
        String path = "";
        if(filename.contains("_1_"))
            path = "Disaster/";
        else if(filename.contains("_2_"))
            path = "Politics/";
        if(filename.contains("CondEntropy"))
            path += ceName+"/" + name + "/";
        else if(filename.contains("mutualEntropy"))
            path += "MI/" + name + "/";
        else if(filename.contains("ProbTweetTrueCond"))
            path += "CP/" + name + "/";
        else if(filename.contains("ProbTweetTrueContain"))
            path += "JP/" + name + "/";
        FileWriter fw = new FileWriter(clusterResultsPath + path + name+"1.csv");
        BufferedWriter bw = new BufferedWriter(fw);
        int numberOfLines = 0;
        while((line = bufferedReaderA.readLine()) != null){
            bw.write(line);
            bw.write("\n");
            numberOfLines++;
        }
        bw.close();
        bufferedReaderA.close();
        new File(outputCSVPath +"out_"+filename+"_csv/part-00000").delete();
        if(findTopMiddle) {
            //=================== GET TOP MIDDLE BOTTOM===========
            runStringCommand("sed -n '1, " + configRead.getTopUserNum() + "p' " + outputCSVPath + "CSVOut_" + filename + ".csv >  " + outputCSVPath + "top10_CSVOut_" + filename + ".csv");
            runStringCommand("sed -n '" + ((int) Math.floor(numberOfLines / 2) - (configRead.getTopUserNum() / 2)) + ", " + ((int) Math.floor(numberOfLines / 2) + (configRead.getTopUserNum() / 2 - 1)) + "p' " + outputCSVPath + "CSVOut_" + filename + ".csv >  " + outputCSVPath + "middle10_CSVOut_" + filename + ".csv");
            runStringCommand("sed -n '" + (numberOfLines - (configRead.getTopUserNum() - 1)) + ", " + numberOfLines + "p' " + outputCSVPath + "CSVOut_" + filename + ".csv >  " + outputCSVPath + "tail10_CSVOut_" + filename + ".csv");
        }*/
        int numberOfLines = accumulator.value().intValue();
        return numberOfLines;
    }

    public static int readResultsCSV(String filename) throws IOException, InterruptedException {
        //if(!filename.startsWith("CSVOut"))
        //    return 0;
        filename = "/Volumes/SocSensor/Zahra/FeatureStatisticsRun_Sept1/ClusterResults/Disaster/MI/From/CSVOut_mutualEntropyTweetFromUser_1_parquet.csv";
        FileReader fileReaderA = new FileReader(filename);
        BufferedReader bufferedReaderA = new BufferedReader(fileReaderA);
        String line;
        FileWriter fw = new FileWriter(filename +"_2.csv");
        BufferedWriter bw = new BufferedWriter(fw);
        int numberOfLines = 0;
        String[] strs;
        while((line = bufferedReaderA.readLine()) != null){
            strs = line.split(",");
            if(line.split(",").length < 2) {
                System.out.println("LOOK: " + line);
                continue;
            }
            //if(strs[1].equals("0.0") || strs[1].equals("-0.0")) break;
            //bw.write(strs[0] + "," + new BigDecimal(strs[1]).toPlainString() + "," + strs[2]);
            bw.write(line);
            bw.write("\n");
            numberOfLines++;
        }
        bw.close();
        bufferedReaderA.close();
        if(findTopMiddle) {
            //=================== GET TOP MIDDLE BOTTOM===========
            runStringCommand("sed -n '" + ((int) Math.floor(numberOfLines / 2) - 5) + ", " + ((int) Math.floor(numberOfLines / 2) + 4) + "p' " + outputCSVPath + "NoZero_" + filename + " >  " + outputCSVPath + "middle10_NoZero_" + filename);
            runStringCommand("sed -n '" + (numberOfLines - 9) + ", " + numberOfLines + "p' " + outputCSVPath + "NoZero_" + filename + " >  " + outputCSVPath + "tail10_NoZero_" + filename);
        }
        System.out.println("Filename: " + filename + " #lines: " + numberOfLines);
        return numberOfLines;
    }

    public static int readResults1(DataFrame results, SQLContext sqlContext, int index, String filename) throws IOException, InterruptedException {
        /**/

        JavaRDD strRes = results.javaRDD().map(new Function<Row, String>() {//.distinct()
            @Override
            public String call(Row row) throws Exception {
                return row.getLong(0) + "," + row.getString(1);
            }
        });
        strRes.coalesce(1).saveAsTextFile("/Volumes/SocSensor/Zahra/Tweet_Term_hashtag/" +"outTerms_"+filename+"_csv");
        //strRes.coalesce(1).saveAsTextFile(outputCSVPath +"out_"+filename+"_csv");
        /*FileReader fileReaderA = new FileReader(outputCSVPath +"out_"+filename+"_csv/part-00000");
        BufferedReader bufferedReaderA = new BufferedReader(fileReaderA);
        String line;
        FileWriter fw = new FileWriter(outputCSVPath +"CSVOut_"+filename+".csv");
        BufferedWriter bw = new BufferedWriter(fw);
        int numberOfLines = 0;
        while((line = bufferedReaderA.readLine()) != null){
            bw.write(line);
            bw.write("\n");
            numberOfLines++;
        }
        bw.close();
        if(findTopMiddle) {
            //=================== GET TOP MIDDLE BOTTOM===========
            runStringCommand("sed -n '1, " + configRead.getTopUserNum() + "p' " + outputCSVPath + "CSVOut_" + filename + ".csv >  " + outputCSVPath + "top10_CSVOut_" + filename + ".csv");
            runStringCommand("sed -n '" + ((int) Math.floor(numberOfLines / 2) - (configRead.getTopUserNum() / 2)) + ", " + ((int) Math.floor(numberOfLines / 2) + (configRead.getTopUserNum() / 2 - 1)) + "p' " + outputCSVPath + "CSVOut_" + filename + ".csv >  " + outputCSVPath + "middle10_CSVOut_" + filename + ".csv");
            runStringCommand("sed -n '" + (numberOfLines - (configRead.getTopUserNum() - 1)) + ", " + numberOfLines + "p' " + outputCSVPath + "CSVOut_" + filename + ".csv >  " + outputCSVPath + "tail10_CSVOut_" + filename + ".csv");
        }*/
        return 0;
    }


    public static ArrayList<String> listFilesForFolder(final File folder) {
        ArrayList<String> fileNames = new ArrayList<String>();
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                fileNames.add(fileEntry.getName());
                System.out.println(fileEntry.getName());
                //listFilesForFolder(fileEntry);
            } else {
                if(!fileEntry.getName().startsWith(".") && !fileEntry.getName().startsWith("_")) {
                    fileNames.add(fileEntry.getName());
                    System.out.println(folder.getPath() + "/" +  fileEntry.getName());
                }
            }
        }
        return fileNames;
    }

    /**
     * Run script.
     *
     * @param scriptFile the script file
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws InterruptedException the interrupted exception
     */
    public static void runScript(final String scriptFile) throws IOException, InterruptedException {
        final String command = scriptFile;
        if (!new File(command).exists() || !new File(command).canRead() || !new File(command).canExecute()) {
            System.err.println("Cannot find or read " + command);
            System.err.println("Make sure the file is executable and you have permissions to execute it. Hint: use \"chmod +x filename\" to make it executable");
            throw new IOException("Cannot find or read " + command);
        }
        final int returncode = Runtime.getRuntime().exec(new String[] { "bash", "-c", command }).waitFor();
        if (returncode != 0) {
            System.err.println("The script returned an Error with exit code: " + returncode);
            throw new IOException();
        }
    }

    public static void runStringCommand(final String command) throws IOException, InterruptedException {
        final int returncode = Runtime.getRuntime().exec(new String[] { "bash", "-c", command }).waitFor();
        if (returncode != 0) {
            System.err.println("The script returned an Error with exit code: " + returncode);
            throw new IOException();
        }
    }

    public static void printForumla(int itNum, int hashtagNum){
        String str = "=AVERAGE(";
        for(int i = 1; i <= itNum; i++)
            str += "B" + String.valueOf((i-1)*hashtagNum + 1) + ",";
        str += ")";
        System.out.println(str);
    }


    public static int readResultsCSV2(String path, String filename) throws IOException, InterruptedException {
        //if(!filename.startsWith("From") || !filename.startsWith("Mention") || !filename.startsWith("Hashtag"))
        //    return 0;
        FileReader fileReaderA = new FileReader("ClusterResults/counts/name_numbers/CSVOut_term_tweetCount_parquet.csv");
        BufferedReader bufferedReaderA = new BufferedReader(fileReaderA);
        String line;
        FileWriter fw = new FileWriter("ClusterResults/counts/name_numbers/CSVOut_term_tweetCount_parquet1.csv");
        BufferedWriter bw = new BufferedWriter(fw);
        int numberOfLines = 0;
        String [] strs; double val;
        while((line = bufferedReaderA.readLine()) != null){
            strs = line.split(",");
            bw.write(new BigDecimal(strs[1]).toPlainString() + "," + strs[0]);
            bw.write("\n");
            numberOfLines++;
        }
        bw.close();
        bufferedReaderA.close();
        System.out.println("Filename: " + path + filename + " #lines: " + numberOfLines);
        return numberOfLines;
    }

    public static void makeScatterFiles() throws IOException {

        String[] hashtagCounts = {"CSVOut_hashtag_tweetCount_parquet.csv", "CSVOut_hashtag_userCount_parquet.csv"};
        String[] userCounts = {"CSVOut_user_hashtagCount_parquet.csv", "CSVOut_user_tweetCount_parquet.csv"};
        String[] termCounts = {"CSVOut_term_tweetCount_parquet.csv"};
        String hashtagProbPath, hashtagUniqueCountPath, outputPath, outputPath2, commonPath, countName;
        HashMap<String, String[]> hashMap;
        List<ScatterPlot> objects;
        boolean flagCE;
        for(String topic : topics) {
            for (String subAlg : subAlgs) {
                for (String feature : features) {
                    for(int c = 0; c < 2; c++) {
                        if (feature.equals("Term") && c == 1)//no second count for term
                            continue;
                        commonPath =  clusterResultsPath + topic + "/" + subAlg + "/";
                        //if(subAlg.equals("CE")) // for CE, we consider only non-zero values
                        //    hashtagProbPath = commonPath + feature + "/NoZero_" + feature + "1.csv";
                        //else
                        hashtagProbPath = commonPath + feature + "/" + feature + "1.csv";
                        switch (feature) {
                            case "Hashtag":
                                countName = hashtagCounts[c];
                                break;
                            case "Term":
                                countName =  termCounts[c];
                                break;
                            default:
                                countName =  userCounts[c];
                                break;
                        }
                        hashtagUniqueCountPath = "/Volumes/SocSensor/Zahra/Sept16/ClusterResults/counts/name_numbers/" + countName;
                        outputPath = commonPath + feature + "_" + countName.split("[._]")[2] + "_" + subAlg + ".csv";
                        outputPath2 = commonPath + feature + "_" + countName.split("[._]")[2] + "_" + subAlg + "_keys.csv";
                        FileReader fileReaderA = new FileReader(hashtagProbPath);
                        FileReader fileReaderB = new FileReader(hashtagUniqueCountPath);
                        BufferedReader bufferedReaderA = new BufferedReader(fileReaderA);
                        BufferedReader bufferedReaderB = new BufferedReader(fileReaderB);
                        hashMap = new HashMap<>();
                        String line;
                        //FileWriter fw = new FileWriter(outputPath);
                        //BufferedWriter bw = new BufferedWriter(fw);
                        FileWriter fw2 = new FileWriter(outputPath2);
                        BufferedWriter bw2 = new BufferedWriter(fw2);
                        int numberOfLines = 0;
                        String[] strs;
                        double val;
                        String[] value;
                        System.out.println( hashtagProbPath + " --- " + hashtagUniqueCountPath);
                        while ((line = bufferedReaderB.readLine()) != null) {
                            strs = line.split(",");
                            if(strs.length <2)
                                continue;
                            if(Double.valueOf(strs[0]) < 5)
                                break;
                            if (!hashMap.containsKey(strs[1])) {
                                value = new String[2];
                                value[0] = strs[0];// first string is the tweetCount
                                value[1] = "";// second String is the probability
                                hashMap.put(strs[1], value);
                            }else {
                                System.out.println("Something wrong " + strs[0] + " " + strs[1]);
                            }
                        }
                        System.out.println("First file done");
                        while ((line = bufferedReaderA.readLine()) != null) { // Mention, 0.01, username
                            strs = line.split(",");
                            if(strs.length <2)
                                continue;
                            if(strs.length == 2){
                                if (hashMap.containsKey(strs[1])) {
                                    value = hashMap.get(strs[1]);
                                    value[1] = new BigDecimal(strs[0]).toPlainString();
                                    hashMap.put(strs[1], value);
                                }
                            }else {
                                if (hashMap.containsKey(strs[2])) {
                                    value = hashMap.get(strs[2]);
                                    value[1] = new BigDecimal(strs[1]).toPlainString();
                                    hashMap.put(strs[2], value);
                                }
                            }/*else {
                                if(!subAlg.equals("CE"))
                                    System.out.println("Something wrong " + strs[0] + " " + strs[1] + "  " + strs[2]);
                            }*/
                        }
                        System.out.println("Second file done");
                        objects = new ArrayList<ScatterPlot>();
                        flagCE = subAlg.equals("CE");
                        for (String key : hashMap.keySet()) {
                            if(hashMap.get(key)[1].equals(""))
                                continue;
                            //objects.add(new ScatterPlot(Double.valueOf(hashMap.get(key)[1]), Double.valueOf(hashMap.get(key)[0]), key, flagCE));
                            objects.add(new ScatterPlot(Double.valueOf(hashMap.get(key)[1]), Double.valueOf(hashMap.get(key)[0]), key, flagCE));
                        }
                        Collections.sort(objects);
                        for (int i = 0; i < objects.size(); i++) {
                            if(i < 10)
                                bw2.write(objects.get(i).getSecondDimCount() + ","+objects.get(i).getFeatureValue()+","+objects.get(i).getFeatureKey()+","+1);
                            else if(i >= (objects.size()/2 - 5) && i < (objects.size()/2 + 5))
                                bw2.write(objects.get(i).getSecondDimCount() + ","+objects.get(i).getFeatureValue()+","+objects.get(i).getFeatureKey()+","+2);
                            else if(i >= objects.size()-10)
                                bw2.write(objects.get(i).getSecondDimCount() + ","+objects.get(i).getFeatureValue()+","+objects.get(i).getFeatureKey()+","+3);
                            else
                                bw2.write(objects.get(i).getSecondDimCount() + ","+objects.get(i).getFeatureValue()+","+""+",0");
                            bw2.write("\n");
                            //if(hashMap.get(key)[1].equals("")){
                                //if(!subAlg.equals("CE"))
                                //    System.out.println("Something wrong " + key + " "  + hashMap.get(key)[0]);
                                //else
                            //    continue;
                            //}
                            //bw.write(hashMap.get(key)[0] + "," + hashMap.get(key)[1]);
                            //bw.write("\n");
                        }
                        //bw.close();
                        bw2.close();
                        bufferedReaderA.close();
                        bufferedReaderB.close();
                    }
                }
            }
        }
    }

    public static void cleanTerms() throws IOException {
        FileReader fileReaderA = new FileReader(outputCSVPath+"/part-00000");
        BufferedReader bufferedReaderA = new BufferedReader(fileReaderA);
        String line;
        FileWriter fw = new FileWriter(outputCSVPath +"cleanTerms.csv");
        BufferedWriter bw = new BufferedWriter(fw);
        int[] lineNum5_10_50_100 = new int[4];
        for(int i = 0; i < 4; i++)
            lineNum5_10_50_100[i] = 0;
        int numberOfLines = 0;
        String[] strs;
        while((line = bufferedReaderA.readLine()) != null){
            strs = line.split(",");
            strs[1] = new BigDecimal(strs[1]).toPlainString();
            numberOfLines++;
            if(numberOfLines < 571)
                continue;
            if(Double.valueOf(strs[1]) < 100) {
                lineNum5_10_50_100[3]++;
                if(Double.valueOf(line.split(",")[1]) < 50)
                    lineNum5_10_50_100[2]++;
                if(Double.valueOf(line.split(",")[1]) < 10)
                    lineNum5_10_50_100[1]++;
                if(Double.valueOf(line.split(",")[1]) < 5)
                    lineNum5_10_50_100[0]++;
                continue;
            }
            bw.write(strs[0] + "," + strs[1]);
            bw.write("\n");
        }
        bw.close();
        fileReaderA.close();
        System.out.println("Number of Terms with less than 5 occurences: " + lineNum5_10_50_100[0]);
        System.out.println("Number of Terms with less than 10 occurences: " + lineNum5_10_50_100[1]);
        System.out.println("Number of Terms with less than 50 occurences: " + lineNum5_10_50_100[2]);
        System.out.println("Number of Terms with less than 100 occurences: " + lineNum5_10_50_100[3]);
        System.out.println("Number of Terms retained after cleaning: " + (numberOfLines - lineNum5_10_50_100[3]));
    }


    public static void getNonZeroforCE() throws IOException, InterruptedException {
        int ind;
        System.out.println("BUILDING NON_ZERO FILES");

        FileReader fileReaderA;
        BufferedReader bufferedReaderA;
        for (String topic : topics) {
            ind = 0;
            for (String feature : features) {
                outputCSVPath = clusterResultsPath + topic + "/"+ceName+"/" + feature + "/";
                System.out.println(clusterResultsPath + topic + "/"+ceName+"/" + feature + "/" + feature + ".csv");
                fileReaderA = new FileReader(outputCSVPath +feature + "1.csv");
                bufferedReaderA = new BufferedReader(fileReaderA);
                String line;
                FileWriter fw = new FileWriter(outputCSVPath +feature + "_tmp.csv");
                BufferedWriter bw = new BufferedWriter(fw);
                int numberOfLines = 0;
                String[] strs;
                while((line = bufferedReaderA.readLine()) != null){
                    strs = line.split(",");
                    if(strs.length > 2) {
                        if (strs[1].equals("0.0") || strs[1].equals("-0.0"))
                            break;
                    }else {
                        if (strs[0].equals("0.0") || strs[0].equals("-0.0"))
                            break;
                    }
                    bw.write(line);
                    bw.write("\n");
                    numberOfLines++;
                }
                bufferedReaderA.close();
                bw.close();
                ind++;
                runStringCommand("rm "+outputCSVPath + feature + "1.csv");
                runStringCommand("mv "+outputCSVPath + feature + "_tmp.csv" + " " + outputCSVPath + feature + "1.csv");
            }
        }
    }

    public static void fixNumbers(String clusterResultPath, String topic, String subAlg, String feature) throws IOException, InterruptedException {
        String path = clusterResultsPath+ topic + "/" + subAlg + "/" +feature +"/";
        String fileName = feature+".csv";
        FileReader fileReaderA = new FileReader(path+fileName);
        BufferedReader bufferedReaderA = new BufferedReader(fileReaderA);
        String line;
        FileWriter fw = new FileWriter(path +"Tmp_"+fileName);
        BufferedWriter bw = new BufferedWriter(fw);
        int numberOfLines = 0;
        String [] strs;
        while((line = bufferedReaderA.readLine()) != null){
            strs = line.split(",");
            if(strs.length < 2) {
                if (line.contains("1.0132366607773573E-7"))
                    continue;
                continue;
            }
            if(strs.length > 2)
                bw.write(feature + "," + new BigDecimal(strs[1]).toPlainString() + "," + strs[2]);
            else
                bw.write(feature + "," + new BigDecimal(strs[0]).toPlainString() + "," + strs[1]);
            bw.write("\n");
            numberOfLines++;
        }
        bw.close();
        bufferedReaderA.close();
        runStringCommand("rm " + path + fileName);
        runStringCommand("mv " + path + "Tmp_" + fileName + " " + path + fileName);
        System.out.println("DONE");
    }

    public static void getLists() throws IOException, InterruptedException {
        String sortRandomly;
        getNonZeroforCE(); // This is from the original result file outputted from cluster in the format: value1, feature1
        for (String topic : topics) {
            for(String subAlg: subAlgs) {
                for(String feature : features) {
                    if(subAlg.equals("CE") || subAlg.equals("CE_Suvash")) {
                        runStringCommand("cd " + clusterResultsPath + topic + "/" + subAlg + "/; tail -" + topFeatureNum + " " + feature + "/" + feature + "1.csv > " + feature + "/" + feature + ".csv;");
                    }else
                        runStringCommand("cd " + clusterResultsPath + topic + "/" + subAlg + "/; sed -n '1,"+topFeatureNum+"p' " + feature + "/" + feature + "1.csv > " + feature + "/" + feature + ".csv;");
                    // Fix scientific numbers and add featureName column to the beginning
                    fixNumbers(clusterResultsPath, topic, subAlg, feature);
                    // SORT the CE lowest to highest
                    if(subAlg.equals("CE") || subAlg.equals("CE_Suvash")) {
                        runStringCommand("cd " + clusterResultsPath + topic + "/" + subAlg + "/; sort --field-separator=',' -n -k2,2 " + feature + "/" + feature + ".csv > " + feature + "/" + feature + "_tmp.csv;");
                        runStringCommand("rm " +  clusterResultsPath + topic + "/" + subAlg + "/" + feature + "/" + feature + ".csv");
                        runStringCommand("mv " + clusterResultsPath + topic + "/" + subAlg + "/" + feature + "/" + feature + "_tmp.csv  " + clusterResultsPath + topic + "/" + subAlg + "/" + feature + "/" + feature + ".csv");
                    }
                }
                String command = "cd " + clusterResultsPath + topic + "/" + subAlg + "/; cat ";
                for(String feature: features)
                    command += feature + "/" + feature + ".csv ";
                command += " > mixed.csv";

                runStringCommand(command);

                if(checkEquality(clusterResultsPath + topic + "/" + subAlg + "/mixed.csv")) {
                    sortRandomly = "";
                    for(String feature: features)
                        sortRandomly += "head -"+topFeatureNum + " " + clusterResultsPath + topic + "/" + subAlg +"/" + feature + "/" + feature + ".csv > " + feature + "2.csv; ";
                    sortRandomly = "cat ";
                    for(String feature: features)
                        sortRandomly += clusterResultsPath + topic + "/" + subAlg + "/" + feature +"/" + feature + "2.csv ";
                    sortRandomly += "; rm ";
                    for(String feature: features)
                        sortRandomly += clusterResultsPath + topic + "/" + subAlg + "/" + feature +"/" + feature + "2.csv ";
                    runStringCommand(sortRandomly);
                }
                if(subAlg.equals("CE") || subAlg.equals("CE_Suvash"))
                    runStringCommand("cd " + clusterResultsPath + topic + "/" + subAlg + "/; sort --field-separator=',' -n -k2,2 mixed.csv  > mixed1.csv;  sed -n '1,"+topFeatureNum+"p' mixed1.csv > mixed.csv; rm mixed1.csv;");
                else
                    runStringCommand("cd " + clusterResultsPath + topic + "/" + subAlg + "/; sort --field-separator=',' -rn -k2,2 mixed.csv  > mixed1.csv;  sed -n '1,"+topFeatureNum+"p' mixed1.csv > mixed.csv; rm mixed1.csv;");
            }
        }
    }

    private static boolean checkEquality(String fileName) throws IOException {
        FileReader fileReaderA = new FileReader(fileName);
        BufferedReader bufferedReaderA = new BufferedReader(fileReaderA);
        String line;
        String [] strs;
        double value = Double.valueOf(bufferedReaderA.readLine().split(",")[1]);
        while((line = bufferedReaderA.readLine()) != null){
            if(Double.valueOf(line.split(",")[1]) != value) {
                bufferedReaderA.close();
                return false;
            }
        }
        bufferedReaderA.close();
        return true;
    }
}













