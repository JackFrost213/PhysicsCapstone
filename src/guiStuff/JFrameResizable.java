package guiStuff;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class JFrameResizable extends JFrame{

	 public JFrameResizable() {
		 //addComponentListener(cl);
	    }
	
	 
	 ComponentListener cl=new ComponentAdapter()
	 {
	  public void componentResized(ComponentEvent event)
	  {
	   //this.componentResized(event);
	   JFrameResizable frame = (JFrameResizable) event.getComponent();
	   for(Component temp : frame.getComponents()) {
			temp.setSize(frame.getSize());
		}
	  }
	 };

}
