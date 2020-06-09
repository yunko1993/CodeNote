package linghao.codenote.snmp;

import org.snmp4j.CommunityTarget;
import org.snmp4j.UserTarget;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;

public class SnmpUtil {

    public static final int DEFAULT_VERSION = SnmpConstants.version2c;
    public static final String DEFAULT_PROTOCOL = "udp";
    public static final int DEFAULT_PORT = 161;
    public static final long DEFAULT_TIMEOUT = 3 * 1000L;
    public static final int DEFAULT_RETRY = 3;

    /**
     * 创建共同体对象communityTarget
     *
     * @param address
     * @param community
     * @return CommunityTarget
     */
    public static CommunityTarget createMyDefaultTarget(String address,
                                                        String community) {
        Address targetAddress = GenericAddress.parse(address);
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(community));
        target.setAddress(targetAddress);
        target.setVersion(DEFAULT_VERSION);
        target.setTimeout(DEFAULT_TIMEOUT);// milliseconds
        target.setRetries(DEFAULT_RETRY);
        return target;
    }

    /**
     * 创建共同体对象communityTarget
     *
     * @param targetAddress
     * @param community
     * @param version
     * @return CommunityTarget
     */
    public static CommunityTarget createCommunityTarget(Address targetAddress,
                                                        String community, int version) {
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(community));
        target.setAddress(targetAddress);
        target.setVersion(version);
        return target;
    }

    /**
     * 创建共同体对象communityTarget
     *
     * @param targetAddress
     * @param community
     * @param version
     * @param timeOut
     * @param retry
     * @return CommunityTarget
     */
    public static CommunityTarget createCommunityTarget(Address targetAddress,
                                                        String community, int version, long timeOut, int retry) {
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(community));
        target.setAddress(targetAddress);
        target.setVersion(version);
        target.setTimeout(timeOut); // milliseconds
        target.setRetries(retry);
        return target;
    }

    /**
     * 创建共同体对象communityTarget
     *
     * @param address
     * @param community
     * @param version
     * @param timeOut
     * @param retry
     * @return CommunityTarget
     */
    public static CommunityTarget createCommunityTarget(String address,
                                                        String community, int version, long timeOut, int retry) {
        Address targetAddress = GenericAddress.parse(address);
        return createCommunityTarget(targetAddress, community, version,
                timeOut, retry);
    }

    /**
     * 创建snmp Address
     *
     * @param protocol
     * @param ip
     * @param port
     * @return Address
     */
    public static Address createAddress(String protocol, String ip, int port) {
        String address = protocol + ":" + ip + "/" + port;
        return GenericAddress.parse(address);
    }

    /**
     * 创建snmp udp Address
     *
     * @param ip
     * @param port
     * @return Address
     */
    public static Address createUdpAddress(String ip, int port) {
        String address = ip + "/" + port;
        return new UdpAddress(address);
    }

    /**
     * 创建snmp tcp Address
     *
     * @param ip
     * @param port
     * @return Address
     */
    public static TcpAddress createTcpAddress(String ip, int port) {
        String address = ip + "/" + port;
        return new TcpAddress(address);
    }

    /**
     * 创建 UserTarget
     *
     * @param targetAddress
     * @param version
     * @param timeOut
     * @param level
     * @param securityName
     * @return UserTarget
     */
    public static UserTarget createUserTarget(Address targetAddress,
                                              int version, long timeOut, int level, String securityName) {
        UserTarget target = new UserTarget();
        target.setAddress(targetAddress);
        target.setRetries(1);
        target.setTimeout(timeOut); // milliseconds
        target.setVersion(version);
        target.setSecurityLevel(level);
        target.setSecurityName(new OctetString(securityName));
        return target;
    }

    /**
     * 创建 UserTarget
     *
     * @param address
     * @param version
     * @param timeOut
     * @param level
     * @param securityName
     * @return UserTarget
     */
    public static UserTarget createUserTarget(String address, int version,
                                              long timeOut, int level, String securityName) {
        Address targetAddress = GenericAddress.parse(address);
        return createUserTarget(targetAddress, version, timeOut, level,
                securityName);
    }
}

