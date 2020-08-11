package com.company;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args)throws Exception {
/*
        File file = new File("H:\\内存卡\\VideoData\\1565960151333 - 副本\\k0");
        String convert = convert(file);
        mergeFile(convert);

        System.out.println(convert);*/
        mergeFile(new File("需要合并的文件夹路径"),"",false);
        // write your code here
/*String str="283dcc2d3747e9650a81ead31e04fa8a";

        System.out.println(str.length());*/
    }
    public static void  test() {
        File dirFiles = new File("H:\\内存卡\\video\\CLOUDPLAY\\VideoData\\2018.9.20");
        File[] files = dirFiles.listFiles(e -> e.isDirectory());
        for (File everyFile:files) {
            File file = new File(everyFile.getAbsolutePath()+"/"+"k0");
            try {
                if(!file.exists()){
                    mergeFile(everyFile,"",false);
                    return;
                }
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                String s = bufferedReader.readLine();
                System.out.println(s);
                bufferedReader.close();
                mergeFile(everyFile,s,true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将文件二进制转换为16进制，没有使用
     * @param file
     * @return
     * @throws Exception
     */
    public static String convert(File file)throws Exception{
        InputStream is = new FileInputStream(file);

        int bytesCounter =0;
        int value = 0;
        StringBuilder sbHex = new StringBuilder();
        StringBuilder sbText = new StringBuilder();
        StringBuilder sbResult = new StringBuilder();

        while ((value = is.read()) != -1) {
            //convert to hex value with "X" formatter
            sbHex.append(String.format("%02X", value));

            //If the chracater is not convertable, just print a dot symbol "."
            if (!Character.isISOControl(value)) {
                sbText.append((char)value);
            }else {
                sbText.append(".");
            }

            //if 16 bytes are read, reset the counter,
            //clear the StringBuilder for formatting purpose only.
            if(bytesCounter==15){
                sbResult.append(sbHex).append("      ").append(sbText).append("\n");
              /*  sbHex.setLength(0);*/
                sbText.setLength(0);
                bytesCounter=0;
            }else{
                bytesCounter++;
            }
        }

        //if still got content
/*        if(bytesCounter!=0){
            //add spaces more formatting purpose only
            for(; bytesCounter<16; bytesCounter++){
                //1 character 3 spaces
                sbHex.append("   ");
            }
            sbResult.append(sbHex).append("      ").append(sbText).append("\n");
        }*/

        is.close();
        return sbHex.toString();
    }

    /**
     * 合并文件
     * @param sourceFile 需要合并的源文件夹
     * @param key  密钥
     * @param needDecode  是否需要解密，需要解密，则必须传入密钥，不需要则可以将密钥设置为null或任意字符即可
     */
    public static void mergeFile(File sourceFile,String key,boolean needDecode) {
        List<File> collect = Arrays.stream(sourceFile.listFiles()).filter(f -> {
            String name = f.getName();
            name=name.replaceAll("Y2hlbmppbmdjb25n","");
            try {
                int i = Integer.parseInt(name);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }).collect(Collectors.toList());

      /*  collect.sort(new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {

            }
        });*/
        Collections.sort(collect, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                int i1 = Integer.parseInt(o1.getName());
                int i2=Integer.parseInt(o2.getName());
                return i1-i2;
            }
        });
        for(int i=0;i<collect.size();i++){
            System.out.println(collect.get(i).getName());
        }
      /*  System.out.println(file.getParent()+"\\"+file.getName()+".mp4");
       if(true){
           return;
       }*/
        File finalOutPutFile = new File(sourceFile.getParent()+"\\"+sourceFile.getName()+".mp4");

        try {
            FileOutputStream fileOutputStream=new FileOutputStream(finalOutPutFile,true);
            for(int i=0;i<collect.size();i++){
               /* File file2 = collect.get(i);
                if(!new File("E:/file").exists()){
                    new File("E:/file").mkdirs();
                }*/
              /*  EncryFileUtil.decryptFile(file2.getAbsolutePath(),"E:/file/"+file2.getName());
                if(true){
                    return;
                }*/
                FileInputStream fileInputStream = new FileInputStream(collect.get(i));
                byte b[]=new byte[4096];
                int size=-1;
                ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                while((size=fileInputStream.read(b,0,b.length))!=-1) {
                    byteArrayOutputStream.write(b,0,size);
                }
                fileInputStream.close();
                byte[] bytes = byteArrayOutputStream.toByteArray();
                byteArrayOutputStream.close();
                byte [] newbyte;
                if(needDecode) {
                   newbyte = AesUtil.aesDecry(bytes, key);
                }else newbyte=bytes;
                fileOutputStream.write(newbyte);

            }
            if(fileOutputStream!=null){
                fileOutputStream.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
