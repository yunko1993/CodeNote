package linghao.codenote.snmp;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Vector;

import org.snmp4j.CommandResponder;
import org.snmp4j.CommandResponderEvent;
import org.snmp4j.MessageDispatcherImpl;
import org.snmp4j.MessageException;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.StateReference;
import org.snmp4j.mp.StatusInformation;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TcpAddress;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.MultiThreadedMessageDispatcher;
import org.snmp4j.util.ThreadPool;

/**
 * @author 凌浩,
 * @date 2019/11/14,
 * @time 8:48,
 */
public class SnmpTrapReceiver implements CommandResponder {
    private MultiThreadedMessageDispatcher dispatcher;
    private Snmp snmp = null;
    private Address listenAddress;
    private ThreadPool threadPool;

    public SnmpTrapReceiver() {
    }

    //初始化监听。
    private void init() throws UnknownHostException, IOException {
        threadPool = ThreadPool.create("Trap", 2);
        dispatcher = new MultiThreadedMessageDispatcher(threadPool,
                new MessageDispatcherImpl());
        listenAddress = GenericAddress.parse(System.getProperty(
                "snmp4j.listenAddress", "udp:127.0.0.1/162")); // 本地IP与监听端口
        TransportMapping transport;
        // 对TCP与UDP协议进行处理
        if (listenAddress instanceof UdpAddress) {
            transport = new DefaultUdpTransportMapping(
                    (UdpAddress) listenAddress);
        } else {
            transport = new DefaultTcpTransportMapping(
                    (TcpAddress) listenAddress);
        }
        snmp = new Snmp(dispatcher, transport);
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv1());
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv2c());
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv3());
        USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(
                MPv3.createLocalEngineID()), 0);
        SecurityModels.getInstance().addSecurityModel(usm);
        snmp.listen();
    }

    public void run() {
        try {
            init();
            snmp.addCommandResponder(this);
            System.out.println("开始监听Trap信息!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 实现CommandResponder的processPdu方法, 用于处理传入的请求、PDU等信息 当接收到trap时，会自动进入这个方法
     *
     * @param respEvnt
     */
    public void processPdu(CommandResponderEvent respEvnt) {
        if (respEvnt != null && respEvnt.getPDU() != null) {
            PDU src_pdu = respEvnt.getPDU();
            // 需要确认trap
            if (src_pdu.getType() == PDU.INFORM) {
                PDU responsePDU = new PDU(src_pdu);
                responsePDU.setErrorIndex(0);
                responsePDU.setErrorStatus(0);
                responsePDU.setType(PDU.RESPONSE);
                StatusInformation statusInfo = new StatusInformation();
                StateReference stateRef = respEvnt.getStateReference();
                try {
                    respEvnt.getMessageDispatcher().returnResponsePdu(
                            respEvnt.getMessageProcessingModel(),
                            respEvnt.getSecurityModel(),
                            respEvnt.getSecurityName(),
                            respEvnt.getSecurityLevel(), responsePDU,
                            respEvnt.getMaxSizeResponsePDU(), stateRef,
                            statusInfo);

                } catch (MessageException msgEx) {
                    msgEx.printStackTrace();
                }
            }

            Vector<VariableBinding> recVBs = (Vector<VariableBinding>) respEvnt.getPDU()
                    .getVariableBindings();
            for (int i = 0; i < recVBs.size(); i++) {
                VariableBinding recVB = recVBs.elementAt(i);
                System.out
                        .println(recVB.getOid() + " : " + recVB.getVariable());
            }
        }

    }

    public static void main(String[] args) {
        //开启服务
        SnmpTrapReceiver multithreadedtrapreceiver = new SnmpTrapReceiver();
        multithreadedtrapreceiver.run();
    }
}
