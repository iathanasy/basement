package com.eyxyt.basement;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * @author cd.wang
 * @create 2020-07-23 9:10
 * @since 1.0.0
 */
public class SplitFile {

    //被分割文件的路径
    private String srcPath;
    //被分割的文件对象
    private File src;
    //分割的块数
    private int size;
    //每一块的大小
    private long blockSize;
    //每一块的实际大小
    private long actualBlockSize;
    //原文件的长度
    private long length;
    //分割得到的文件名的集合
    private List<String> splitFileNames=new ArrayList<String>();

    public SplitFile(String srcPath,long blockSize){
        this.srcPath=srcPath;
        this.blockSize=blockSize;
        init();
    }

    /**
     * 初始化文件分隔需要用到的参数
     */
    private void init(){
        //路径为空，则抛出异常
        if(srcPath==null){
            throw new RuntimeException("文件路径不合法");
        }

        src=new File(srcPath);

        //文件是否存在
        if(!src.exists()){
            throw new RuntimeException("文件不存在");
        }
        //是否是文件夹
        if(src.isDirectory()){
            throw new RuntimeException("不能分割文件夹");
        }

        //如果文件存在
        length=src.length();

        if(length<this.blockSize){
            this.blockSize=length;
        }

        //计算分割的块数
        size=(int) ((length-1)/this.blockSize+1);
        //初始化分割后的文件名集合
        initSplitFileNames();
    }

    /**
     * 初始化文件被分割后新生成文件的名字
     * 格式为：原文件名+第几块+扩展名
     */
    private void initSplitFileNames() {
        //文件的全名（包含后缀）
        String fileName=src.getName();
        //获得文件的扩展名前的分隔符‘.’
        int index=fileName.indexOf('.');
        //文件名的前缀
        String prefixName=fileName.substring(0,index );
        //文件名的后缀
        String extName=fileName.substring(index);
        for(int i=0;i<size;i++){
            splitFileNames.add(prefixName+(i+1)+extName);
        }
    }

    /**
     * 文件分割的详细细节
     */
    public void split(){
        RandomAccessFile raf=null;
        OutputStream os=null;
        try {
            raf=new RandomAccessFile(src,"r");
            byte[] b=new byte[1024];
            int len=0;
            for(int i=0;i<size;i++){
                raf.seek(blockSize*i);

                //计算最后一块的实际大小
                if(i==(size-1)){
                    actualBlockSize=length-blockSize*(size-1);
                }else{
                    actualBlockSize=blockSize;
                }

                os=new BufferedOutputStream(new FileOutputStream(new File(src.getParent(),splitFileNames.get(i))));
                while(-1!=(len=raf.read(b))){
                    //如果读取的长度已经超过了实际的长度，则只需要写实际长度的数据
                    if(len>=actualBlockSize){
                        os.write(b, 0, (int) actualBlockSize);
                        os.flush();
                        break;
                    }else{
                        os.write(b, 0, len);
                        os.flush();
                        actualBlockSize-=len;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(os!=null){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(raf!=null){
                try {
                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * 文件合并
     * @param destPath 目标目录
     */
    public void merge(String destPath){
        if(destPath==null||"".equals(destPath)||splitFileNames == null){
            System.out.println("合并失败");
        }
        //获取第一个文件
        String fileName = splitFileNames.get(0);
        //获得文件的扩展名前的分隔符‘.’
        int index=fileName.indexOf('.');
        //文件名的前缀
        String prefixName=fileName.substring(0,index - 1);
        //文件名的后缀
        String extName=fileName.substring(index);

        //合并后的文件名
        destPath = destPath + prefixName + "_merge" + extName;//合并后的文件路径

        File destFile = new File(destPath);//合并后的文件
        OutputStream out = null;
        BufferedOutputStream bos = null;
        try {
            out = new FileOutputStream(destFile);
            bos = new BufferedOutputStream(out);
            String srcPath = src.getParent() + "\\";
            for (String src : splitFileNames) {
                File srcFile = new File( srcPath + src);
                InputStream in = new FileInputStream(srcFile);
                BufferedInputStream bis = new BufferedInputStream(in);
                byte[] bytes = new byte[1024];
                int len = -1;
                while((len = bis.read(bytes))!=-1){
                    bos.write(bytes, 0, len);
                }
                bis.close();
                in.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            //关闭流
            try {
                if(bos!=null)bos.close();
                if(out!=null)out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    //测试
    public static void main(String[] args) {
        SplitFile sf=new SplitFile("F:\\logs\\file\\debug.log",1024);

        //拆分文件
        sf.split();

        //合并文件
        sf.merge("F:\\logs\\file\\");
    }

}