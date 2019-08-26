package cn.datacharm.mallimageservice.service;

import cn.datacharm.utils.UploadUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import cn.datacharm.vo.PicUploadResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * description:
 *
 * @author Herb
 * @date 2019/08/20
 */
@Service
public class PicService {
    @Value("${url.path}")
    private String urlPath;
    @Value("${disk.path}")
    private String diskPath;

    public PicUploadResult imgUp(MultipartFile pic) {
        //准备一个空数据对象

        PicUploadResult result = new PicUploadResult();
        //获取图片的后缀,判断是否合法(图片源文件的名称)
        String oldName = pic.getOriginalFilename();
        String extName = oldName.substring(oldName.lastIndexOf("."));
        //通过正则表达式来判断是否合法
        if (!extName.matches(".(jpg|png|gif)")){
            //进入if说明后缀不合法
            result.setError(1);//表示上传失败
            return result;
        }
        //生成一个公用路径;直接调用upload的路径生成器,根据图片名称来生成路径
        //参数一:filename图片名称;参数二:upload是前缀
        //根据图片名称字符串,生成hash散列的8位字符的字符串
        //3d22dghj
        //根据传入的前缀名,拼接形成公用文件夹结构
        String dir ="/" + UploadUtil.getUploadPath(oldName,"upload") + "/";
        //根据公用路径,根据nginx访问的静态文件位置G:/TarenaImg
        //防止一个文件夹中存在多个文件,导致查询效率低下
        String diskDir=diskPath+dir;//将图片数据输出到文件夹
        //先判断是否存在
        File _dir=new File(diskDir);
        if(!_dir.exists()){//不存在,则要创建
            _dir.mkdirs();
        }
        //没有进入if,说明已经存在,直接使用,将pic中的数据输出保存到文件夹,重命名文件夹的名称
        //重命名
        String fileName = UUID.randomUUID().toString()+extName;
        //将pic输出为_dir中的一个fileName,名称的文件
        try {
            //将对象的数据,按照指定的file的路径之,输出到磁盘中生成一个文件
            pic.transferTo(new File(diskDir+fileName));
        } catch (IOException e) {
            e.printStackTrace();
            result.setError(1);
            return result;
        }
        //生成url地址
        String urlName = urlPath + dir + fileName;
        result.setUrl(urlName);
        return result;
    }
}
