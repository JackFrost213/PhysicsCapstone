package shapes3D;

import java.util.ArrayList;
import java.util.Arrays;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;

import main.runnable.ChaosGraph;
import main.runnable.SimulationMain;

public class Trail extends Geometry3D {

	private ArrayList<Vector3f> positions;

	public Trail(ColorRGBA colorRGBA) {
		super("PolyLine", new Line(new Vector3f[0]));
		positions = new ArrayList<Vector3f>();
		try {
		Material mat = new Material(SimulationMain.assetManagerExternal, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", colorRGBA);
		mat.getAdditionalRenderState().setLineWidth(5);
		this.setMaterial(mat);
		}
		catch(Exception e) {
			
		}
		this.updateModelBound();
	}

	public Trail(Vector3f[] pos, ColorRGBA colorRGBA) {
		super("PolyLine", new Line(pos));
		positions = (ArrayList<Vector3f>) Arrays.asList(pos);
		Material mat = new Material(SimulationMain.assetManagerExternal, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", colorRGBA);
		mat.getAdditionalRenderState().setLineWidth(5);
		this.setMaterial(mat);
		this.updateModelBound();
	}

	public void addPosToTrail(Vector3f newPosition) {
		/*if (positions.size() > 1000) {
			// cull half of the points
			System.out.println("Culled Points");
			ArrayList<Vector3f> positionsCulled = new ArrayList<Vector3f>();
			for (int x = 0; x < positions.size(); x += 2) {
				positionsCulled.add(positions.get(x));
			}
			positions = positionsCulled;
		}*/
		//TODO Fix this and improve it so this scales to any simulation scale
		if (!ChaosGraph.generatingChaos && (positions.size() == 0 || newPosition.distance(positions.get(positions.size() - 1)) > SimulationMain.deltaX/5)) {
			positions.add(newPosition);
			Vector3f[] temp = new Vector3f[positions.size()];
			Vector3f[] pos = positions.toArray(temp);
			this.setMesh(new Line(pos));
		}
	}

}
