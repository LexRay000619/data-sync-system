package com.example.controller;

import com.example.service.WebSocketServer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * @author Lex
 */
@CrossOrigin(origins = {"*"})
@RestController
public class MyController {
    @GetMapping("/websocket/{cid}")
    public ModelAndView socket(@PathVariable String cid){
        ModelAndView mav = new ModelAndView("/websocket");
        mav.addObject("cid",cid);
        return mav;
    }

    @ResponseBody
    @RequestMapping("/websocket/push/{cid}")
    public String pushToWeb(@PathVariable String cid,String message){
        try {
            WebSocketServer.sendInfo(message,cid);
        } catch (IOException e) {
            e.printStackTrace();
            return "推送失败";
        }
        return "发送成功";
    }
}
