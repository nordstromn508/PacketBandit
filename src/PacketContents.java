import jpcap.PacketReceiver;
import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;
import jpcap.packet.UDPPacket;

public class PacketContents implements PacketReceiver {
	public void receivePacket(Packet packet){
		if(packet instanceof TCPPacket) {
			TCPPacket pkt = (TCPPacket) packet;
			System.out.println("Src: " +pkt.src_port + " Dst: " + pkt.dst_port);
			if		((PacketBanditGUI.filterPort == -1) ||
					(pkt.src_port == PacketBanditGUI.filterPort) ||
					(pkt.dst_port == PacketBanditGUI.filterPort)) {
				write(Integer.toString(pkt.ident), pkt.src_ip.toString(), pkt.dst_ip.toString(), ("dd"+pkt.header.toString()+"dd"), Short.toString(pkt.length), Long.toString(pkt.ack_num), "\n");
			}
		} else if(packet instanceof UDPPacket) {
			UDPPacket pkt = (UDPPacket) packet;
			System.out.println("Src: " +pkt.src_port + " Dst: " + pkt.dst_port);
			if		((PacketBanditGUI.filterPort == -1) ||
					(pkt.src_port == PacketBanditGUI.filterPort) ||
					(pkt.dst_port == PacketBanditGUI.filterPort)) {
				write(Integer.toString(pkt.ident), pkt.src_ip.toString(), pkt.dst_ip.toString(), Short.toString(pkt.protocol), Integer.toString(pkt.length), "\n");
			}
		}
	}

	public void write(String... str) {
		for(String s : str)
			PacketBanditGUI.taOutput.append(s + "     ");
	}
}
