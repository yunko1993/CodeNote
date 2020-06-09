package linghao.codenote.snmp;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.util.Vector;


public class SnmpGet {

    private static int version = SnmpConstants.version1;
    private static String protocol = "udp";
    private static int port = 161;

    /**
     * @param args
     */
    public static void main(String[] args) {

//        String ip = "127.0.0.1";
//        String ip = "111.231.249.224";
        String ip = "88.1.10.178";
        String community = "public";
//        String oidval = ".1.3.6.1.2.1.1.5.0";
        String oidval = ".1.3.6.1.2.1.1.5.0";
        SnmpGet tester = new SnmpGet();
        tester.snmpGet(ip, community, oidval);

    }

    @SuppressWarnings("unchecked")
    private void snmpGet(String ip, String community, String oid) {

        String address = protocol + ":" + ip + "/" + port;
        CommunityTarget target = SnmpUtil.createCommunityTarget(address,
                community, version, 2 * 1000L, 3);
        DefaultUdpTransportMapping udpTransportMapping = null;
        Snmp snmp = null;
        try {
            PDU pdu = new PDU();
            // pdu.add(new VariableBinding(new OID(new int[]
            // {1,3,6,1,2,1,1,2})));
            pdu.add(new VariableBinding(new OID(oid)));
            pdu.setType(PDU.GET);

            udpTransportMapping = new DefaultUdpTransportMapping();
            udpTransportMapping.listen();
            snmp = new Snmp(udpTransportMapping);

            // 发送同步消息
            ResponseEvent response = snmp.send(pdu, target);
            System.out.println("PeerAddress:" + response.getPeerAddress());
            PDU responsePdu = response.getResponse();

            if (responsePdu == null) {
                System.out.println(ip + ":Request time out");
            } else {
                Vector vbVect = responsePdu.getVariableBindings();
                System.out.println("vb size:" + vbVect.size());
                if (vbVect.size() == 0) {
                    System.out.println(" pdu vb size is 0 ");
                } else {
                    Object obj = vbVect.firstElement();
                    VariableBinding vb = (VariableBinding) obj;
                    System.out.println(vb.getOid() + " = " + vb.getVariable());
                }

            }
            System.out.println("success finish snmp get the oid!");
        } catch (Exception e) {
            System.out.println("SNMP Get Exception:" + e);
        } finally {
            if (snmp != null) {
                try {
                    snmp.close();
                } catch (IOException ex1) {
                    snmp = null;
                }
            }
            if (udpTransportMapping != null) {
                try {
                    udpTransportMapping.close();
                } catch (IOException ex2) {
                    udpTransportMapping = null;
                }
            }
        }
    }
}

