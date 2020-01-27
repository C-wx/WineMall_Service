package winemall.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Explain 上传图片的工具类
 */
@Slf4j
public class FileUpload {

    public static List<String> upload_image(MultipartFile[] files, HttpSession session) {
        String prefix = "file:D://WineMall_Service/upload/";
        List<String> list_image = new ArrayList<String>();
        for (int i = 0; i < files.length; i++) {
            if (!files[i].isEmpty()) {
                String originalFilename = files[i].getOriginalFilename();
                String uploadPathPre = Objects.requireNonNull(files[i].getContentType()).contains("image/") ? "/img" : "/file";
                String datePrefix = LocalDate.now().toString();
                String completePrefix = prefix + uploadPathPre + "/" + datePrefix + "/";
                String ext = originalFilename.substring(Objects.requireNonNull(originalFilename).lastIndexOf("."));
                String newFileName = IdUtil.randomUUID().concat(ext);
                File targetFile = new File(completePrefix.substring(5));
                boolean m = true;
                if (!targetFile.exists()) {
                    m = targetFile.mkdirs();
                }
                String uploadFilePath = null;
                if (m) {
                    uploadFilePath = FileUtil.getAbsolutePath(completePrefix + newFileName);
                } else {
                    log.error("创建目录：" + completePrefix + "失败！");
                }
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(uploadFilePath);
                    out.write(files[i].getBytes());
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String virtualPath = "/upfiles".concat(uploadPathPre).concat("/" + datePrefix + "/").concat(newFileName);
                list_image.add(virtualPath);
            }
        }
        return list_image;
    }
}
