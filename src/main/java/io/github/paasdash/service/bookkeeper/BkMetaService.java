package io.github.paasdash.service.bookkeeper;

import io.github.paasdash.constant.BookkeeperConst;
import io.github.paasdash.module.bookkeeper.BookkeeperInstance;
import io.github.paasdash.service.zookeeper.ZkService;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BkMetaService {

    private final Map<String, BookkeeperInstance> bookkeeperInstanceMap;

    private final ZkService zkService;

    public BkMetaService(@Autowired BookkeeperInstanceService bookkeeperInstanceService,
                         @Autowired ZkService zkService) {
        this.bookkeeperInstanceMap = bookkeeperInstanceService.getBookkeeperInstanceMap();
        this.zkService = zkService;
    }

    public List<Long> listLedgers(String bookkeeperInstanceName){
        BookkeeperInstance instanceInfo = bookkeeperInstanceMap.get(bookkeeperInstanceName);
        if (instanceInfo == null){
            throw new NullPointerException(String.format("%s instance is not set", bookkeeperInstanceName));
        }
        List<Long> ledgers = new ArrayList<>();
        String zkInstanceName = instanceInfo.getZkInstanceName();
        try (ZooKeeper zooKeeper = zkService.newZookeeper(zkInstanceName)){
            List<String> floor1 = zkService.getChildren(zkInstanceName, BookkeeperConst.BOOKKEEPER_LEDGER_PATH);
            floor1.removeAll(List.of(
               BookkeeperConst.BOOKKEEPER_AVAILABLE,
               BookkeeperConst.BOOKKEEPER_IDGEN,
               BookkeeperConst.BOOKKEEPER_COOKIES,
               BookkeeperConst.BOOKKEEPER_LAYOUT,
               BookkeeperConst.BOOKKEEPER_UNDER_REPLICATION,
               BookkeeperConst.BOOKKEEPER_INSTANCE_ID
            ));
            for (String ledgerHead : floor1) {
                String absoluteLedgerHead = BookkeeperConst.BOOKKEEPER_LEDGER_PATH + "/" + ledgerHead;
                List<String> floor2 = zkService.getChildren(zkInstanceName, absoluteLedgerHead);
                for (String ledgerBody : floor2) {
                    String absoluteLedgerBody = absoluteLedgerHead + "/" + ledgerBody;
                    List<String> floor3 = zkService.getChildren(zkInstanceName, absoluteLedgerBody);
                    for (String ledgerFoot : floor3) {
                        try {
                            ledgers.add(Long.parseLong(
                                            ledgerHead
                                            + ledgerBody
                                            + ledgerFoot.replace("L", "")
                            ));
                        } catch (Throwable e){
                            // do nothing to avoid other format
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ledgers;
    }
}
