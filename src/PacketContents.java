import jpcap.PacketReceiver;
import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;
import jpcap.packet.UDPPacket;

public class PacketContents implements PacketReceiver {
	private int space = 17;
	public void receivePacket(Packet packet){
		if(packet instanceof TCPPacket) {
			TCPPacket pkt = (TCPPacket) packet;
			if		((PacketBanditGUI.filterPort == -1) ||
					(pkt.src_port == PacketBanditGUI.filterPort) ||
					(pkt.dst_port == PacketBanditGUI.filterPort)) {
				write(Integer.toString(pkt.ident), pkt.src_ip.toString().substring(1), pkt.dst_ip.toString().substring(1), Short.toString(pkt.protocol), Short.toString(pkt.length), Long.toString(pkt.ack_num));
			}
		} else if(packet instanceof UDPPacket) {
			UDPPacket pkt = (UDPPacket) packet;
			if		((PacketBanditGUI.filterPort == -1) ||
					(pkt.src_port == PacketBanditGUI.filterPort) ||
					(pkt.dst_port == PacketBanditGUI.filterPort)) {
				write(Integer.toString(pkt.ident), pkt.src_ip.toString().substring(1), pkt.dst_ip.toString().substring(1), Short.toString(pkt.protocol), Integer.toString(pkt.length));
			}
		}
	}

	public void write(String... str) {
		//if(PacketBanditGUI.taOutput.;)
		for(String s : str) {
			PacketBanditGUI.taOutput.append(String.format("%" + (-space) + "s", s));
		}
		PacketBanditGUI.taOutput.append("\n");
	}
}