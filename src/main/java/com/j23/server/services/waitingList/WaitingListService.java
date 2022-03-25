package com.j23.server.services.waitingList;

import com.j23.server.models.waitingList.WaitingList;
import com.j23.server.repos.waitingList.WaitingListRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WaitingListService {

    private WaitingListRepository waitingListRepository;

    public List<WaitingList> findAllWaitingList() {
        return waitingListRepository.findAll();
    }
}
