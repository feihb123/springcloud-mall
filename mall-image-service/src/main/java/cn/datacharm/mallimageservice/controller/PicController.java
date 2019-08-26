package cn.datacharm.mallimageservice.controller;

import cn.datacharm.mallimageservice.service.PicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import cn.datacharm.vo.PicUploadResult;

/**
 * description:
 * 图片上传Controller
 *
 * @author Herb
 * @date 2019/08/20
 */
@RestController
public class PicController {
    private PicService picService;
    @Autowired
    public PicController(PicService picService) {
        this.picService = picService;
    }

    @RequestMapping("pic/uploadImg")
    public PicUploadResult imgUp(MultipartFile pic) {
        System.out.println("img controller!");
        return picService.imgUp(pic);

    }
}
