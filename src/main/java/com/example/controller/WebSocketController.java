package com.example.controller;

import com.example.service.WebSocketServer;
import com.example.utils.IdGenerator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * @author Lex
 */
@CrossOrigin(origins = {"*"})
@RestController
public class WebSocketController {
    @GetMapping("/websocket/{cid}")
    public ModelAndView socket(@PathVariable String cid) {
        ModelAndView mav = new ModelAndView("/websocket");
        mav.addObject("cid", cid);
        return mav;
    }

    @GetMapping("/websocket/getId")
    public int getId() {
        return IdGenerator.getNextId();
    }

    @ResponseBody
    @RequestMapping("/websocket/push/{cid}")
    public String pushToWeb(@PathVariable String cid, String message) {
        try {
            // 目前的版本中,当打开网页建立websocket连接时并不会调用该方法
            WebSocketServer.sendInfo(message, cid);
        } catch (IOException e) {
            e.printStackTrace();
            return "推送失败";
        }
        return "发送成功";
    }
}
