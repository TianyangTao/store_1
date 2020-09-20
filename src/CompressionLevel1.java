import javax.xml.crypto.Data;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/*
 * @ClassName: Compression
 * @Description: 继续深入，批量及优化
 *
 *
 * @author TaoTianYang
 * @date 2020年9月20日 下午15.21
 * @version V1.1
 */
public class CompressionLevel1 {
    static String compresspath="D:\\guanwang";
    public static void main(String[] args){
        boolean b1=FileToZip(compresspath);
        if (b1)
        {
            System.out.println("压缩成功");
        }
    }
    public static boolean FileToZip(String compresspath)
    {
        boolean bool=false;
        try {
            ZipOutputStream zipOutputStream=null;
            File file=new File(compresspath);
            //检查是否是文件夹
            if(file.isDirectory())
            {
                /*
                file.getName() 解决java.io.FileNotFoundException:（拒绝访问） 异常
                 */
                zipOutputStream=new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(compresspath+file.getName())));
                compressZip(zipOutputStream,file,"");
            }
            else
            {
                zipOutputStream =new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(compresspath.substring(0,compresspath.lastIndexOf("."))+".zip")));
                zipFile(zipOutputStream,file);
            }
            zipOutputStream.closeEntry();
            zipOutputStream.close();
            bool=true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return bool;
    }
    public static void compressZip(ZipOutputStream zipOutputStream,File file,String suffixpath)
    {
        /*
        list()方法是返回某个目录下的所有文件和目录的文件名，返回的是String数组
        listFiles()方法是返回某个目录下所有文件和目录的绝对路径，返回的是File数组
         */
        File [] listFiles=file.listFiles();
        for (File fi:listFiles)
        {
            //检查是否是文件
            if(fi.isDirectory())
            {
                if(suffixpath.equals(""))
                {
                    compressZip(zipOutputStream,fi,fi.getName());
                }
                else{
                    compressZip(zipOutputStream,fi,suffixpath+File.separator+fi.getName());
                }
            }
            else
            {
                zip(zipOutputStream,fi,suffixpath);
            }
        }
    }
    public static  void zip(ZipOutputStream zipOutputStream,File file,String suffixpath)
    {
        try{
            ZipEntry zipEntry=null;
            if (suffixpath.equals(""))
            {
                zipEntry=new ZipEntry(file.getName());
            }
            else
            {
                /*
                File.separator这个代表系统目录中的间隔符，说白了就是斜线，不过有时候需要双线，有时候是单线，你用这个静态变量就解决兼容问题了
                 */
                zipEntry=new ZipEntry(suffixpath+File.separator+file.getName());
            }
            zipOutputStream.putNextEntry(zipEntry);
            BufferedInputStream bufferedInputStream=new BufferedInputStream(new FileInputStream(file));
            byte [] buffer=new byte[1024];
            int read=0;
            while((read=bufferedInputStream.read(buffer))!=-1)
            {
                zipOutputStream.write(buffer,0,read);
            }
            bufferedInputStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static void zipFile(ZipOutputStream zipOutputStream,File file)
    {
        try{
            ZipEntry zipEntry=new ZipEntry(file.getName());
            zipOutputStream.putNextEntry(zipEntry);
            BufferedInputStream bufferedInputStream=new BufferedInputStream(new FileInputStream(file));
            byte [] buffer=new byte[1024];
            int read=0;
            while((read=bufferedInputStream.read(buffer))!=-1)
            {
                zipOutputStream.write(buffer,0,read);
            }
            bufferedInputStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
