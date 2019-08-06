import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

public class T {


    public static void main(String[] args) throws Exception {
        download("https://test.haocheedai.com/data/media/img/discover/lowprice.png","D:\\upload\\SDSPage");
    }

    public static String download(String urlPath,String savePath) throws Exception {
        // 构造URL
        URL url = new URL(urlPath);
        // 打开连接
        URLConnection con = url.openConnection();
        //设置请求超时为5s
        con.setConnectTimeout(5*1000);
        // 输入流
        InputStream is = con.getInputStream();
        // 1K的数据缓冲
        byte[] bs = new byte[1024];
        // 读取到的数据长度
        int len;
        // 输出的文件流
        File sf=new File(savePath);
        if(!sf.exists()){
            sf.mkdirs();
        }
        int randomNo=(int)(Math.random()*1000000);
        String filename=urlPath.substring(urlPath.lastIndexOf("/")+1,urlPath.length());//获取服务器上图片的名称
        OutputStream os = new FileOutputStream(sf.getPath()+"\\"+filename);
        String virtualPath="/upload/SDSPage/"+filename;//存入数据库的虚拟路径
        // 开始读取
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        // 完毕，关闭所有链接
        os.close();
        is.close();
        return virtualPath;
    }
}