import jpcap.PacketReceiver;
import jpcap.packet.Packet;

public class PacketContents implements PacketReceiver {
	public void receivePacket(Packet packet){
		PacketBanditGUI.taOutput.append(packet.toString() + "\n -----------------------------------------------------\n\n");
	}
}
