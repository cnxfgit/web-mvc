package com.web.mvc.testwork.controller;

import com.web.mvc.framework.annotation.component.RequestMapping;
import com.web.mvc.framework.annotation.component.Controller;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

@Controller
public class CaptchaController {

    private final static int width = 120;// 图片宽
    private final static int height = 48;// 图片高

    private final static String[] ss = new String[]{"+","-","x"};

    static ThreadLocal<Integer> result = new ThreadLocal<>();

    @RequestMapping("/admin/captcha")
    public void sendImg(HttpServletRequest request, HttpServletResponse response){
        BufferedImage bufferedImage = getImage();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        InputStream input = null;
        try {
            ImageIO.write(bufferedImage, "png", os);
            input = new ByteArrayInputStream(os.toByteArray());
            response.setContentType("image/png");
            IOUtils.copy(input, response.getOutputStream());
            HttpSession session = request.getSession();
            session.setAttribute("captcha",result.get());
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            result.remove();
            try {
                os.close();
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 规则：0 - 9的加减乘，三次随机运算，只有第一次有机会出乘法
    public String produceString(){
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        // 0-9 随机选取数字
        int one = random.nextInt(10);
        String first = ss[random.nextInt(3)];
        int two = random.nextInt(10);
        String second = ss[random.nextInt(2)];
        int three = random.nextInt(10);
        String third = ss[random.nextInt(2)];
        int four = random.nextInt(10);
        sb.append(one).append(first)
                .append(two).append(second)
                .append(three).append(third)
                .append(four);

        int res = one;
        res = calculate(res,first,two);
        res = calculate(res,second,three);
        res = calculate(res,third,four);

        result.set(res);
        return sb.toString();
    }

    public int calculate(int i,String m,int j){
        if ("+".equals(m)) return i+j;
        if ("-".equals(m)) return i-j;
        if ("x".equals(m)) return i*j;
        return 0;
    }

    public BufferedImage getImage() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();
        graphics.setColor(Color.white);
        graphics.fillRect(0, 0, width, height);  //绘制验证码图像背景为白色

        Font newFont = new Font("黑体", Font.BOLD, 30);// 黑体 加粗 大小为30
        FontMetrics fontMetrics = graphics.getFontMetrics(newFont);

        String string = produceString();// 随机生成运算字符串

        graphics.setColor(Color.green);
        graphics.setFont(newFont);
        //绘制绿色的验证码，并使其居中
        graphics.drawString(string, (width - fontMetrics.stringWidth(string)) / 2, (height - fontMetrics.getHeight()) / 2 + fontMetrics.getAscent());
        graphics.setColor(Color.blue);

        Random random = new Random();
        for (int i = 0; i < 10; ++i) {  //绘制10条干扰线，其颜色为蓝
            int x1 = random.nextInt(width), y1 = random.nextInt(height);
            int x2 = random.nextInt(width), y2 = random.nextInt(height);
            graphics.drawLine(x1, y1, x2, y2);
        }
        return image;
    }
}
