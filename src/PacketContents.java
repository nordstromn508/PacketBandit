import jpcap.PacketReceiver;
import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;
import jpcap.packet.UDPPacket;

public class PacketContents implements PacketReceiver {
    public static final byte space = -5;
    public static final byte IDSpace = (byte) (-5 + space);
    public static final byte IPSpace = (byte) (-19 + space);
    public static final byte TLPSpace = (byte) (-3 + space);
    public static final byte LengthSpace = (byte) (-4 + space);
    public static final byte AckSpace = (byte) (-10 + space);
    public static final byte HopSpace = (byte) (-3 + space);
    public static final byte ALPSpace = (byte) (-6 + space);

    public void receivePacket(Packet packet) {
        if (packet instanceof TCPPacket) {
            TCPPacket pkt = (TCPPacket) packet;
            if ((PacketBanditGUI.filterPort == -1) ||
                    (pkt.src_port == PacketBanditGUI.filterPort) ||
                    (pkt.dst_port == PacketBanditGUI.filterPort)) {
                if (pkt.ident != 0)
                    write(dynamicSpace(IDSpace, Integer.toString(pkt.ident)),
                            dynamicSpace(IPSpace, pkt.src_ip.toString().substring(1) + ":" + fixHttp(pkt.src_port)),
                            dynamicSpace(IPSpace, pkt.dst_ip.toString().substring(1) + ":" + fixHttp(pkt.dst_port)),
                            dynamicSpace(TLPSpace, "TCP"),
                            dynamicSpace(LengthSpace, Integer.toString(pkt.length)),
                            dynamicSpace(ALPSpace, convertPort(pkt.src_port, pkt.dst_port)),
                            dynamicSpace(AckSpace, Long.toString(pkt.ack_num)));
            }
        } else if (packet instanceof UDPPacket) {
            UDPPacket pkt = (UDPPacket) packet;
            if ((PacketBanditGUI.filterPort == -1) ||
                    (pkt.src_port == PacketBanditGUI.filterPort) ||
                    (pkt.dst_port == PacketBanditGUI.filterPort)) {
                if (pkt.ident != 0)
                    write(dynamicSpace(IDSpace, Integer.toString(pkt.ident)),
                            dynamicSpace(IPSpace, pkt.src_ip.toString().substring(1) + ":" + fixHttp(pkt.src_port)),
                            dynamicSpace(IPSpace, pkt.dst_ip.toString().substring(1) + ":" + fixHttp(pkt.dst_port)),
                            dynamicSpace(TLPSpace, "UDP"),
                            dynamicSpace(LengthSpace, Integer.toString(pkt.length)),
                            dynamicSpace(ALPSpace, convertPort(pkt.src_port, pkt.dst_port)),
                            dynamicSpace(HopSpace, Short.toString(pkt.hop_limit)));
            }
        }
    }

    public void write(String... str) {
        for (String s : str)
            PacketBanditGUI.taOutput.append(s);
        PacketBanditGUI.taOutput.append("\n");
    }

    public static String dynamicSpace(int n, String s) {
        return String.format("%" + n + "s", s);
    }

    public static String convertPort(int Src, int Dest) {
        if ((Src == 20 || Dest == 20) || (Src == 21 || Dest == 21)) {
            return "FTP";
        } else if (Src == 22 || Dest == 22) {
            return "SSH";
        } else if (Src == 23 || Dest == 23) {
            return "TELNET";
        } else if (Src == 25 || Dest == 25) {
            return "SMTP";
        } else if ((Src == 50 || Dest == 50) || (Src == 51 || Dest == 51)) {
            return "IPSEC";
        } else if (Src == 53 || Dest == 53) {
            return "DNS";
        } else if ((Src == 67 || Dest == 67) || (Src == 68 || Dest == 68)) {
            return "DHCP";
        } else if (Src == 69 || Dest == 69) {
            return "TFTP";
        } else if (Src == 8009 || Dest == 8009) {
            return "HTTP";
        } else if (Src == 110 || Dest == 110) {
            return "POP3";
        } else if (Src == 119 || Dest == 119) {
            return "NNTP";
        } else if (Src == 123 || Dest == 123) {
            return "NTP";
        } else if ((Src >= 135 && Src <= 139) || (Dest >= 135 && Dest <= 139)) {
            return "N-BIOS";
        } else if (Src == 143 || Dest == 143) {
            return "IMAP4";
        } else if ((Src == 161 || Dest == 161) || (Src == 162 || Dest == 162)) {
            return "SNMP";
        } else if (Src == 389 || Dest == 389) {
            return "LDAP";
        } else if (Src == 443 || Dest == 443) {
            return "SSL";
        }
        return "N/A";
    }

    public static String fixHttp(int port) {
        if(Integer.toString(port).equalsIgnoreCase("8009"))
            return "80";
        return Integer.toString(port);
    }
}