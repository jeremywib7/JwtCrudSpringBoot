package com.j23.server.controllers.waitingList;

import com.j23.server.models.waitingList.WaitingList;
import com.j23.server.services.waitingList.WaitingListService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/waitingList")
public class WaitingListController {

    private WaitingListService waitingListService;

    @GetMapping("/all")
    private List<WaitingList> findAllWaitingList() {
        return waitingListService.findAllWaitingList();
    }
}
