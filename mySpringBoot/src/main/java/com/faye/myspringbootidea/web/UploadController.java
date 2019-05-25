package com.faye.myspringbootidea.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

@Controller
public class UploadController {

    @RequestMapping("/hello")
    public String hello(Model m) throws Exception {
        m.addAttribute("now", DateFormat.getDateTimeInstance().format(new Date()));
//        if(true) throw new Exception("some exception for test");
        return "hello"; //这时返回"hello"就不再是字符串，而是根据application.properties 中的视图重定向，到/WEB-INF/jsp目录下去寻找hello.jsp文件
    }

    //因为uploadPage.jsp 在WEB-INF下，不能直接从浏览器访问，所以要在这里加一个uploadPage跳转，这样就可以通过
    @RequestMapping("/uploadPage")
    public String uploadPage() {
        return "uploadPage";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String upload(HttpServletRequest httpServletRequest,
                         //1. 接受上传的文件
                         @RequestParam("file") MultipartFile file, Model model){

        try {
            //2. 根据时间戳创建新的文件名，这样即便是第二次上传相同名称的文件，也不会把第一次的文件覆盖了
            String fileName = System.currentTimeMillis() + file.getOriginalFilename();
            //3. .getServletContext().getRealPath("") 获取当前项目的真实路径，然后拼接前面的文件名; File.separator:分隔符
            String destFileName = httpServletRequest.getServletContext().getRealPath("") + "uploaded" + File.separator + fileName;
            //4. 第一次运行的时候，这个文件所在的目录往往是不存在的，这里需要创建一下目录
            File destFile = new File(destFileName);
            destFile.getParentFile().mkdir();
            //5. 把浏览器上传的文件复制到希望的位置
            file.transferTo(destFile);
            //6. 把文件名放在model里，以便后续显示用
            model.addAttribute("fileName", fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return "Upload failed: " + e.getMessage();
        }
        return "showImg";
    }
}
