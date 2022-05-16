package com.j23.server.services.waitingList;

import com.j23.server.models.waitingList.CountdownWaitingList;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class WaitingListService {

  public List<CountdownWaitingList> countdownWaitingLists = new ArrayList<>();

}
