import javax.xml.crypto.Data;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/*
 * @ClassName: Compression
 * @Description: 压缩文件,接口详解，
 *               已测试三种read方法源码，本质是for循环单字节读取，相比较read(byte[] b)较为直观
 *               继续深入，批量及优化
 * @author TaoTianYang
 * @date 2020年9月19日 下午17.07
 * @version V1.0
 */
public class CompressionLevel {
    //存放路径
    static String path="C:\\Users\\15962\\Desktop\\zipup\\";
    static String packfilePath=path+"timg2.jfif";
    static String packoutpath=path+"new.zip";
    public static void main(String[] args) {
        Zipcom(packfilePath,packoutpath);
    }
    public static void Zipcom(String filePath,String outpath)
    {
        /*l
        FileInputStream流被称为文件字节输入流，意思指对文件数据以字节的形式进行读取操作如读取图片视频等
        从输入流中读取一个字节返回int型变量，若到达文件末尾，则返回-1，返回byte类型
        读取的字节实际是由8位二进制组成，二进制文件不利于直观查看，可以转成常用的十进制进行展示，因此需要把读取的字节从二进制转成十进制整数，故返回int型

        BufferedInputStream 本质上是通过一个内部缓冲区数组实现的。例如，在新建某输入流对应的BufferedInputStream后，当我们通过read()读取输入流的数据时，
        BufferedInputStream会将该输入流的数据分批的填入到缓冲区中。每当缓冲区中的数据被读完之后，输入流会再次填充数据缓冲区；如此反复，直到我们读完输入流数据位置。

        ZipOutputStream java自带的api1.6会出现乱码，最好使用1.7以上版本
        如果项目中使用的是jdk1.6没办法改，这时我们就需要引进apache的ant.jar，使用它提供的ZipEntry和ZipOutputStream接口
        优势：文件压缩过后，只需要进行一次文件的传输就可以了。减少频繁发送的问题。缺点：文件大小会变大，如果传输过程中断了，风险较大

        ZipEntry 用于bai保存一些被压缩文件的信息，如du文件名，最后访问时间，最后修改时间，创建时zhi间，文件大小，crc 校验值dao 等信息。
        ZipEntry 具有一个带 String 类型参数的构造方法：ZipEntry(String name), name 是入口名称，就是打开压缩文件时，看到的里面的文件名称。
                                                                         String型参数就是压缩包内子文件的文件名对吧

        ZipOutputStream.putNextEntry：
        开始写入新的ZIP文件条目，并将流定位到条目数据的开头。 如果仍处于活动状态，则关闭当前条目。 如果没有为条目指定压缩方法，则使用默认的压缩方法；如果条目没有设置修改时间，则使用当前时间。


        具体参考java api
         */
        File file=null;
        FileInputStream fileInputStream=null;
        BufferedInputStream bufferedInputStream=null;
        DataInputStream dataInputStream=null;
        /*
        输出
         */
        File outfile=null;
        FileOutputStream fileOutputStream=null;
        BufferedOutputStream bufferedOutputStream=null;
        ZipOutputStream zipOutputStream=null;
        ZipEntry ze=null;
        try{
            /*
            1、创建输入流、输出流、创建输入流缓冲区、输出楼缓冲区
            2、dataInputStream给缓冲区提速
            3、ZipoutputStram文件压缩
            4、通过读取流，判断read返回参数是否为—1,判断是否压缩完毕
            5、闭流
             */
            file = new File(filePath);
            fileInputStream =new FileInputStream(file);
            bufferedInputStream =new BufferedInputStream(fileInputStream);
            dataInputStream=new DataInputStream(bufferedInputStream);//增强

            outfile=new File(outpath);
            fileOutputStream=new FileOutputStream(outfile);
            bufferedOutputStream=new BufferedOutputStream(fileOutputStream,1024);
            zipOutputStream=new ZipOutputStream(bufferedOutputStream);
            ze=new ZipEntry(file.getName());//压缩输出流
            zipOutputStream.putNextEntry(ze);//实体ZipEntry保存
            int len=0;//临时文件
            byte [] bts=new byte[(int)file.length()];//为了防止内存浪费，根据文件字节长度来设置
            while((len=dataInputStream.read(bts))!=-1)
            {
                System.out.println(len);        //被测试对象大小（byte）
                zipOutputStream.write(bts,0,len);
            }
            System.out.println("压缩成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            try{
                zipOutputStream.closeEntry();
                zipOutputStream.close();
                bufferedOutputStream.close();
                fileOutputStream.close();
                dataInputStream.close();
                bufferedInputStream.close();
                fileInputStream.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
