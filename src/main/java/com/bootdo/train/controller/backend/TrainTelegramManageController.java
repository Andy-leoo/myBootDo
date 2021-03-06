package com.bootdo.train.controller.backend;

import com.bootdo.common.utils.PageUtils;
import com.bootdo.common.utils.Query;
import com.bootdo.common.utils.R;
import com.bootdo.system.domain.UserDO;
import com.bootdo.system.service.UserService;
import com.bootdo.train.commons.Const;
import com.bootdo.train.pojo.TrainTelegram;
import com.bootdo.train.pojo.TrainTelegramUser;
import com.bootdo.train.service.TrainTelegramUserService;
import com.bootdo.train.service.impl.TrainTelegramServiceImpl;
import com.bootdo.train.utils.RegEx_util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//转发电报
@Controller
@RequestMapping("/train/telegram")
public class TrainTelegramManageController {

    private String prefix ="train/telegram";
    @Autowired
    private TrainTelegramServiceImpl trainTelegramService;
    @Autowired
    private UserService userService;
    @Autowired
    private TrainTelegramUserService trainTelegramUserService;


    @RequestMapping()
    public String index(){
        return prefix+"/telegram";
    }

    @RequestMapping("/list")
    @ResponseBody
    public PageUtils list(@RequestParam Map<String ,Object> params){
        Query query = new Query(params);
        List<TrainTelegram> trainTelegrams = trainTelegramService.selectAll(query);
        for (TrainTelegram trainTelegram: trainTelegrams) {
            String splitDetail = RegEx_util.splitAndFilterString(trainTelegram.getDetail(), 15);
            trainTelegram.setDetail(splitDetail);
        }
        int total = trainTelegramService.count(query);
        PageUtils pageUtil = new PageUtils(trainTelegrams, total);
        return pageUtil;
    }


    @RequestMapping("/add")
    public String add(ModelMap modelMap){
        List<UserDO> list = userService.list(new HashMap<>());
        modelMap.put("userList",list);
        return prefix+"/add";
    }

    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable("id")int id, ModelMap modelMap){
        List<UserDO> list = userService.list(new HashMap<>());
        modelMap.put("userList",list);
        TrainTelegram TrainTelegram = trainTelegramService.selectOneById(id);
        modelMap.put("TrainTelegram",TrainTelegram);
        List<TrainTelegramUser> trainTelegramUsers = trainTelegramUserService.selectByTelegramId(Long.valueOf(id));
        StringBuffer userIds = new StringBuffer();
        for (TrainTelegramUser trainTelegramUser :trainTelegramUsers){
            userIds.append(trainTelegramUser.getUserId()).append(",");
        }
        modelMap.put("userIds",userIds.toString());
        return prefix+"/edit";
    }



    @RequestMapping("/saveTelegram")
    @ResponseBody
    public R saveNews(TrainTelegram params, HttpSession session){
        UserDO user = (UserDO) session.getAttribute(Const.CURRENT_USER);
        if (StringUtils.isBlank(params.getTitle())){
            return R.error("标题不能为空");
        }
        if (StringUtils.isBlank(params.getDetail())){
            return  R.error("内容不能为空");
        }


        if (trainTelegramService.save(params,user)>0) {

            return  R.ok("新增成功");
        }

        return R.error();
    }

    @RequestMapping("/remove")
    @ResponseBody
    public R remove(int id){
        if (trainTelegramService.remove(id)>0) {
            return R.ok();
        }
        return R.error();
    }

    @RequestMapping("/detail/{id}")
    public String detail(@PathVariable("id") int id, ModelMap modelMap){
        TrainTelegram trainTelegram = trainTelegramService.selectOneById(id);
        modelMap.put("trainTelegram",trainTelegram);
        List<TrainTelegramUser> trainTelegramUsers = trainTelegramUserService.selectByTelegramId(Long.valueOf(id));
        modelMap.put("trainTelegramUsers",trainTelegramUsers);
        return prefix+"/detail";
    }

    @RequestMapping("/update")
    @ResponseBody
    public R update(TrainTelegram params, HttpServletRequest request){
        UserDO user = (UserDO) request.getSession().getAttribute(Const.CURRENT_USER);
        if (trainTelegramService.update(params,user)>0) {
            return  R.ok("更新成功");
        }
        return R.error();
    }

}
