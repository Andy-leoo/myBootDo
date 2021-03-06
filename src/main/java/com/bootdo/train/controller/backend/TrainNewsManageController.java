package com.bootdo.train.controller.backend;

import com.bootdo.common.utils.PageUtils;
import com.bootdo.common.utils.Query;
import com.bootdo.common.utils.R;
import com.bootdo.system.domain.UserDO;
import com.bootdo.system.service.UserService;
import com.bootdo.train.commons.Const;
import com.bootdo.train.pojo.TrainNews;
import com.bootdo.train.pojo.TrainNewsUser;
import com.bootdo.train.service.TrainNewsUserService;
import com.bootdo.train.service.impl.TrainNewsServiceImpl;
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

@Controller
@RequestMapping("/train/news")
public class TrainNewsManageController {

    private String prefix ="train/news";
    @Autowired
    private TrainNewsServiceImpl trainNewsService;
    @Autowired
    private UserService userService;
    @Autowired
    private TrainNewsUserService trainNewsUserService;


    @RequestMapping()
    public String index(){
        return prefix+"/news";
    }

    @RequestMapping("/list")
    @ResponseBody
    public PageUtils list(@RequestParam Map<String ,Object> params){
        Query query = new Query(params);
        List<TrainNews> trainNews = trainNewsService.selectAll(query);
        for (TrainNews trainNew: trainNews) {
            String splitDetail = RegEx_util.splitAndFilterString(trainNew.getDetail(), 15);
            trainNew.setDetail(splitDetail);
        }
        int total = trainNewsService.count(query);
        PageUtils pageUtil = new PageUtils(trainNews, total);
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
        TrainNews trainNews = trainNewsService.selectOneById(id);
        modelMap.put("trainNews",trainNews);
        List<TrainNewsUser> trainNewsUsers = trainNewsUserService.selectByNewsId(Long.valueOf(id));
        StringBuffer userIds = new StringBuffer();
        for (TrainNewsUser trainNewsUser :trainNewsUsers){
            userIds.append(trainNewsUser.getUserId()).append(",");
        }
        modelMap.put("userIds",userIds.toString());
        return prefix+"/edit";
    }



    @RequestMapping("/saveNews")
    @ResponseBody
    public R saveNews(TrainNews params, HttpSession session){
        UserDO user = (UserDO) session.getAttribute(Const.CURRENT_USER);
        if (StringUtils.isBlank(params.getTitle())){
            return R.error("标题不能为空");
        }
        if (StringUtils.isBlank(params.getDetail())){
            return  R.error("内容不能为空");
        }


        if (trainNewsService.save(params,user)>0) {

            return  R.ok("新增成功");
        }

        return R.error();
    }

    @RequestMapping("/remove")
    @ResponseBody
    public R remove(int id){
        if (trainNewsService.remove(id)>0) {
            return R.ok();
        }
        return R.error();
    }

    @RequestMapping("/detail/{id}")
    public String detail(@PathVariable("id") int id, ModelMap modelMap){
        TrainNews trainNews = trainNewsService.selectOneById(id);
        modelMap.put("trainNews",trainNews);
        List<TrainNewsUser> trainNewsUsers = trainNewsUserService.selectByNewsId(Long.valueOf(id));
        modelMap.put("newsUsers",trainNewsUsers);
        return prefix+"/detail";
    }

    @RequestMapping("/update")
    @ResponseBody
    public R update(TrainNews params, HttpServletRequest request){
        UserDO user = (UserDO) request.getSession().getAttribute(Const.CURRENT_USER);
        if (trainNewsService.update(params,user)>0) {
            return  R.ok("更新成功");
        }
        return R.error();
    }

}
